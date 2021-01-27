package com.suan.server;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.UUID;

public class ClientConnectedResponse {
    private final UUID id;
    private final String message;
    private final ClientMetadata metadata;

    @JsonCreator
    public ClientConnectedResponse(UUID id, String message, ClientMetadata metadata) {
        this.id = id;
        this.message = message;
        this.metadata = metadata;
    }

    public UUID getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public ClientMetadata getClientMetadata() {
        return metadata;
    }

    @Override
    public String toString() {
        return "ClientDiscoveryResponse{" +
            "message='" + message + '\'' +
            ", metadata=" + metadata +
            '}';
    }
}
