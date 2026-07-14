package com.company.studentmanagementsystem.students.service;

import com.company.studentmanagementsystem.books.BookRepository;
import com.company.studentmanagementsystem.books.model.Book;
import com.company.studentmanagementsystem.courses.CourseRepository;
import com.company.studentmanagementsystem.courses.model.Course;
import com.company.studentmanagementsystem.exceptions.BookNotFoundException;
import com.company.studentmanagementsystem.exceptions.CourseNotFoundException;
import com.company.studentmanagementsystem.exceptions.StudentNotFoundException;
import com.company.studentmanagementsystem.students.StudentFinder;
import com.company.studentmanagementsystem.students.StudentRepository;
import com.company.studentmanagementsystem.students.model.Student;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentGetService {
    private final StudentRepository studentRepository;
    private final StudentFinder studentFinder;

    public StudentGetService(
            StudentRepository studentRepository,
            StudentFinder studentFinder
    ) {
        this.studentRepository = studentRepository;
        this.studentFinder = studentFinder;
    }

    @Transactional(readOnly = true)
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Student findById(Long id){
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Course> getCoursesFromStudent(Long studentId) {
        Student student = studentFinder.getStudentById(studentId);

        return student.getCourses();
    }

    @Transactional(readOnly = true)
    public List<Book> getStudentBooks(Long studentId) {
        Student student = studentFinder.getStudentById(studentId);

        return student.getBooks();
    }
}
