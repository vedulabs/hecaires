package com.hecaires.rest.controllers;

import com.hecaires.rest.exceptions.DuplicateResourceException;
import com.hecaires.rest.models.Device;
import com.hecaires.rest.services.DeviceService;
import com.hecaires.rest.payloads.request.AddDeviceRequest;
import com.hecaires.rest.payloads.response.ErrorMessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/device")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Device Setup Endpoint", description = "com.hecaires.rest.controllers.DeviceController")
public class DeviceController {
    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    DeviceService deviceService;

    @GetMapping(
            value = "/list",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Returns a list of all devices associated with the current authenticated user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of all devices associated with the current authenticated user",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Device.class))
                            )
                    }
            )
    })
    public ResponseEntity<?> getAllDevices() {
        return ResponseEntity.ok(deviceService.getAllDevicesForAuthenticatedUser());
    }

    @PostMapping(
            value = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Adds a device to a list of devices associated with the current authenticated user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns device that was added to the registry",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Device.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Device with provided signature already exists in the registry",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = ErrorMessageResponse.class))
                            )
                    }
            )
    })
    public ResponseEntity<?> addDevice(@Valid @RequestBody AddDeviceRequest addDeviceRequest) {
        try {
            Device newDevice = deviceService.addDeviceForAuthenticatedUser(addDeviceRequest);
            return ResponseEntity.ok(newDevice);
        } catch (DuplicateResourceException ex) {
            ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse(
                    HttpStatus.CONFLICT.value(),
                    new Date(),
                    ex.getMessage(),
                    "The device must have a unique signature"
            );
            return new ResponseEntity<ErrorMessageResponse>(errorMessageResponse, HttpStatus.CONFLICT);
        }
    }

    @GetMapping(
            value = "/uuid",
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Returns a unique ID (UUID) that could be used to identify a device")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Unique ID that could be used to identify a device",
                    content = {
                            @Content(
                                    mediaType = MediaType.TEXT_PLAIN_VALUE,
                                    schema = @Schema(implementation = String.class)
                            )
                    }
            )
    })
    public ResponseEntity<String> generateUniqueDeviceId() {
        return ResponseEntity.ok(deviceService.generateUniqueDeviceId());
    }
}