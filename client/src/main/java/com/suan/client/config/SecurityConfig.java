package com.suan.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final SecurityContextLogoutHandler logoutHandler;
    private final String audience;

    public SecurityConfig(ClientRegistrationRepository clientRegistrationRepository,
                          SecurityContextLogoutHandler logoutHandler,
                          @Value("${auth0.audience}") String audience) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.logoutHandler = logoutHandler;
        this.audience = audience;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests ->
                authorizeRequests.mvcMatchers("/").permitAll()
                    .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 ->
                oauth2.authorizationEndpoint(authorization ->
                    authorization.authorizationRequestResolver(
                        authorizationRequestResolver(clientRegistrationRepository)
                    )
                )
            )
            .oauth2Client(withDefaults())
            .logout()
                .logoutUrl("/logout")
                .addLogoutHandler(logoutHandler);
    }

    private OAuth2AuthorizationRequestResolver authorizationRequestResolver(
            ClientRegistrationRepository clientRegistrationRepository) {
        var authorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(
            clientRegistrationRepository, "/oauth2/authorization");
        authorizationRequestResolver.setAuthorizationRequestCustomizer(customizer ->
            customizer.additionalParameters(params ->
                params.put("audience", audience)
            )
        );

        return authorizationRequestResolver;
    }

}
