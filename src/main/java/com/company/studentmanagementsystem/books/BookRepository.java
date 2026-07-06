package com.company.studentmanagementsystem.books;

import com.company.studentmanagementsystem.students.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

}