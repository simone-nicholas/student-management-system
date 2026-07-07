package com.company.studentmanagementsystem.exceptions;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(Long studentId) {
        super("Course not found at student with id " + studentId);
    }
}
