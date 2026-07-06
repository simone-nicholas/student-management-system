package com.company.studentmanagementsystem.books;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService =  bookService;
    }

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @PostMapping("/books")
    public ResponseEntity<Book> addBook(@RequestBody @Valid Book book) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookService.addBook(book));
    }

    @PostMapping("/students/{studentId}/books/{bookId}")
    public ResponseEntity<Book> addBook(@PathVariable Long studentId, @PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.assignBookToStudent(studentId, bookId));
    }
}