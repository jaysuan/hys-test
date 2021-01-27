package com.suan.client;

public class ServerResponse {
    private String id;
    private String message;
    private ClientMetadata clientMetadata;

    public ServerResponse() {}

    public ServerResponse(String id, String message, ClientMetadata clientMetadata) {
        this.id = id;
        this.message = message;
        this.clientMetadata = clientMetadata;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public ClientMetadata getClientMetadata() {
        return clientMetadata;
    }

    @Override
    public String toString() {
        return "ServerResponse{" +
            "id='" + id + '\'' +
            ", message='" + message + '\'' +
            ", clientMetadata=" + clientMetadata +
            '}';
    }
}
