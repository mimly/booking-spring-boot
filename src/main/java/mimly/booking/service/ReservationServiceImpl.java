package mimly.booking.service;

import lombok.extern.slf4j.Slf4j;
import mimly.booking.model.TimeSlot;
import mimly.booking.model.event.ReservationExpiredEvent;
import mimly.booking.model.repository.AssistantRepository;
import mimly.booking.model.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static mimly.booking.config.AddressBook.Topic.RESERVATION_SERVICE_TOPIC;

@Service
@Slf4j(topic = RESERVATION_SERVICE_TOPIC)
public class ReservationServiceImpl implements ReservationService {

    private final AssistantRepository assistantRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final ScheduledExecutorService executorService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final Map<ScheduledFuture<?>, ReservationExpiredEvent> currentReservations;

    @Autowired
    public ReservationServiceImpl(AssistantRepository assistantRepository, TimeSlotRepository timeSlotRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.assistantRepository = assistantRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.applicationEventPublisher = applicationEventPublisher;
        this.currentReservations = new ConcurrentHashMap<>();
    }

    @Override
    public List<TimeSlot> getAllTimeSlots() {
        return this.timeSlotRepository.findAll();
    }

    private TimeSlot findTimeSlot(TimeSlot timeSlot) {
        return this.timeSlotRepository.findById(timeSlot.getId())
                .orElseThrow(() -> new AccessDeniedException("Time not found"));
    }

    @Override
    public synchronized TimeSlot makeReservation(TimeSlot timeSlot) {
        TimeSlot reservedTimeSlot = this.findTimeSlot(timeSlot);
        if (reservedTimeSlot.getConfirmedBy() != null)
            throw new AccessDeniedException("Time already taken");
        if (reservedTimeSlot.getReserved())
            throw new AccessDeniedException("Time already reserved");
        reservedTimeSlot.setReserved(true);
        this.timeSlotRepository.saveAndFlush(reservedTimeSlot);
        return reservedTimeSlot;
    }

    @Override
    public void setReservationTimeout(TimeSlot timeSlot, String destination) {
        final ReservationExpiredEvent reservationExpiredEvent = new ReservationExpiredEvent(this, timeSlot, destination);
        ScheduledFuture<?> scheduledFuture = this.executorService.schedule(
                () -> this.applicationEventPublisher.publishEvent(reservationExpiredEvent),
                10,
                TimeUnit.SECONDS
        );
        this.currentReservations.put(scheduledFuture, reservationExpiredEvent);
    }

    @Override
    public void unsetReservationTimeout(TimeSlot timeSlot) {
        this.currentReservations.entrySet().stream()
                .filter(entry -> entry.getValue().getReservedTimeSlot().equals(timeSlot))
                .forEach(entry -> entry.getKey().cancel(true));
        this.currentReservations.entrySet().removeIf(entry -> entry.getValue().getReservedTimeSlot().equals(timeSlot));
    }

    @Override
    public TimeSlot cancelReservation(TimeSlot timeSlot) {
        TimeSlot cancelledTimeSlot = this.findTimeSlot(timeSlot);
        cancelledTimeSlot.setReserved(false);
        this.timeSlotRepository.saveAndFlush(cancelledTimeSlot);
        return cancelledTimeSlot;
    }

    @Override
    public TimeSlot confirmReservation(TimeSlot timeSlot) {
        TimeSlot confirmedTimeSlot = this.findTimeSlot(timeSlot);
        confirmedTimeSlot.setReserved(true);
        confirmedTimeSlot.setConfirmedBy(timeSlot.getConfirmedBy());
        this.timeSlotRepository.saveAndFlush(confirmedTimeSlot);
        return confirmedTimeSlot;
    }
}
