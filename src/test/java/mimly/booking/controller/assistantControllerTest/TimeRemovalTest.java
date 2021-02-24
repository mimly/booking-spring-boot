package mimly.booking.controller.assistantControllerTest;

import mimly.booking.model.TimeSlot;
import mimly.booking.model.repository.AssistantRepository;
import mimly.booking.model.repository.TimeSlotRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Arrays;

import static mimly.booking.config.SecurityConfig.ROLE_ASSISTANT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TimeRemovalTest {

    private final MockMvc mockMvc;
    private final AssistantRepository assistantRepository;
    private final TimeSlotRepository timeSlotRepository;

    private final String URL_ADRESS = "/api/secured/v1/times/";
    private final String LOREM = "Lorem";
    private final String IPSUM = "Ipsum";
    private long LOREM_TIME_ID;
    private long IPSUM_TIME_ID;

    @Autowired
    public TimeRemovalTest(MockMvc mockMvc, AssistantRepository assistantRepository, TimeSlotRepository timeSlotRepository) {
        this.mockMvc = mockMvc;
        this.assistantRepository = assistantRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    @BeforeEach
    public void createTimes() throws Exception {
        for (final String assistant : Arrays.asList(LOREM, IPSUM)) {
            TimeSlot timeSlot = new TimeSlot();
            timeSlot.setTime("11:11");
            timeSlot.setAssistant(this.assistantRepository.findByUsername(assistant));
            this.timeSlotRepository.saveAndFlush(timeSlot);
            if (assistant.equals(LOREM)) {
                LOREM_TIME_ID = timeSlot.getId();
            } else if (assistant.equals(IPSUM)) {
                IPSUM_TIME_ID = timeSlot.getId();
            }
        }
    }

    @AfterEach
    public void removeTimes() throws Exception {
        if (this.timeSlotRepository.existsById(LOREM_TIME_ID)) {
            this.timeSlotRepository.deleteById(LOREM_TIME_ID);
        }
        if (this.timeSlotRepository.existsById(IPSUM_TIME_ID)) {
            this.timeSlotRepository.deleteById(IPSUM_TIME_ID);
        }
        this.timeSlotRepository.flush();
    }

    @Test
    @WithMockUser(username = LOREM, password = LOREM, authorities = {ROLE_ASSISTANT})
    public void validTime() throws Exception {
        this.mockMvc.perform(delete(URL_ADRESS + LOREM_TIME_ID)
                .with(csrf())
        ).andExpect(ResultMatcher.matchAll(status().is(200)));
    }

    @Test
    @WithMockUser(username = LOREM, password = LOREM, authorities = {ROLE_ASSISTANT})
    public void timeNotFound() throws Exception {
        this.mockMvc.perform(delete(URL_ADRESS + (LOREM_TIME_ID + 2))
                .with(csrf())
        ).andExpect(ResultMatcher.matchAll(status().is(404)));

        this.mockMvc.perform(delete(URL_ADRESS + Long.MAX_VALUE)
                .with(csrf())
        ).andExpect(ResultMatcher.matchAll(status().is(404)));
    }

    @Test
    @WithMockUser(username = LOREM, password = LOREM, authorities = {ROLE_ASSISTANT})
    public void unauthorizedOperation() throws Exception {
        this.mockMvc.perform(delete(URL_ADRESS + IPSUM_TIME_ID)
                .with(csrf())
        ).andExpect(ResultMatcher.matchAll(status().is(403)));
    }

    @Test
    @WithMockUser(username = LOREM, password = LOREM, authorities = {ROLE_ASSISTANT})
    public void unacceptedMediaTypeWithNoContent() throws Exception {
        this.mockMvc.perform(delete(URL_ADRESS + LOREM_TIME_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
        ).andExpect(ResultMatcher.matchAll(status().is(200)));
    }

    @Test
    @WithMockUser(username = LOREM, password = LOREM, authorities = {ROLE_ASSISTANT})
    public void unacceptedMediaType() throws Exception {
        this.mockMvc.perform(delete(URL_ADRESS + LOREM_TIME_ID)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("time=11:11")
                .with(csrf())
        ).andExpect(ResultMatcher.matchAll(status().is(200)));
    }

    @ParameterizedTest
    @WithMockUser(username = LOREM, password = LOREM, authorities = {ROLE_ASSISTANT})
    @ValueSource(strings = {
            "",
            "time=11:11",
            "{}",
            "/",
            "0",
            "-1"
    })
    public void wrongFormat(final String param) throws Exception {
        this.mockMvc.perform(delete(URL_ADRESS + param)
                .with(csrf())
        ).andExpect(ResultMatcher.matchAll(status().is4xxClientError()));
    }

    @Test
    public void validTimeUnauthenticated() throws Exception {
        this.mockMvc.perform(delete(URL_ADRESS + LOREM_TIME_ID)
                .with(csrf())
        ).andExpect(ResultMatcher.matchAll(status().is(401)));
    }

    @Test
    public void timeNotFoundUnauthenticated() throws Exception {
        this.mockMvc.perform(delete(URL_ADRESS + (LOREM_TIME_ID + 2))
                .with(csrf())
        ).andExpect(ResultMatcher.matchAll(status().is(401)));

        this.mockMvc.perform(delete(URL_ADRESS + Long.MAX_VALUE)
                .with(csrf())
        ).andExpect(ResultMatcher.matchAll(status().is(401)));
    }

    @Test
    public void unauthorizedOperationUnauthenticated() throws Exception {
        this.mockMvc.perform(delete(URL_ADRESS + IPSUM_TIME_ID)
                .with(csrf())
        ).andExpect(ResultMatcher.matchAll(status().is(401)));
    }

    @Test
    public void unacceptedMediaTypeWithNoContentUnauthenticated() throws Exception {
        this.mockMvc.perform(delete(URL_ADRESS + LOREM_TIME_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
        ).andExpect(ResultMatcher.matchAll(status().is(401)));
    }

    @Test
    public void unacceptedMediaTypeUnauthenticated() throws Exception {
        this.mockMvc.perform(delete(URL_ADRESS + LOREM_TIME_ID)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("time=11:11")
                .with(csrf())
        ).andExpect(ResultMatcher.matchAll(status().is(401)));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "time=11:11",
            "{}",
            "/",
            "0",
            "-1"
    })
    public void wrongFormatUnauthenticated(final String param) throws Exception {
        this.mockMvc.perform(delete(URL_ADRESS + param)
                .with(csrf())
        ).andExpect(ResultMatcher.matchAll(status().is(401)));
    }
}