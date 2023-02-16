package com.hecaires.rest.payloads.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddBulkDocumentRequest {
    @NotBlank
    String payload;
}