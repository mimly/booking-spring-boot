package mimly.booking.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class AdditionDTO {

    @NotNull(message = "Time is null")
    @Pattern(regexp = "^([01][0-9]|2[0-3]):[0-5][0-9]$", message = "Wrong time format")
    private String time;
}
