package com.company.studentmanagementsystem.books;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookInterface extends JpaRepository<Book, Long> {

}