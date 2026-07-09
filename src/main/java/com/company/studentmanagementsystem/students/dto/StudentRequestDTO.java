package com.company.studentmanagementsystem.students.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record StudentRequestDTO(
        @NotBlank
        String name,

        @NotBlank
        String surname,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Numero di telefono non valido")
        String phoneNumber
) {
}
