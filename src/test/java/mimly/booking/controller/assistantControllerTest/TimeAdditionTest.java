package mimly.booking.controller.assistantControllerTest;

import mimly.booking.controller.TestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
public class TimeAdditionTest {

    private final TestHelper testHelper;

    @Autowired
    public TimeAdditionTest(TestHelper testHelper) {
        this.testHelper = testHelper;
    }

    @Test
    public void timeAlreadyExists() throws Exception {
//        testWithContentAndExpectFailure("username=&password=mimly1&confirm=mimly1");
    }

    @Test
    public void usernameIsEmpty() throws Exception {
//        testWithContentAndExpectFailure("username=&password=mimly1&confirm=mimly1");
    }

    @Test
    public void usernameIsNull() throws Exception {
//        testWithContentAndExpectFailure("password=mimly1&confirm=mimly1");
    }
}