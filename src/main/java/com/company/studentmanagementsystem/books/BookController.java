package com.company.studentmanagementsystem.books;

import com.company.studentmanagementsystem.books.dto.BookRequestDTO;
import com.company.studentmanagementsystem.books.dto.BookResponseDTO;
import com.company.studentmanagementsystem.books.mapper.BookMapper;
import com.company.studentmanagementsystem.books.model.Book;
import com.company.studentmanagementsystem.books.service.BookDeleteService;
import com.company.studentmanagementsystem.books.service.BookGetService;
import com.company.studentmanagementsystem.books.service.BookPostService;
import com.company.studentmanagementsystem.students.model.Student;
import com.company.studentmanagementsystem.students.dto.StudentResponseDTO;
import com.company.studentmanagementsystem.students.mapper.StudentMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookPostService bookPostService;
    private final BookGetService bookGetService;
    private final BookDeleteService bookDeleteService;

    public BookController(
            BookPostService bookPostService,
            BookGetService bookGetService,
            BookDeleteService bookDeleteService
    ) {
        this.bookPostService = bookPostService;
        this.bookGetService = bookGetService;
        this.bookDeleteService = bookDeleteService;
    }

    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> getAllBooks() {

        List<BookResponseDTO> books = bookGetService.getAllBooks()
                .stream()
                .map(BookMapper::toDTO)
                .toList();

        return ResponseEntity.ok(books);
    }

    @GetMapping("/{bookId}/owner")
    public ResponseEntity<StudentResponseDTO> getStudentByBookId(@PathVariable("bookId") Long bookId) {
        Student student = bookGetService.getBookOwner(bookId);

        return ResponseEntity.ok(StudentMapper.toDTO(student));
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookResponseDTO> getBook(
            @PathVariable Long bookId
    ) {

        Book book = bookGetService.getBookById(bookId);

        return ResponseEntity.ok(
                BookMapper.toDTO(book)
        );
    }


    @PostMapping
    public ResponseEntity<BookResponseDTO> createBook(
            @Valid @RequestBody BookRequestDTO request
    ) {

        Book book = BookMapper.toEntity(request);

        Book created = bookPostService.addBook(book);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{bookId}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(BookMapper.toDTO(created));
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBook(
            @PathVariable Long bookId
    ) {
        bookDeleteService.deleteBook(bookId);

        return ResponseEntity.noContent().build();
    }
}