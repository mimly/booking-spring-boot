package mimly.booking;

import mimly.booking.controller.AssistantController;
import mimly.booking.controller.AuthenticationController;
import mimly.booking.controller.ReservationController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BookingApplicationTests {

    private final AssistantController assistantController;
    private final AuthenticationController authenticationController;
    private final ReservationController reservationController;

    @Autowired
    BookingApplicationTests(AssistantController assistantController, AuthenticationController authenticationController, ReservationController reservationController) {
        this.assistantController = assistantController;
        this.authenticationController = authenticationController;
        this.reservationController = reservationController;
    }

    @Test
    void contextLoads() {
        assertThat(assistantController).isNotNull();
        assertThat(authenticationController).isNotNull();
        assertThat(reservationController).isNotNull();
    }
}
