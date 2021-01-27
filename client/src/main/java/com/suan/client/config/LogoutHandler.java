package com.suan.client.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class LogoutHandler extends SecurityContextLogoutHandler {
    public static final Logger LOGGER = LoggerFactory.getLogger(LogoutHandler.class);
    public static final String AUTH0_LOGOUT_PATH = "v2/logout?client_id={clientId}&returnTo={redirectUrl}";
    private final ClientRegistrationRepository clientRegistrationRepository;

    public LogoutHandler(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        super.logout(request, response, authentication);

        var auth0ClientRegistration = clientRegistrationRepository.findByRegistrationId("auth0");
        var issuer = auth0ClientRegistration.getProviderDetails().getIssuerUri();
        var clientId = auth0ClientRegistration.getClientId();

        var redirectUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
            .build().toString();
        var logoutUrl = UriComponentsBuilder.fromHttpUrl(issuer + AUTH0_LOGOUT_PATH)
            .encode()
            .buildAndExpand(clientId, redirectUrl)
            .toUriString();

        try {
            response.sendRedirect(logoutUrl);
        } catch (IOException ex) {
            LOGGER.info("Error in redirecting to Auth0's logout URL", ex);
        }
    }
}
