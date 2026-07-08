package com.company.studentmanagementsystem.exceptions;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(Long courseId) {
        super("Course with id " + courseId + " not found");
    }
}
