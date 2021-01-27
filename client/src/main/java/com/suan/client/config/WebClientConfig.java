package com.suan.client.config;

import com.suan.client.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {
    private final String resourceUri;

    public WebClientConfig(@Value("${resource-uri}") String resourceUri) {
        this.resourceUri = resourceUri;
    }

    @Bean
    WebClient webClient(OAuth2AuthorizedClientManager authorizedClientManager) {
        var logger = new WebClientLogger();
        var oauth2Client = new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        return WebClient.builder()
            .baseUrl(resourceUri)
            .filter(logger.logResponse())
            .apply(oauth2Client.oauth2Configuration())
            .build();
    }

    @Bean
    OAuth2AuthorizedClientManager authorizedClientManager(ClientRegistrationRepository clientRegistrationRepository,
                                                          OAuth2AuthorizedClientRepository authorizedClientRepository) {
        var authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
            .authorizationCode()
            .refreshToken()
            .build();
        var authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository,
            authorizedClientRepository);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }

    private static class WebClientLogger {
        private static final Logger LOGGER = LoggerFactory.getLogger(WebClientLogger.class);

        ExchangeFilterFunction logResponse() {
            return ExchangeFilterFunction.ofResponseProcessor(response -> {
                response
                    .bodyToMono(ServerResponse.class)
                    .log()
                    .doOnNext(serverResponse -> LOGGER.info("Response body: {}", serverResponse));

                return Mono.just(response);
            });
        }
    }
}
