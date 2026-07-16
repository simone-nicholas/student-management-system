package com.company.studentmanagementsystem.books.service;

import com.company.studentmanagementsystem.books.BookRepository;
import com.company.studentmanagementsystem.books.model.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookPostServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookPostService bookPostService;

    @Test
    void addBook_savesAndReturnsBook() {
        Book book = new Book();
        book.setTitle("Cyber Security");

        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookPostService.addBook(book);

        assertEquals("Cyber Security", result.getTitle());
        verify(bookRepository).save(book);
    }
}
