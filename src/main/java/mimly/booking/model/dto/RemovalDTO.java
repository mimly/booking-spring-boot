//package mimly.booking.model.dto;
//
//import lombok.Value;
//
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Pattern;
//
//@Value
//public class RemovalDTO {
//
//    @NotNull(message = "Time is null")
//    @Pattern(regexp = "^[1-9]+[0-9]*$", message = "Only positive IDs")
//    String id;
//
//    public long asLong() {
//        return Long.parseLong(this.id);
//    }
//}