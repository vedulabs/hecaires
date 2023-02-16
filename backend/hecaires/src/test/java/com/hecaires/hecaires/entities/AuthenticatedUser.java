package com.hecaires.hecaires.entities;

import com.hecaires.rest.models.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthenticatedUser {
    User user;
    String bearerToken;
}