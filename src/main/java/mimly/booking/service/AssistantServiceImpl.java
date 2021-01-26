package mimly.booking.service;

import lombok.extern.slf4j.Slf4j;
import mimly.booking.model.Assistant;
import mimly.booking.model.TimeSlot;
import mimly.booking.model.dto.AdditionDTO;
import mimly.booking.model.repository.AssistantRepository;
import mimly.booking.model.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

import static mimly.booking.config.AddressBook.Topic.ASSISTANT_SERVICE_TOPIC;
import static mimly.booking.config.SecurityConfig.ROLE_ASSISTANT;

@Service
@Slf4j(topic = ASSISTANT_SERVICE_TOPIC)
public class AssistantServiceImpl implements AssistantService {

    private final AssistantRepository assistantRepository;
    private final TimeSlotRepository timeSlotRepository;

    @Autowired
    public AssistantServiceImpl(AssistantRepository assistantRepository, TimeSlotRepository timeSlotRepository) {
        this.assistantRepository = assistantRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    @Override
    public Assistant getAssistant(Principal principal) {
        return this.assistantRepository.findByUsername(principal.getName());
    }

    private boolean isTimeAlreadyAdded(String time, Principal principal) {
        Assistant assistant = this.assistantRepository.findByUsername(principal.getName());
        return assistant.getTimeSlots().stream()
                .map(TimeSlot::getTime)
                .anyMatch(time::equals);
    }

    @Override
    @Secured(ROLE_ASSISTANT)
    public TimeSlot addTimeSlot(AdditionDTO additionDTO, Principal principal) {
        TimeSlot addedTimeSlot = new TimeSlot();
        if (this.isTimeAlreadyAdded(additionDTO.getTime(), principal))
            throw new AccessDeniedException("Time already exist");
        addedTimeSlot.setTime(additionDTO.getTime());
        addedTimeSlot.setAssistant(this.getAssistant(principal));
        this.timeSlotRepository.saveAndFlush(addedTimeSlot);
        return addedTimeSlot;
    }

    @Override
    @Secured(ROLE_ASSISTANT)
    public TimeSlot removeTimeSlot(Long id, Principal principal) {
        Optional<TimeSlot> optionalTimeSlot = this.timeSlotRepository.findById(id);
        if (!optionalTimeSlot.isPresent())
            throw new AccessDeniedException("Time not found");
        TimeSlot removedTimeSlot = optionalTimeSlot.get();
        if (!principal.getName().equals(removedTimeSlot.getAssistant().getUsername()))
            throw new AccessDeniedException("Unauthorized operation");
        this.getAssistant(principal).getTimeSlots().remove(removedTimeSlot);
        this.timeSlotRepository.delete(removedTimeSlot);
        this.timeSlotRepository.flush();
        return removedTimeSlot;
    }
}
