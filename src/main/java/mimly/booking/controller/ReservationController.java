package mimly.booking.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import mimly.booking.model.TimeSlot;
import mimly.booking.model.event.ReservationExpiredEvent;
import mimly.booking.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static mimly.booking.config.AddressBook.APIv1;
import static mimly.booking.config.AddressBook.WS_APIv1;
import static mimly.booking.config.AddressBook.WebSocketEndpoint.*;

@RestController
@RequestMapping(APIv1)
@MessageMapping(WS_APIv1)
@Slf4j
public class ReservationController implements ApplicationListener<ReservationExpiredEvent> {

    private final ReservationService reservationService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public ReservationController(ReservationService reservationService, SimpMessagingTemplate simpMessagingTemplate) {
        this.reservationService = reservationService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping(TIMES)
    @SendToUser(UNICAST_TIMES)
    public List<TimeSlot> getAllTimeSlots() {
        return this.reservationService.getAllTimeSlots();
    }

    @MessageMapping(MAKE_RESERVATION)
    @SendToUser(UNICAST_RESERVATION_STARTED)
    @SendTo(BROADCAST_RESERVATIONS)
    public TimeSlot makeReservation(TimeSlot timeSlot, SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
        TimeSlot reservedTimeSlot = this.reservationService.makeReservation(timeSlot);
        this.reservationService.setReservationTimeout(reservedTimeSlot, simpMessageHeaderAccessor.getSessionId());
        return reservedTimeSlot;
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(ReservationExpiredEvent event) {
        String destination = event.getDestination();
        TimeSlot reservedTimeSlot = event.getReservedTimeSlot();
        TimeSlot expiredTimeSlot = this.reservationService.cancelReservation(reservedTimeSlot);

        // ??? !!!
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create();
        accessor.setHeader(SimpMessageHeaderAccessor.SESSION_ID_HEADER, destination);
        this.simpMessagingTemplate.convertAndSendToUser(destination, UNICAST_RESERVATION_ENDED, expiredTimeSlot, accessor.getMessageHeaders());
        this.simpMessagingTemplate.convertAndSend(BROADCAST_RESERVATIONS, expiredTimeSlot);
    }

    @MessageMapping(CANCEL_RESERVATION)
    @SendToUser(UNICAST_RESERVATION_ENDED)
    @SendTo(BROADCAST_RESERVATIONS)
    public TimeSlot cancelReservation(TimeSlot timeSlot) {
        this.reservationService.unsetReservationTimeout(timeSlot);
        TimeSlot cancelledTimeSlot = this.reservationService.cancelReservation(timeSlot);
        return cancelledTimeSlot;
    }

    @MessageMapping(CONFIRM_RESERVATION)
    @SendToUser(UNICAST_RESERVATION_ENDED)
    @SendTo(BROADCAST_RESERVATIONS)
    public TimeSlot confirmReservation(TimeSlot timeSlot) {
        this.reservationService.unsetReservationTimeout(timeSlot);
        TimeSlot confirmedTimeSlot = this.reservationService.confirmReservation(timeSlot);
        return confirmedTimeSlot;
    }

    @MessageExceptionHandler(AccessDeniedException.class)
    @SendToUser(UNICAST_RESERVATION_ENDED)
    public BookingApplicationError handleAccessDeniedException(AccessDeniedException accessDeniedException) {
        return BookingApplicationError.builder()
                .httpStatus(HttpStatus.FORBIDDEN)
                .throwable(accessDeniedException)
                .build();
    }
}