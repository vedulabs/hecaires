package com.hecaires.rest.controllers.bigdata;

import com.hecaires.bigdata.documents.BulkDocument;
import com.hecaires.bigdata.services.BulkProcessingService;
import com.hecaires.rest.payloads.request.AddBulkDocumentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bigdata/bulk")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Bulk Document processing Endpoint", description = "com.hecaires.rest.controllers.bigdata.BulkProcessingController")
public class BulkProcessingController {
    private static final Logger logger = LoggerFactory.getLogger(BulkProcessingController.class);

    @Autowired
    BulkProcessingService bulkProcessingService;

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Persists Bulk bigdata Document into 'hecaires.bulk' Collection")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns the ID of the bulk document",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = String.class)
                            )
                    }
            )
    })
    public ResponseEntity<?> putBulkDocumentForCurrentAuthenticatedUser(@Valid @RequestBody AddBulkDocumentRequest addBulkDocumentRequest) {
        BulkDocument newBulkDocument = bulkProcessingService.putBulkDocumentForCurrentAuthenticatedUser(addBulkDocumentRequest);
        return ResponseEntity.ok(newBulkDocument.getId());
    }
}