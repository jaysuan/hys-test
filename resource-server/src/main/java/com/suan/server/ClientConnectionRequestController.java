package com.suan.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ClientConnectionRequestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientConnectionRequestController.class);

    @PostMapping("/report")
    public ClientConnectedResponse clientReport(@RequestBody ClientMetadata metadata,
                                                HttpServletRequest request) {
        LOGGER.info("Request from {}", request.getRemoteHost());
        LOGGER.info("New client connected: {}", metadata);
        return new ClientConnectedResponse(UUID.randomUUID(), "Client connected", metadata);
    }

    @GetMapping("/healthcheck")
    public String healthCheck() {
        return "Still up!";
    }

}
