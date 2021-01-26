package mimly.booking.controller;

import lombok.extern.slf4j.Slf4j;
import mimly.booking.model.TimeSlot;
import mimly.booking.model.dto.AdditionDTO;
import mimly.booking.service.AssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.security.Principal;
import java.util.List;

import static mimly.booking.config.AddressBook.APIv1_SECURED;
import static mimly.booking.config.AddressBook.HttpEndpoint.ADD_TIME;
import static mimly.booking.config.AddressBook.HttpEndpoint.REMOVE_TIME;
import static mimly.booking.config.AddressBook.Topic.ASSISTANT_CONTROLLER_TOPIC;
import static mimly.booking.config.AddressBook.WebSocketEndpoint.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequestMapping(APIv1_SECURED)
@Slf4j(topic = ASSISTANT_CONTROLLER_TOPIC)
public class AssistantController {

    private final AssistantService assistantService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public AssistantController(AssistantService assistantService, SimpMessagingTemplate simpMessagingTemplate) {
        this.assistantService = assistantService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Transactional(rollbackFor = AccessDeniedException.class)
    @PostMapping(value = ADD_TIME, consumes = APPLICATION_JSON_VALUE)
    public TimeSlot addTimeSlot(@RequestBody @Valid AdditionDTO additionDTO, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            String error = errors.get(errors.size() - 1).getDefaultMessage();
            throw new AccessDeniedException(error);
        }

        TimeSlot addedTimeSlot = this.assistantService.addTimeSlot(additionDTO, principal);
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create();
        accessor.addNativeHeader(ORIGIN, POST);
        this.simpMessagingTemplate.convertAndSend(BROADCAST_RESERVATIONS, addedTimeSlot, accessor.getMessageHeaders());
        return addedTimeSlot;
    }

    @Transactional(rollbackFor = AccessDeniedException.class)
    @DeleteMapping(value = REMOVE_TIME, consumes = APPLICATION_JSON_VALUE)
    public TimeSlot removeTimeSlot(@PathVariable @NotNull(message = "Time is null") @Pattern(regexp = "^[1-9]+[0-9]*$", message = "Only positive IDs") String id, Principal principal) {
        TimeSlot removedTimeSlot = this.assistantService.removeTimeSlot(Long.valueOf(id), principal);
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create();
        accessor.addNativeHeader(ORIGIN, DELETE);
        this.simpMessagingTemplate.convertAndSend(BROADCAST_RESERVATIONS, removedTimeSlot, accessor.getMessageHeaders());
        return removedTimeSlot;
    }
}
