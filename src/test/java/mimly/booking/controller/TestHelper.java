package mimly.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class TestHelper {

    private final MockMvc mockMvc;

    @Autowired
    public TestHelper(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    public void testGetWithContentAndExpect(String url, String content, int status) throws Exception {
        this.mockMvc
                .perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(ResultMatcher.matchAll(status().is(status)));
    }

    public void testPostWithContentAndExpect(String url, String content, int status) throws Exception {
        this.mockMvc
                .perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(ResultMatcher.matchAll(status().is(status)));
    }

    public void testDeleteWithContentAndExpect(String url, String content, int status) throws Exception {
        this.mockMvc
                .perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(ResultMatcher.matchAll(status().is(status)));
    }
}
