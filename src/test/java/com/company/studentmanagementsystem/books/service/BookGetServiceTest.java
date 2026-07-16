package com.company.studentmanagementsystem.books.service;

import com.company.studentmanagementsystem.books.BookFinder;
import com.company.studentmanagementsystem.books.BookRepository;
import com.company.studentmanagementsystem.books.model.Book;
import com.company.studentmanagementsystem.exceptions.BookHasNoOwnerException;
import com.company.studentmanagementsystem.exceptions.BookNotFoundException;
import com.company.studentmanagementsystem.students.model.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookGetServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookFinder bookFinder;

    @InjectMocks
    private BookGetService bookGetService;

    @Test
    void getAllBooks_returnsAllBooks() {
        Book cyberSecurity = new Book();
        cyberSecurity.setId(1L);
        cyberSecurity.setTitle("Cyber Security");

        Book networking = new Book();
        networking.setId(2L);
        networking.setTitle("Networking");

        when(bookRepository.findAll()).thenReturn(List.of(cyberSecurity, networking));

        List<Book> result = bookGetService.getAllBooks();

        assertEquals(2, result.size());
        assertEquals("Cyber Security", result.get(0).getTitle());
        assertEquals("Networking", result.get(1).getTitle());
    }

    @Test
    void getAllBooks_returnsEmptyList_whenNoBooksExist() {
        when(bookRepository.findAll()).thenReturn(List.of());

        List<Book> result = bookGetService.getAllBooks();

        assertEquals(0, result.size());
    }

    @Test
    void getBookById_returnsBook_whenExists() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Cyber Security");

        when(bookFinder.getBookById(1L)).thenReturn(book);

        Book result = bookGetService.getBookById(1L);

        assertEquals("Cyber Security", result.getTitle());
    }

    @Test
    void getBookById_throwsException_whenNotFound() {
        when(bookFinder.getBookById(99L)).thenThrow(new BookNotFoundException(99L));

        assertThrows(BookNotFoundException.class, () -> bookGetService.getBookById(99L));
    }

    @Test
    void getBookOwner_returnsStudent_whenBookHasOwner() {
        Student mario = new Student();
        mario.setId(1L);
        mario.setName("Mario");

        Book book = new Book();
        book.setId(10L);
        book.setStudent(mario);

        when(bookFinder.getBookById(10L)).thenReturn(book);

        Student result = bookGetService.getBookOwner(10L);

        assertEquals("Mario", result.getName());
    }

    @Test
    void getBookOwner_throwsException_whenBookHasNoOwner() {
        Book book = new Book();
        book.setId(10L);
        book.setStudent(null);

        when(bookFinder.getBookById(10L)).thenReturn(book);

        assertThrows(BookHasNoOwnerException.class, () -> bookGetService.getBookOwner(10L));
    }

    @Test
    void getBookOwner_throwsException_whenBookNotFound() {
        when(bookFinder.getBookById(99L)).thenThrow(new BookNotFoundException(99L));

        assertThrows(BookNotFoundException.class, () -> bookGetService.getBookOwner(99L));
    }
}
