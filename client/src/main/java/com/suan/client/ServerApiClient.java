package com.suan.client;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

@Service
public class ServerApiClient {
    private final WebClient webClient;

    public ServerApiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public ServerResponse sendClientMetadata(ClientMetadata metadata) {
        return webClient.post()
            .uri("/report")
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(metadata)
            .attributes(clientRegistrationId("auth0"))
            .retrieve()
            .bodyToMono(ServerResponse.class)
            .block();
    }

    public String healthCheck() {
        return webClient.get()
            .uri("/healthcheck")
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

}
