package com.company.studentmanagementsystem.books;

import com.company.studentmanagementsystem.books.dto.BookRequestDTO;
import com.company.studentmanagementsystem.books.model.Book;
import com.company.studentmanagementsystem.books.service.BookDeleteService;
import com.company.studentmanagementsystem.books.service.BookGetService;
import com.company.studentmanagementsystem.books.service.BookPostService;
import com.company.studentmanagementsystem.exceptions.BookHasNoOwnerException;
import com.company.studentmanagementsystem.exceptions.BookNotFoundException;
import com.company.studentmanagementsystem.students.model.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookPostService bookPostService;

    @MockitoBean
    private BookGetService bookGetService;

    @MockitoBean
    private BookDeleteService bookDeleteService;

    @Test
    void getAllBooks_returnsListOfBooks() throws Exception {
        Book cyberSecurity = new Book("Cyber Security", "Autore", "1234567890123", BigDecimal.TEN, LocalDate.of(2020, 1, 1));
        cyberSecurity.setId(1L);

        when(bookGetService.getAllBooks()).thenReturn(List.of(cyberSecurity));

        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Cyber Security"));
    }

    @Test
    void getBook_returnsBook_whenExists() throws Exception {
        Book book = new Book("Cyber Security", "Autore", "1234567890123", BigDecimal.TEN, LocalDate.of(2020, 1, 1));
        book.setId(1L);

        when(bookGetService.getBookById(1L)).thenReturn(book);

        mockMvc.perform(get("/api/v1/books/{bookId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Cyber Security"));
    }

    @Test
    void getBook_returns404_whenNotFound() throws Exception {
        when(bookGetService.getBookById(99L)).thenThrow(new BookNotFoundException(99L));

        mockMvc.perform(get("/api/v1/books/{bookId}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStudentByBookId_returnsOwner_whenBookHasOwner() throws Exception {
        Student mario = new Student("Mario", "Rossi", "mario.rossi@email.com", "1234567890");
        mario.setId(1L);

        when(bookGetService.getBookOwner(10L)).thenReturn(mario);

        mockMvc.perform(get("/api/v1/books/{bookId}/owner", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mario"));
    }

    @Test
    void getStudentByBookId_returns409_whenBookHasNoOwner() throws Exception {
        when(bookGetService.getBookOwner(10L)).thenThrow(new BookHasNoOwnerException(10L));

        mockMvc.perform(get("/api/v1/books/{bookId}/owner", 10L))
                .andExpect(status().isConflict());
    }

    @Test
    void createBook_returns201WithLocation_whenValid() throws Exception {
        BookRequestDTO request = new BookRequestDTO(
                "Cyber Security", "Autore", "1234567890123", BigDecimal.TEN, LocalDate.of(2020, 1, 1)
        );

        Book created = new Book("Cyber Security", "Autore", "1234567890123", BigDecimal.TEN, LocalDate.of(2020, 1, 1));
        created.setId(1L);

        when(bookPostService.addBook(any(Book.class))).thenReturn(created);

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/api/v1/books/1")))
                .andExpect(jsonPath("$.title").value("Cyber Security"));
    }

    @Test
    void createBook_returns400_whenTitleIsBlank() throws Exception {
        BookRequestDTO request = new BookRequestDTO(
                "", "Autore", "1234567890123", BigDecimal.TEN, LocalDate.of(2020, 1, 1)
        );

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteBook_returns204() throws Exception {
        mockMvc.perform(delete("/api/v1/books/{bookId}", 1L))
                .andExpect(status().isNoContent());
    }
}
