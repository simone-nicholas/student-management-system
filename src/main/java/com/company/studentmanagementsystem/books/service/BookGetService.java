package com.company.studentmanagementsystem.books.service;

import com.company.studentmanagementsystem.books.BookFinder;
import com.company.studentmanagementsystem.books.BookRepository;
import com.company.studentmanagementsystem.books.model.Book;
import com.company.studentmanagementsystem.exceptions.BookHasNoOwnerException;
import com.company.studentmanagementsystem.students.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookGetService {

    private final BookRepository bookRepository;
    private final BookFinder bookFinder;

    public BookGetService(
            BookRepository bookRepository,
            BookFinder bookFinder
    ) {
        this.bookRepository = bookRepository;
        this.bookFinder = bookFinder;
    }

    @Transactional(readOnly = true)
    public Page<Book> getAllBooks(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return bookRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Book getBookById(Long id) {
        return bookFinder.getBookById(id);
    }

    @Transactional(readOnly = true)
    public Student getBookOwner(Long bookId){
        Book book = getBookById(bookId);

        if(book.getStudent() == null) throw new BookHasNoOwnerException(bookId);

        return book.getStudent();
    }
}