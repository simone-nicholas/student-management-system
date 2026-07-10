package com.company.studentmanagementsystem.exceptions;

import java.time.Instant;

public record ErrorResponseDTO(
        int status,
        String error,
        String message,
        String path,
        Instant timestamp
) {}