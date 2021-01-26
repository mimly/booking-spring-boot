package mimly.booking.service;

import mimly.booking.model.Assistant;
import mimly.booking.model.TimeSlot;
import mimly.booking.model.dto.AdditionDTO;

import java.security.Principal;

public interface AssistantService {

    Assistant getAssistant(Principal principal);

    TimeSlot addTimeSlot(AdditionDTO additionDTO, Principal principal);

    TimeSlot removeTimeSlot(Long id, Principal principal);
}
