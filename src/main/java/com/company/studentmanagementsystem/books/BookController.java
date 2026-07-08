package com.company.studentmanagementsystem.books;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/students/{studentId}/books")
    public ResponseEntity<List<Book>> getAllBooksByStudentId(@PathVariable("studentId") Long studentId) {
        return ResponseEntity.ok(bookService.getStudentBooks(studentId));
    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity<Book> getBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getBookById(bookId));
    }

    @PostMapping("/books")
    public ResponseEntity<Book> createBook(@RequestBody @Valid Book book) {
        Book created = bookService.addBook(book);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{bookId}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(created);
    }

    @PostMapping("/students/{studentId}/books/{bookId}")
    public ResponseEntity<Book> assignBookToStudent(@PathVariable Long studentId, @PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.assignBookToStudent(studentId, bookId));
    }

    @DeleteMapping("/students/{studentId}/books/{bookId}")
    public ResponseEntity<Void> removeBookFromStudent(
            @PathVariable Long studentId,
            @PathVariable Long bookId) {

        bookService.removeBookFromStudent(studentId, bookId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/books/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }
}