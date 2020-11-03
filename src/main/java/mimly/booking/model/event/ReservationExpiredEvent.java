package mimly.booking.model.event;

import lombok.Getter;
import mimly.booking.model.TimeSlot;
import org.springframework.context.ApplicationEvent;

public class ReservationExpiredEvent extends ApplicationEvent {

    @Getter
    private final TimeSlot reservedTimeSlot;
    @Getter
    private final String destination;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public ReservationExpiredEvent(Object source, TimeSlot reservedTimeSlot, String destination) {
        super(source);
        this.reservedTimeSlot = reservedTimeSlot;
        this.destination = destination;
    }
}
