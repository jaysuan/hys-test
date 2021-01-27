package com.suan.client.host;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AwsPublicIpResolver implements PublicIpResolver {
    private WebClient webClient;

    public AwsPublicIpResolver(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://checkip.amazonaws.com").build();
    }

    @Override
    public String getPublicIp() {
        return webClient.get()
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

}
