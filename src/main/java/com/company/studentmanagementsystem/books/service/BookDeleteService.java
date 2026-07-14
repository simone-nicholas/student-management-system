package com.company.studentmanagementsystem.books.service;

import com.company.studentmanagementsystem.books.BookFinder;
import com.company.studentmanagementsystem.books.BookRepository;
import com.company.studentmanagementsystem.books.model.Book;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookDeleteService {

    private final BookRepository bookRepository;
    private final BookFinder bookFinder;

    public BookDeleteService(
            BookRepository bookRepository,
            BookFinder bookFinder
    ) {
        this.bookRepository = bookRepository;
        this.bookFinder = bookFinder;
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = bookFinder.getBookById(id);

        if (book.getStudent() != null) {
            book.getStudent().getBooks().remove(book);
            book.setStudent(null);
        }

        bookRepository.delete(book);
    }
}
