package com.company.studentmanagementsystem.students.dto;

public record StudentResponseDTO(
        Long id,
        String name,
        String surname,
        String email,
        String phoneNumber
) {
}
