package mimly.booking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class BookingApplicationControllerAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BookingApplicationError> handleConstraintViolationException(HttpServletRequest httpServletRequest, ConstraintViolationException constraintViolationException) {
        return new ResponseEntity<>(BookingApplicationError.builder()
                .httpServletRequest(httpServletRequest)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .throwable(constraintViolationException)
                .build(), HttpStatus.BAD_REQUEST);
    }
}