package com.company.studentmanagementsystem.books;

import com.company.studentmanagementsystem.exceptions.BookNotAssignedException;
import com.company.studentmanagementsystem.exceptions.BookNotFoundException;
import com.company.studentmanagementsystem.exceptions.StudentNotFoundException;
import com.company.studentmanagementsystem.students.Student;
import com.company.studentmanagementsystem.students.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

    @Transactional(readOnly = true)
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Book> getStudentBooks(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));

        return student.getBooks();
    }

    @Transactional
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    @Transactional
    public Book assignBookToStudent(Long studentId, Long bookId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
        Book book = getBookById(bookId);

        Student previousStudent = book.getStudent();

        if (student.equals(book.getStudent())) {
            return book;
        }

        if (previousStudent != null) {
            previousStudent.getBooks().remove(book);
        }

        book.setStudent(student);

        if (!student.getBooks().contains(book)) {
            student.getBooks().add(book);
        }

        return bookRepository.save(book);
    }

    @Transactional
    public void removeBookFromStudent(Long studentId, Long bookId) {
        Book book = getBookById(bookId);

        if (book.getStudent() == null ||
                !book.getStudent().getId().equals(studentId)) {
            throw new BookNotAssignedException(bookId, studentId);
        }

        book.getStudent().getBooks().remove(book);
        book.setStudent(null);
        bookRepository.save(book);
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