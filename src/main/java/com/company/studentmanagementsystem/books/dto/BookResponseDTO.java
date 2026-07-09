package com.company.studentmanagementsystem.books.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookResponseDTO(
        Long id,
        String title,
        String author,
        BigDecimal price,
        LocalDate publishDate
) {}