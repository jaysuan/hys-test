package com.suan.client;

import com.suan.client.host.PublicIpResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ServerReportController {
    public static final Logger LOGGER = LoggerFactory.getLogger(ServerReportController.class);
    private final ServerApiClient serverApiClient;
    private final PublicIpResolver publicIpResolver;

    public ServerReportController(ServerApiClient serverApiClient,
                                  PublicIpResolver publicIpResolver) {
        this.serverApiClient = serverApiClient;
        this.publicIpResolver = publicIpResolver;
    }

    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal OidcUser principal) {
        if (principal != null) {
            model.addAttribute("profile", principal.getClaims());
        }
        return "index";
    }

    @GetMapping("/report")
    public String sendClientDetails(Model model) {
        var metadata = new ClientMetadata(publicIpResolver.getPublicIp());
        var response = serverApiClient.sendClientMetadata(metadata);
        model.addAttribute("publicIp", metadata.getIp());
        model.addAttribute("response", response);
        return "report";
    }

    @GetMapping(value = "/healthcheck", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String serverHealthCheck() {
        return serverApiClient.healthCheck();
    }

}
