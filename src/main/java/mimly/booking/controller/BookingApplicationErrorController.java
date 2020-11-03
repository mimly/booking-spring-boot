package mimly.booking.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class BookingApplicationErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<BookingApplicationError> handleNotFoundErrors(HttpServletRequest httpServletRequest) {
        return new ResponseEntity<>(BookingApplicationError.builder()
                .httpServletRequest(httpServletRequest)
                .message("The requested resource could not be found")
                .httpStatus(HttpStatus.NOT_FOUND)
                .build(), HttpStatus.NOT_FOUND);
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
