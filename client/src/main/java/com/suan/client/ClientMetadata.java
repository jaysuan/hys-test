package com.suan.client;

public class ClientMetadata {
    private String ip;

    public ClientMetadata() {}

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
