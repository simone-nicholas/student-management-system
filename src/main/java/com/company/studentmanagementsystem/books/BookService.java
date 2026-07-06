package com.company.studentmanagementsystem.books;

import com.company.studentmanagementsystem.exceptions.BookNotFoundException;
import com.company.studentmanagementsystem.exceptions.StudentNotFoundException;
import com.company.studentmanagementsystem.students.Student;
import com.company.studentmanagementsystem.students.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

// Assign a book to a student POST /students/{studentId}/books/{bookId}
@Service
public class BookService {
    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;

    public BookService(BookRepository bookRepository, StudentRepository studentRepository) {
        this.bookRepository = bookRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Transactional
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    @Transactional
    public Book assignBookToStudent(Long studentId, Long bookId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
        Book book = bookRepository.findById(bookId).
                orElseThrow(() -> new BookNotFoundException(bookId));

        book.setStudent(student);
        return bookRepository.save(book);
    }
}