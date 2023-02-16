package com.hecaires.hecaires;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hecaires.bigdata.repositories.BulkDocumentRepository;
import com.hecaires.hecaires.entities.AuthenticatedUser;
import com.hecaires.rest.models.ERole;
import com.hecaires.rest.models.Role;
import com.hecaires.rest.models.User;
import com.hecaires.rest.payloads.request.AddBulkDocumentRequest;
import com.hecaires.rest.repositories.RoleRepository;
import com.hecaires.rest.repositories.UserRepository;
import com.hecaires.rest.security.jwt.JwtService;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.URL;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BulkThroughputTests {
    private static final Logger logger = LoggerFactory.getLogger(BulkThroughputTests.class);

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
    BulkDocumentRepository bulkDocumentRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    List<AuthenticatedUser> testUsers = new ArrayList<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final int num_of_test_users = 100;
    private final int num_of_bulk_messages_for_each_user = 1000;

    @BeforeEach
    public void prepare() {
        logger.info("Creating {} test users...", num_of_test_users);

        Role ROLE_USER = roleRepository.findByName(ERole.ROLE_USER).get();
        Set<Role> roles = new HashSet<>(Arrays.asList(ROLE_USER));

        String password = "TEST_USER_PASSWORD";
        String encoded_password = passwordEncoder.encode("TEST_USER_PASSWORD");

        for (int i = 1; i < num_of_test_users + 1; i++) {
            User testUser = new User();
            testUser.setUsername(String.format("TEST_USER_%d", i));
            testUser.setPassword(encoded_password);
            testUser.setRoles(roles);
            testUser.setEmail(String.format("USER_%d_EMAIL@EMAIL.TEST", i));
            userRepository.save(testUser);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            testUser.getUsername(),
                            password
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwtToken = jwtService.generateJwtToken(authentication);

            AuthenticatedUser authenticatedUser = new AuthenticatedUser();
            authenticatedUser.setUser(testUser);
            authenticatedUser.setBearerToken(jwtToken);

            testUsers.add(authenticatedUser);
        }
    }

    @AfterEach
    public void clear() {
        logger.info("Deleting {} test users...", num_of_test_users);
        testUsers.stream().forEach(testUser -> {
            userRepository.delete(testUser.getUser());
            bulkDocumentRepository.deleteByUser_id(testUser.getUser().getId());
        });
    }

    @Test
    public void bulkDataThroughputTest() throws InterruptedException {
        logger.info("Bulk documents throughput test with {} test users...", num_of_test_users);

        long start = System.currentTimeMillis();

        ExecutorService executorService = Executors.newFixedThreadPool(num_of_test_users);

        List<Runnable> runnableTasks = testUsers.stream().map(testUser -> createRunnableTask(testUser)).collect(Collectors.toList());

        List<Callable<Void>> callables = new ArrayList<>();
        for (Runnable r : runnableTasks) {
            callables.add(toCallable(r));
        }

        executorService.invokeAll(callables);

        long finish = System.currentTimeMillis();

        long timeElapsed = Math.round((finish - start) / 1000);

        logger.info("Elapsed time: {}s", timeElapsed);

        Assertions.assertTrue(timeElapsed < 60);
    }

    private Callable<Void> toCallable(final Runnable runnable) {
        return new Callable<Void>() {
            @Override
            public Void call() {
                runnable.run();
                return null;
            }
        };
    }

    private Runnable createRunnableTask(final AuthenticatedUser authenticatedUser){
        Runnable aRunnable = new Runnable(){
            public void run(){
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));
                headers.setBearerAuth(authenticatedUser.getBearerToken());

                for (int i = 1; i < num_of_bulk_messages_for_each_user + 1; i++) {
                    AddBulkDocumentRequest addBulkDocumentRequest = new AddBulkDocumentRequest();
                    RandomString gen = new RandomString(new Random().nextInt(128, 1024), ThreadLocalRandom.current());
                    String generatedPayload = gen.nextString();
                    addBulkDocumentRequest.setPayload(generatedPayload);

                    try {
                        String addBulkDocumentRequestJSON = objectMapper.writeValueAsString(addBulkDocumentRequest);

                        logger.info("Putting BulkDocument#{} {}", i, addBulkDocumentRequestJSON);

                        try {
                            String bulkDocumentEndpointURL = new URL("http://localhost:" + port + "/api/bigdata/bulk").toString();

                            ResponseEntity<?> response = restTemplate.exchange(
                                    bulkDocumentEndpointURL,
                                    HttpMethod.PUT,
                                    new HttpEntity<>(addBulkDocumentRequestJSON, headers),
                                    String.class
                            );
                            logger.info(response.toString());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        };

        return aRunnable;
    }
}