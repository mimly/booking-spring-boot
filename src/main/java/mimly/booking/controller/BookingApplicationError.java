package mimly.booking.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static mimly.booking.config.AddressBook.API_VERSION;

@Builder
@Value
public class BookingApplicationError {

    @JsonIgnore
    HttpServletRequest httpServletRequest;
    @JsonIgnore
    HttpStatus httpStatus;
    @JsonIgnore
    Throwable throwable;

    String apiVersion = API_VERSION;
    String error;
    String message;
    String path;
    int status;
    String timestamp = String.valueOf(ZonedDateTime.now().truncatedTo(ChronoUnit.MILLIS));

    public String getError() {
        return httpStatus.getReasonPhrase();
    }

    public String getMessage() {
        if (throwable != null) {
            return throwable.getMessage();
        }
        return message;
    }

    public String getPath() {
        if (httpServletRequest != null) {
            return httpServletRequest.getRequestURI();
        }
        return path;
    }

    public int getStatus() {
        return httpStatus.value();
    }
}