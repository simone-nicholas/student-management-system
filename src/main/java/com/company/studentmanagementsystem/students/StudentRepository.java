package com.company.studentmanagementsystem.students;

import com.company.studentmanagementsystem.students.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

}