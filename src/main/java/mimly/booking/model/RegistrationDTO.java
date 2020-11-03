//package mimly.booking.model;
//
//import lombok.Value;
//
//import javax.validation.constraints.*;
//
//@Value
//public class RegistrationDTO {
//
//    @NotNull
//    @NotEmpty
//    String username;
//    @NotNull
//    @NotEmpty
//    @Size(min = 3, max = 32)
//    String password;
//    @NotNull
//    @NotEmpty
//    @Size(min = 3, max = 32)
//    String confirm;
//
//    @AssertTrue(message = "Passwords didn't match")
//    public boolean isConfirmed() {
//        if (this.password == null) return true;
//        return this.password.equals(this.confirm);
//    }
//
//    @AssertTrue(message = "Password didn't contain any number")
//    public boolean isAnyDigitPresent() {
//        if (this.password == null) return true;
//        return this.password.matches(".*\\d.*");
//    }
//}
