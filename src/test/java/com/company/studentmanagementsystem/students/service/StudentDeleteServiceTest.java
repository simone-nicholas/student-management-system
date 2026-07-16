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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentDeleteServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private StudentDeleteService studentDeleteService;

    @Test
    void getBookById_returnsBook_whenExists() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Cyber Security");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book result = studentDeleteService.getBookById(1L);

        assertEquals("Cyber Security", result.getTitle());
    }

    @Test
    void getBookById_throwsException_whenNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> studentDeleteService.getBookById(99L));
    }

    @Test
    void getCourseById_returnsCourse_whenExists() {
        Course course = new Course();
        course.setId(1L);
        course.setName("Matematica");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Course result = studentDeleteService.getCourseById(1L);

        assertEquals("Matematica", result.getName());
    }

    @Test
    void getCourseById_throwsException_whenNotFound() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> studentDeleteService.getCourseById(99L));
    }

    @Test
    void findById_returnsStudent_whenExists() {
        Student mario = new Student();
        mario.setId(1L);
        mario.setName("Mario");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(mario));

        Student result = studentDeleteService.findById(1L);

        assertEquals("Mario", result.getName());
    }

    @Test
    void findById_throwsException_whenNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentDeleteService.findById(99L));
    }

    @Test
    void deleteById_removesAllAssociationsAndDeletesStudent() {
        Student mario = new Student();
        mario.setId(1L);

        Course math = new Course();
        math.setId(10L);
        math.setStudents(new ArrayList<>(List.of(mario)));

        Book book = new Book();
        book.setId(20L);
        book.setStudent(mario);

        mario.setCourses(new ArrayList<>(List.of(math)));
        mario.setBooks(new ArrayList<>(List.of(book)));

        when(studentRepository.findById(1L)).thenReturn(Optional.of(mario));

        studentDeleteService.deleteById(1L);

        assertFalse(math.getStudents().contains(mario));
        assertTrue(mario.getCourses().isEmpty());
        assertNull(book.getStudent());
        assertTrue(mario.getBooks().isEmpty());
        verify(studentRepository).delete(mario);
    }

    @Test
    void deleteById_throwsException_whenStudentNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentDeleteService.deleteById(99L));
    }

    @Test
    void removeCourseFromStudent_removesAssociationBetweenStudentAndCourse() {
        Student mario = new Student();
        mario.setId(1L);

        Course math = new Course();
        math.setId(10L);

        mario.setCourses(new ArrayList<>(List.of(math)));
        math.setStudents(new ArrayList<>(List.of(mario)));

        when(studentRepository.findById(1L)).thenReturn(Optional.of(mario));
        when(courseRepository.findById(10L)).thenReturn(Optional.of(math));

        studentDeleteService.removeCourseFromStudent(1L, 10L);

        assertFalse(mario.getCourses().contains(math));
        assertFalse(math.getStudents().contains(mario));
        verify(studentRepository).save(mario);
        verify(courseRepository).save(math);
    }

    @Test
    void removeCourseFromStudent_throwsException_whenStudentNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentDeleteService.removeCourseFromStudent(99L, 10L));
    }

    @Test
    void removeCourseFromStudent_throwsException_whenCourseNotFound() {
        Student mario = new Student();
        mario.setId(1L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(mario));
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> studentDeleteService.removeCourseFromStudent(1L, 99L));
    }

    @Test
    void removeBookFromStudent_removesAssociation_whenBookIsAssignedToStudent() {
        Student mario = new Student();
        mario.setId(1L);

        Book book = new Book();
        book.setId(20L);
        book.setStudent(mario);
        mario.setBooks(new ArrayList<>(List.of(book)));

        when(bookRepository.findById(20L)).thenReturn(Optional.of(book));

        studentDeleteService.removeBookFromStudent(1L, 20L);

        assertNull(book.getStudent());
        assertFalse(mario.getBooks().contains(book));
        verify(bookRepository).save(book);
    }

    @Test
    void removeBookFromStudent_throwsException_whenBookHasNoStudent() {
        Book book = new Book();
        book.setId(20L);
        book.setStudent(null);

        when(bookRepository.findById(20L)).thenReturn(Optional.of(book));

        assertThrows(BookNotAssignedException.class, () -> studentDeleteService.removeBookFromStudent(1L, 20L));
    }

    @Test
    void removeBookFromStudent_throwsException_whenBookAssignedToDifferentStudent() {
        Student otherStudent = new Student();
        otherStudent.setId(2L);

        Book book = new Book();
        book.setId(20L);
        book.setStudent(otherStudent);

        when(bookRepository.findById(20L)).thenReturn(Optional.of(book));

        assertThrows(BookNotAssignedException.class, () -> studentDeleteService.removeBookFromStudent(1L, 20L));
    }

    @Test
    void removeBookFromStudent_throwsException_whenBookNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> studentDeleteService.removeBookFromStudent(1L, 99L));
    }
}