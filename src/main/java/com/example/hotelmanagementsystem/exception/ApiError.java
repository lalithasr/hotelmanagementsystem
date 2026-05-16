package com.example.hotelmanagementsystem.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiError {

    private String message;
    private int statusCode;
    private String errorCode;
    private LocalDateTime timestamp;
}

