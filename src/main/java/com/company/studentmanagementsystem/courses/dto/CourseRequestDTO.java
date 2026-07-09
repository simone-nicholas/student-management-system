package com.company.studentmanagementsystem.courses.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CourseRequestDTO(
        @NotBlank
        String name,

        @NotBlank
        String code,

        @NotBlank
        String description,

        @NotNull
        @Min(1)
        Integer credits
) {
}