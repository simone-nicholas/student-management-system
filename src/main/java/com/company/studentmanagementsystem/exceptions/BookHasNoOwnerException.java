package com.company.studentmanagementsystem.exceptions;

public class BookHasNoOwnerException extends RuntimeException {

    public BookHasNoOwnerException(Long bookId) {
        super("Book with id " + bookId + " has no owner.");
    }
}
