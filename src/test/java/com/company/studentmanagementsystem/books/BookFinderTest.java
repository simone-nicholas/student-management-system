package com.company.studentmanagementsystem.books;

import com.company.studentmanagementsystem.books.model.Book;
import com.company.studentmanagementsystem.exceptions.BookNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookFinderTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookFinder bookFinder;

    @Test
    void getBookById_returnsBook_whenExists() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Cyber Security");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookFinder.getBookById(1L);

        assertEquals("Cyber Security", result.getTitle());
    }

    @Test
    void getBookById_throwsException_whenNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookFinder.getBookById(99L));
    }
}
