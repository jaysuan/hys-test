package com.suan.client;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
public class ServerApiClientTest {

    private MockWebServer mockServer;
    private ServerApiClient serverApiClient;

    @Test
    @DisplayName("Successful /api/healthcheck call")
    void testHealthCheck() {
        var mockResponse = new MockResponse()
            .addHeader("Content-Type", "text/plain")
            .setBody("Still up!");
        mockServer.enqueue(mockResponse);

        var response = serverApiClient.healthCheck();
        assertThat(response).isEqualTo("Still up!");
    }

}
