package com.company.studentmanagementsystem.students.service;

import com.company.studentmanagementsystem.books.BookRepository;
import com.company.studentmanagementsystem.books.model.Book;
import com.company.studentmanagementsystem.courses.CourseRepository;
import com.company.studentmanagementsystem.courses.model.Course;
import com.company.studentmanagementsystem.exceptions.BookNotFoundException;
import com.company.studentmanagementsystem.exceptions.CourseNotFoundException;
import com.company.studentmanagementsystem.exceptions.StudentNotFoundException;
import com.company.studentmanagementsystem.students.StudentRepository;
import com.company.studentmanagementsystem.students.model.Student;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentPostService {
    private final StudentRepository studentRepository;
    private final BookRepository bookRepository;
    private final CourseRepository courseRepository;

    public StudentPostService(
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

    @Transactional
    public Book assignBookToStudent(Long studentId, Long bookId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
        Book book = getBookById(bookId);

        Student previousStudent = book.getStudent();

        if (student.equals(book.getStudent())) {
            return book;
        }

        if (previousStudent != null) {
            previousStudent.getBooks().remove(book);
        }

        book.setStudent(student);

        if (!student.getBooks().contains(book)) {
            student.getBooks().add(book);
        }

        return bookRepository.save(book);
    }

    @Transactional
    public Course assignCourseToStudent(Long studentId, Long courseId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
        Course course = getCourseById(courseId);

        if (!student.getCourses().contains(course)) {
            student.getCourses().add(course);
        }

        if (!course.getStudents().contains(student)) {
            course.getStudents().add(student);
        }

        studentRepository.save(student);
        courseRepository.save(course);

        return course;
    }

    @Transactional
    public Student create(Student student) {
        return studentRepository.save(student);
    }
}
