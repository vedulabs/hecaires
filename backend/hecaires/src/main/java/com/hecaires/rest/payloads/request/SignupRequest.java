package com.hecaires.rest.payloads.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 256)
    private String username;

    @NotBlank
    @Size(max = 256)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 256)
    private String password;
}