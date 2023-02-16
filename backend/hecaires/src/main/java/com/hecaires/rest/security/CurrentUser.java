package com.hecaires.rest.security;

import com.hecaires.rest.models.ERole;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CurrentUser {
    private String id;

    private String username;

    private String email;

    private List<ERole> roles;
}