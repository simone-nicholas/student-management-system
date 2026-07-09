package com.company.studentmanagementsystem.books.mapper;

import com.company.studentmanagementsystem.books.Book;
import com.company.studentmanagementsystem.books.dto.BookRequestDTO;
import com.company.studentmanagementsystem.books.dto.BookResponseDTO;

public class BookMapper {

    public static Book toEntity(BookRequestDTO dto) {

        Book book = new Book();

        book.setTitle(dto.title());
        book.setAuthor(dto.author());
        book.setIsbn(dto.isbn());
        book.setPrice(dto.price());
        book.setPublishDate(dto.publishDate());

        return book;
    }


    public static BookResponseDTO toDTO(Book book) {

        return new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPrice(),
                book.getPublishDate()
        );
    }
}