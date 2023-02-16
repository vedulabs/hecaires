package com.hecaires.rest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Test Endpoint", description = "com.hecaires.rest.controllers.TestController")
public class TestController {
    @GetMapping("/all")
    @Operation(summary = "Public access test controller")
    public String allAccess() {
        return "Public Access";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MAINTAINER') or hasRole('ADMIN')")
    @Operation(summary = "hasRole('USER') or hasRole('MAINTAINER') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Access";
    }

    @GetMapping("/maintainer")
    @PreAuthorize("hasRole('MAINTAINER')")
    @Operation(summary = "hasRole('MAINTAINER')")
    public String maintainerAccess() {
        return "Maintainer Access";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Access";
    }
}