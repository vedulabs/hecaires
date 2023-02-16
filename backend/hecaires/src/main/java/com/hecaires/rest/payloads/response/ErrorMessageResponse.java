package com.hecaires.rest.payloads.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ErrorMessageResponse {
    private int statusCode;
    private Date timestamp;
    private String message;
    private String description;

    public ErrorMessageResponse(int statusCode, Date timestamp, String message, String description) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
        this.description = description;
    }
}