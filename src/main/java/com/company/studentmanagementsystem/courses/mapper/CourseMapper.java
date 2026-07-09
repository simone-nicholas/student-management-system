package com.company.studentmanagementsystem.courses.mapper;

import com.company.studentmanagementsystem.courses.Course;
import com.company.studentmanagementsystem.courses.dto.CourseRequestDTO;
import com.company.studentmanagementsystem.courses.dto.CourseResponseDTO;

public class CourseMapper {

    public static Course toEntity(CourseRequestDTO dto) {
        Course course = new Course();
        course.setName(dto.name());
        course.setDescription(dto.description());
        course.setCode(dto.code());
        course.setCredits(dto.credits());

        return course;
    }

    public static CourseResponseDTO toDTO(Course course) {
        return new CourseResponseDTO(
                course.getId(),
                course.getName(),
                course.getDescription(),
                course.getCode(),
                course.getCredits()
        );
    }
}