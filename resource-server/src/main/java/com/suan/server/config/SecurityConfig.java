package com.suan.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final String jwkSetUri;
    private final String audience;

    public SecurityConfig(@Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") String jwkSetUri,
                          @Value("${auth0.audience}") String audience) {
        this.jwkSetUri = jwkSetUri;
        this.audience = audience;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests ->
                authorizeRequests.anyRequest().authenticated())
            .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }

    @Bean
    JwtDecoder jwtDecoder() {
        var jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
        var audienceValidator = new AudienceValidator(audience);
        jwtDecoder.setJwtValidator(audienceValidator);

        return jwtDecoder;
    }
}
