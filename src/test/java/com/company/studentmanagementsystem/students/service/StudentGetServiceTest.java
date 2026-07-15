package com.company.studentmanagementsystem.students.service;

import com.company.studentmanagementsystem.exceptions.StudentNotFoundException;
import com.company.studentmanagementsystem.students.StudentRepository;
import com.company.studentmanagementsystem.students.model.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentGetServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentGetService studentGetService;

    @Test
    void findAll() {
        Student mario = new Student();
        Student alex = new Student();

        mario.setId(1L);
        mario.setName("Mario");

        alex.setId(2L);
        alex.setName("Alex");

        when(studentRepository.findAll()).thenReturn(List.of(mario, alex));

        List<Student> result = studentGetService.findAll();

        assertEquals(2, result.size());
        assertEquals("Mario", result.get(0).getName());
        assertEquals("Alex", result.get(1).getName());
    }

    @Test
    void findAll_returnsEmptyList_whenNoStudentsExist() {
        when(studentRepository.findAll()).thenReturn(List.of());

        List<Student> result = studentGetService.findAll();

        assertEquals(0, result.size());
    }

    @Test
    void findById_returnsStudent_whenExists() {
        Student mario = new Student();
        mario.setId(1L);
        mario.setName("Mario");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(mario));

        Student result = studentGetService.findById(1L);

        assertEquals("Mario", result.getName());
    }

    @Test
    void findById_throwsException_whenNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentGetService.findById(99L));
    }

    @Test
    void getCoursesFromStudent() {
    }

    @Test
    void getStudentBooks() {
    }
}