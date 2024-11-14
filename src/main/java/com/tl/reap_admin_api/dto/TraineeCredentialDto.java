package com.tl.reap_admin_api.dto;

import java.util.UUID;

public class TraineeCredentialDto {
    private UUID uuid;
    private String username;
    private String email;

    // Constructors, getters, and setters

    public TraineeCredentialDto() {
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}