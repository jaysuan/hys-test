package com.suan.client.apiclient;

import com.suan.client.ClientMetadata;
import com.suan.client.ServerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
            .onStatus(HttpStatus::is4xxClientError, response -> {
                var message = "Received 4xx response from server";
                switch (response.statusCode()) {
                    case UNAUTHORIZED:
                        message = "User not authenticated";
                        break;
                    case FORBIDDEN:
                        message = "User lacks privilege to call server API";
                        break;
                    default:
                        break;
                }
                return Mono.error(new ServerApiException(response.statusCode(), message));
            })
            .bodyToMono(ServerResponse.class)
            .onErrorStop()
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
