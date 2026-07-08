package com.company.studentmanagementsystem.exceptions;

public class BookNotAssignedException extends RuntimeException {

    public BookNotAssignedException(Long bookId, Long studentId) {
        super("Book " + bookId +
                " is not assigned to student " + studentId);
    }
}