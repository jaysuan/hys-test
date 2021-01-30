package com.suan.server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientConnectionRequestController.class)
@TestPropertySource(properties = {
    "JWK_SET_URI=http://localhost:8090"
})
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

    @Test
    @DisplayName("Call /api/report as authenticated user")
    void shouldReturnOk_clientReport() throws Exception {
        mockMvc.perform(
                post("/api/report")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"ip\": \"165.23.100.3\" }")
                    .with(jwt())
            )
            .andDo(print())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.message").value("Client connected"))
            .andExpect(jsonPath("$.clientMetadata.ip").value("165.23.100.3"));
    }

    @Test
    @DisplayName("Call /api/report as anonymous user")
    void shouldReturnUnauthorized_clientReport() throws Exception {
        mockMvc.perform(post("/api/report"))
            .andExpect(status().isUnauthorized());
    }
}
