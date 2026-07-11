package com.company.studentmanagementsystem.students.service;

import com.company.studentmanagementsystem.books.BookRepository;
import com.company.studentmanagementsystem.books.model.Book;
import com.company.studentmanagementsystem.courses.CourseRepository;
import com.company.studentmanagementsystem.courses.model.Course;
import com.company.studentmanagementsystem.exceptions.BookNotAssignedException;
import com.company.studentmanagementsystem.exceptions.BookNotFoundException;
import com.company.studentmanagementsystem.exceptions.CourseNotFoundException;
import com.company.studentmanagementsystem.exceptions.StudentNotFoundException;
import com.company.studentmanagementsystem.students.StudentRepository;
import com.company.studentmanagementsystem.students.model.Student;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentDeleteService {

    private final StudentRepository studentRepository;
    private final BookRepository bookRepository;
    private final CourseRepository courseRepository;

    public StudentDeleteService(
            StudentRepository studentRepository,
            BookRepository bookRepository,
            CourseRepository courseRepository
            ) {
        this.studentRepository = studentRepository;
        this.bookRepository = bookRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional(readOnly = true)
    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
    }

    @Transactional(readOnly = true)
    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
    }

    @Transactional(readOnly = true)
    public Student findById(Long id){
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Transactional
    public void deleteById(Long id) {
        Student student = findById(id);

        for (Course course : List.copyOf(student.getCourses())) {
            course.getStudents().remove(student);
        }

        student.getCourses().clear();

        for (Book book : student.getBooks()) {
            book.setStudent(null);
        }

        student.getBooks().clear();

        studentRepository.delete(student);
    }

    @Transactional
    public void removeCourseFromStudent(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
        Course course = getCourseById(courseId);

        student.getCourses().remove(course);
        course.getStudents().remove(student);

        studentRepository.save(student);
        courseRepository.save(course);
    }

    @Transactional
    public void removeBookFromStudent(Long studentId, Long bookId) {
        Book book = getBookById(bookId);

        if (book.getStudent() == null ||
                !book.getStudent().getId().equals(studentId)) {
            throw new BookNotAssignedException(bookId, studentId);
        }

        book.getStudent().getBooks().remove(book);
        book.setStudent(null);
        bookRepository.save(book);
    }
}
