package com.company.studentmanagementsystem.books;

import com.company.studentmanagementsystem.exceptions.BookHasNoOwnerException;
import com.company.studentmanagementsystem.exceptions.BookNotAssignedException;
import com.company.studentmanagementsystem.exceptions.BookNotFoundException;
import com.company.studentmanagementsystem.exceptions.StudentNotFoundException;
import com.company.studentmanagementsystem.students.Student;
import com.company.studentmanagementsystem.students.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Student getBookOwner(Long bookId){
        Book book = getBookById(bookId);

        if(book.getStudent() == null) throw new BookHasNoOwnerException(bookId);

        return book.getStudent();
    }

    @Transactional
    public Book addBook(Book book) {
        return bookRepository.save(book);
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