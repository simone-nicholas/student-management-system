package com.company.studentmanagementsystem.courses.dto;

public record CourseResponseDTO(
        Long id,
        String name,
        String description,
        String code,
        Integer credits
) {
}
