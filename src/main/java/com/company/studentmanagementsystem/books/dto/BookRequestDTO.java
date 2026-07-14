package com.company.studentmanagementsystem.books.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookRequestDTO(

        @NotBlank
        String title,

        @NotBlank
        String author,

        @NotBlank
        @Size(min = 10, max = 17)
        String isbn,

        @NotNull
        BigDecimal price,

        @NotNull
        LocalDate publishDate

) {}