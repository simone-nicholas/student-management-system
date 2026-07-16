package com.company.studentmanagementsystem.books.service;

import com.company.studentmanagementsystem.books.BookFinder;
import com.company.studentmanagementsystem.books.BookRepository;
import com.company.studentmanagementsystem.books.model.Book;
import com.company.studentmanagementsystem.exceptions.BookNotFoundException;
import com.company.studentmanagementsystem.students.model.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookDeleteServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookFinder bookFinder;

    @InjectMocks
    private BookDeleteService bookDeleteService;

    @Test
    void deleteBook_removesAssociationAndDeletesBook_whenBookHasOwner() {
        Student mario = new Student();
        mario.setId(1L);

        Book book = new Book();
        book.setId(10L);
        book.setStudent(mario);
        mario.setBooks(new ArrayList<>(List.of(book)));

        when(bookFinder.getBookById(10L)).thenReturn(book);

        bookDeleteService.deleteBook(10L);

        assertNull(book.getStudent());
        assertFalse(mario.getBooks().contains(book));
        verify(bookRepository).delete(book);
    }

    @Test
    void deleteBook_deletesBook_whenBookHasNoOwner() {
        Book book = new Book();
        book.setId(10L);
        book.setStudent(null);

        when(bookFinder.getBookById(10L)).thenReturn(book);

        bookDeleteService.deleteBook(10L);

        verify(bookRepository).delete(book);
    }

    @Test
    void deleteBook_throwsException_whenBookNotFound() {
        when(bookFinder.getBookById(99L)).thenThrow(new BookNotFoundException(99L));

        assertThrows(BookNotFoundException.class, () -> bookDeleteService.deleteBook(99L));
    }
}
