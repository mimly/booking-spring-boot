package mimly.booking.model.dto;

import lombok.Value;

import java.security.Principal;

@Value
public class PrincipalDTO {

    boolean authenticated;
    String assistant;

    public PrincipalDTO(Principal principal) {
        this.authenticated = principal != null;
        this.assistant = principal != null ? principal.getName() : null;
    }
}