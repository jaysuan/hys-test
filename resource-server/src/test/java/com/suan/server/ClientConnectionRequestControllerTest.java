package com.suan.server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientConnectionRequestController.class)
public class ClientConnectionRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Call /api/healthcheck as authenticated user")
    void shouldReturnOk_healthCheck() throws Exception {
        mockMvc.perform(
                get("/api/healthcheck").with(jwt())
            )
            .andExpect(content().string("Still up!"));
    }

    @Test
    @DisplayName("Call /api/healthcheck as anonymous user")
    void shouldReturnUnauthorized_healthCheck() throws Exception {
        mockMvc.perform(get("/api/healthcheck"))
            .andExpect(status().isUnauthorized());
    }
}
