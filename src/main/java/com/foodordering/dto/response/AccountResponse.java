package com.foodordering.dto.response;

import java.time.Instant;
import java.util.List;

public class AccountResponse {
    
    private String id;
    private String username;
    private String email;
    private List<String> roles;
    private Boolean active;
    private Instant createdAt;
    private Instant modifiedAt;

    public AccountResponse() {
    }

    public AccountResponse(String id, String username, String email, List<String> roles, Boolean active, Instant createdAt, Instant modifiedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.active = active;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Instant modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}

