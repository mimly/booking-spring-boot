package mimly.booking.model.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class AuthenticationDTO {

    String assistant;
    boolean authenticated;
}