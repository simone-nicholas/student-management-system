package com.company.studentmanagementsystem.exceptions;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long id) {
        super("Student with id " + id + " not found");
    }
}
