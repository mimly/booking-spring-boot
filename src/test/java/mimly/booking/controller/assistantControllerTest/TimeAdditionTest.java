package mimly.booking.controller.assistantControllerTest;

import org.junit.jupiter.api.Order;
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

import static mimly.booking.config.SecurityConfig.ROLE_ASSISTANT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TimeAdditionTest {

    private final MockMvc mockMvc;

    private final String URL_ADRESS = "/api/secured/v1/times/";
    private final String LOREM = "Lorem";

    @Autowired
    public TimeAdditionTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @Order(1)
    @WithMockUser(username = LOREM, password = LOREM, authorities = {ROLE_ASSISTANT})
    public void validTime() throws Exception {
        this.mockMvc.perform(post(URL_ADRESS)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"time\":\"11:11\"}")
                .with(csrf())
        ).andExpect(ResultMatcher.matchAll(status().is(200)));
    }

    @Test
    @Order(2)
    @WithMockUser(username = LOREM, password = LOREM, authorities = {ROLE_ASSISTANT})
    public void timeAlreadyExists() throws Exception {
        this.mockMvc.perform(post(URL_ADRESS)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"time\":\"11:11\"}")
                .with(csrf())
        ).andExpect(ResultMatcher.matchAll(status().is(403)));
    }

    @Test
    @WithMockUser(username = LOREM, password = LOREM, authorities = {ROLE_ASSISTANT})
    public void noContent() throws Exception {
        this.mockMvc.perform(post(URL_ADRESS)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
        ).andExpect(ResultMatcher.matchAll(status().is4xxClientError()));
    }

    @Test
    @WithMockUser(username = LOREM, password = LOREM, authorities = {ROLE_ASSISTANT})
    public void unacceptedMediaType() throws Exception {
        this.mockMvc.perform(post(URL_ADRESS)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("time=11:11")
                .with(csrf())
        ).andExpect(ResultMatcher.matchAll(status().is4xxClientError()));
    }

    @ParameterizedTest
    @WithMockUser(username = LOREM, password = LOREM, authorities = {ROLE_ASSISTANT})
    @ValueSource(strings = {
            "",
            "time=11:11",
            "{}",
            "{ 11:11 }",
            "{\"wrong\":\"11:11\"}",
            "{\"time\":\"11 11\"}",
//            "{\"time\":\"11:11\", \"wrong\":\"11:11\"}", OK
            "{\"time\":\"11:11\" \"wrong\":\"11:11\"}",
            "{\"time\":\"00:60\"",
            "{\"time\":\"24:00\"",
            "{\"time\":\"24:60\""
    })
    public void wrongFormat(final String content) throws Exception {
        this.mockMvc.perform(post(URL_ADRESS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        ).andExpect(ResultMatcher.matchAll(status().is4xxClientError()));
    }

    @Test
    public void validTimeUnauthenticated() throws Exception {
        this.mockMvc.perform(post(URL_ADRESS)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"time\":\"13:13\"}")
                .with(csrf())
        ).andExpect(ResultMatcher.matchAll(status().is(401)));
    }

    @Test
    public void timeAlreadyExistsUnauthenticated() throws Exception {
        this.mockMvc.perform(post(URL_ADRESS)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"time\":\"11:11\"}")
                .with(csrf())
        ).andExpect(ResultMatcher.matchAll(status().is(401)));
    }

    @Test
    public void noContentUnauthenticated() throws Exception {
        this.mockMvc.perform(post(URL_ADRESS)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
        ).andExpect(ResultMatcher.matchAll(status().is(401)));
    }

    @Test
    public void unacceptedMediaTypeUnauthenticated() throws Exception {
        this.mockMvc.perform(post(URL_ADRESS)
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
            "{ 11:11 }",
            "{\"wrong\":\"11:11\"}",
            "{\"time\":\"11 11\"}",
            "{\"time\":\"11:11\", \"wrong\":\"11:11\"}",
            "{\"time\":\"11:11\" \"wrong\":\"11:11\"}",
            "{\"time\":\"00:60\"",
            "{\"time\":\"24:00\"",
            "{\"time\":\"24:60\""
    })
    public void wrongFormatUnauthenticated(final String content) throws Exception {
        this.mockMvc.perform(post(URL_ADRESS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .with(csrf())
        ).andExpect(ResultMatcher.matchAll(status().is(401)));
    }
}