package com.hecaires.rest.payloads.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddDeviceRequest {
    @NotBlank
    @Size(min = 32, max = 32)
    String id;
}