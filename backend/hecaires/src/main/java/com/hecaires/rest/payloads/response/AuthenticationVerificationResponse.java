package com.hecaires.rest.payloads.response;

import lombok.Data;

import java.util.List;

@Data
public class AuthenticationVerificationResponse {
    private String id;
    private String username;
    private String email;
    private List<String> roles;

    public AuthenticationVerificationResponse(String id, String username, String email, List<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}