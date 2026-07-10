package com.company.studentmanagementsystem.books.service;

import com.company.studentmanagementsystem.books.BookRepository;
import com.company.studentmanagementsystem.books.model.Book;
import com.company.studentmanagementsystem.exceptions.BookNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookDeleteService {

    private final BookRepository bookRepository;

    public BookDeleteService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = getBookById(id);

        if (book.getStudent() != null) {
            book.getStudent().getBooks().remove(book);
            book.setStudent(null);
        }

        bookRepository.delete(book);
    }
}
