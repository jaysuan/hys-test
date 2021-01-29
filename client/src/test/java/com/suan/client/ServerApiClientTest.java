package com.suan.client;

import com.suan.client.apiclient.ServerApiClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
    "RESOURCE_URI=http://localhost:8090/api",
})
public class ServerApiClientTest {

    @Autowired
    private ServerApiClient serverApiClient;

    private static MockWebServer mockServer;

    @BeforeAll
    static void init() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start(8090);
    }

    @AfterAll
    static void shutdown() throws IOException {
        mockServer.shutdown();
    }

    @Test
    @DisplayName("Successful /api/healthcheck call")
    void shouldReturnServerHealthcheckResponse() {
        var mockResponse = new MockResponse()
            .addHeader("Content-Type", "text/plain")
            .setBody("Still up!");
        mockServer.enqueue(mockResponse);

        var response = serverApiClient.healthCheck();
        assertThat(response).isEqualTo("Still up!");
    }


    @TestConfiguration
    static class TestWebClientConfig {

        @Bean
        WebClient webClient(@Value("${resource-uri}") String resourceUri) {
            return WebClient.create(resourceUri);
        }

        @Bean
        ServerApiClient apiClient(WebClient webClient) {
            return new ServerApiClient(webClient);
        }

    }
}
