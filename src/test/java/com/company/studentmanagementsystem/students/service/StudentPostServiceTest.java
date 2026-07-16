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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentPostServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private StudentPostService studentPostService;

    @Test
    void getBookById_returnsBook_whenExists() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Cyber Security");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book result = studentPostService.getBookById(1L);

        assertEquals("Cyber Security", result.getTitle());
    }

    @Test
    void getBookById_throwsException_whenNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> studentPostService.getBookById(99L));
    }

    @Test
    void getCourseById_returnsCourse_whenExists() {
        Course course = new Course();
        course.setId(1L);
        course.setName("Matematica");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Course result = studentPostService.getCourseById(1L);

        assertEquals("Matematica", result.getName());
    }

    @Test
    void getCourseById_throwsException_whenNotFound() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> studentPostService.getCourseById(99L));
    }

    @Test
    void assignBookToStudent_assignsBook_whenNotYetAssigned() {
        Student mario = new Student();
        mario.setId(1L);
        mario.setBooks(new ArrayList<>());

        Book book = new Book();
        book.setId(10L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(mario));
        when(bookRepository.findById(10L)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);

        Book result = studentPostService.assignBookToStudent(1L, 10L);

        assertEquals(mario, result.getStudent());
        assertTrue(mario.getBooks().contains(book));
    }

    @Test
    void assignBookToStudent_returnsSameBook_whenAlreadyAssignedToSameStudent() {
        Student mario = new Student();
        mario.setId(1L);

        Book book = new Book();
        book.setId(10L);
        book.setStudent(mario);
        mario.setBooks(new ArrayList<>(List.of(book)));

        when(studentRepository.findById(1L)).thenReturn(Optional.of(mario));
        when(bookRepository.findById(10L)).thenReturn(Optional.of(book));

        Book result = studentPostService.assignBookToStudent(1L, 10L);

        assertEquals(book, result);
        verify(bookRepository, never()).save(any());
    }

    @Test
    void assignBookToStudent_removesFromPreviousStudent_whenAssignedElsewhere() {
        Student previousStudent = new Student();
        previousStudent.setId(2L);

        Book book = new Book();
        book.setId(10L);
        book.setStudent(previousStudent);
        previousStudent.setBooks(new ArrayList<>(List.of(book)));

        Student newStudent = new Student();
        newStudent.setId(1L);
        newStudent.setBooks(new ArrayList<>());

        when(studentRepository.findById(1L)).thenReturn(Optional.of(newStudent));
        when(bookRepository.findById(10L)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);

        Book result = studentPostService.assignBookToStudent(1L, 10L);

        assertFalse(previousStudent.getBooks().contains(book));
        assertTrue(newStudent.getBooks().contains(book));
        assertEquals(newStudent, result.getStudent());
    }

    @Test
    void assignBookToStudent_throwsException_whenStudentNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentPostService.assignBookToStudent(99L, 10L));
    }

    @Test
    void assignBookToStudent_throwsException_whenBookNotFound() {
        Student mario = new Student();
        mario.setId(1L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(mario));
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> studentPostService.assignBookToStudent(1L, 99L));
    }

    @Test
    void assignCourseToStudent_assignsCourse_whenNotYetAssigned() {
        Student mario = new Student();
        mario.setId(1L);
        mario.setCourses(new ArrayList<>());

        Course math = new Course();
        math.setId(10L);
        math.setStudents(new ArrayList<>());

        when(studentRepository.findById(1L)).thenReturn(Optional.of(mario));
        when(courseRepository.findById(10L)).thenReturn(Optional.of(math));

        Course result = studentPostService.assignCourseToStudent(1L, 10L);

        assertTrue(mario.getCourses().contains(math));
        assertTrue(math.getStudents().contains(mario));
        verify(studentRepository).save(mario);
        verify(courseRepository).save(math);
        assertEquals(math, result);
    }

    @Test
    void assignCourseToStudent_doesNotDuplicate_whenAlreadyAssigned() {
        Student mario = new Student();
        mario.setId(1L);

        Course math = new Course();
        math.setId(10L);

        mario.setCourses(new ArrayList<>(List.of(math)));
        math.setStudents(new ArrayList<>(List.of(mario)));

        when(studentRepository.findById(1L)).thenReturn(Optional.of(mario));
        when(courseRepository.findById(10L)).thenReturn(Optional.of(math));

        studentPostService.assignCourseToStudent(1L, 10L);

        assertEquals(1, mario.getCourses().size());
        assertEquals(1, math.getStudents().size());
    }

    @Test
    void assignCourseToStudent_throwsException_whenStudentNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentPostService.assignCourseToStudent(99L, 10L));
    }

    @Test
    void assignCourseToStudent_throwsException_whenCourseNotFound() {
        Student mario = new Student();
        mario.setId(1L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(mario));
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> studentPostService.assignCourseToStudent(1L, 99L));
    }

    @Test
    void create_savesAndReturnsStudent() {
        Student mario = new Student();
        mario.setName("Mario");

        when(studentRepository.save(mario)).thenReturn(mario);

        Student result = studentPostService.create(mario);

        assertEquals("Mario", result.getName());
        verify(studentRepository).save(mario);
    }
}