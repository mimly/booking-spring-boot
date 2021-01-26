package mimly.booking.service;

import mimly.booking.model.TimeSlot;

import java.util.List;

public interface ReservationService {

    List<TimeSlot> getAllTimeSlots();

    TimeSlot makeReservation(TimeSlot timeSlot);

    void setReservationTimeout(TimeSlot timeSlot, String destination);

    void unsetReservationTimeout(TimeSlot timeSlot);

    TimeSlot cancelReservation(TimeSlot timeSlot);

    TimeSlot confirmReservation(TimeSlot timeSlot);
}
