package com.hecaires.hecaires;

import com.hecaires.rest.models.ERole;
import com.hecaires.rest.models.Role;
import com.hecaires.rest.models.User;
import com.hecaires.rest.payloads.request.SigninRequest;
import com.hecaires.rest.payloads.request.SignupRequest;
import com.hecaires.rest.payloads.response.JwtResponse;
import com.hecaires.rest.payloads.response.MessageResponse;
import com.hecaires.rest.repositories.RoleRepository;
import com.hecaires.rest.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTests {
    // inject the runtime port, it requires the webEnvironment
    @LocalServerPort
    private int port;

    // we use TestRestTemplate, it's an alternative to RestTemplate specific for tests
    // to use this template a webEnvironment is mandatory
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    User testUser;

    @BeforeEach
    public void prepare() {
        Role ROLE_USER = roleRepository.findByName(ERole.ROLE_USER).get();
        Set<Role> roles = new HashSet<>(Arrays.asList(ROLE_USER));

        testUser = new User();
        testUser.setUsername("TEST_USER");
        testUser.setPassword(passwordEncoder.encode("TEST_USER_PASSWORD"));
        testUser.setRoles(roles);
        testUser.setEmail("EMAIL@EMAIL.TEST");
        userRepository.save(testUser);
    }

    @AfterEach
    public void clear() {
        userRepository.delete(testUser);
    }

    @Test
    public void signInEndpointTest() throws MalformedURLException {
        SigninRequest signinRequest = new SigninRequest();
        signinRequest.setUsername("TEST_USER");
        signinRequest.setPassword("TEST_USER_PASSWORD");
        ResponseEntity<JwtResponse> response = restTemplate
                .postForEntity(
                        new URL("http://localhost:" + port + "/api/auth/signin").toString(),
                        signinRequest,
                        JwtResponse.class
                );
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void signInEndpointTest_badCredentials() throws MalformedURLException {
        SigninRequest signinRequest = new SigninRequest();
        signinRequest.setUsername("wrong_TEST_USER");
        signinRequest.setPassword("wrong_TEST_USER_PASSWORD");
        ResponseEntity<JwtResponse> response = restTemplate
                .postForEntity(
                        new URL("http://localhost:" + port + "/api/auth/signin").toString(),
                        signinRequest,
                        JwtResponse.class
                );
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void signUpEndpointTest_duplicateEmail() throws MalformedURLException {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("TEST_USER_1");
        signupRequest.setPassword("TEST_USER_1_PASSWORD");
        signupRequest.setEmail("EMAIL@EMAIL.TEST");
        ResponseEntity<MessageResponse> response = restTemplate
                .postForEntity(
                        new URL("http://localhost:" + port + "/api/auth/signup").toString(),
                        signupRequest,
                        MessageResponse.class
                );
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}