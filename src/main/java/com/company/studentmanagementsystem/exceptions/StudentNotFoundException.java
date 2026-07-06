package com.company.studentmanagementsystem.exceptions;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(Long id) {
        super("Book with id " + id + " not found");
    }
}
