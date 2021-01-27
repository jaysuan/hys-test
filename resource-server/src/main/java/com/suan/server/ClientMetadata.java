package com.suan.server;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ClientMetadata {
    private final String ip;

    @JsonCreator
    public ClientMetadata(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    @Override
    public String toString() {
        return "ClientMetadata{" +
            "ip='" + ip + '\'' +
            '}';
    }
}
