package com.hecaires.rest.payloads.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SigninRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}