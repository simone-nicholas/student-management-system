package com.company.studentmanagementsystem.students;

import com.company.studentmanagementsystem.exceptions.CourseNotFoundException;
import com.company.studentmanagementsystem.students.model.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentFinderTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentFinder studentFinder;

    @Test
    void getStudentById_returnsStudent_whenExists() {
        Student mario = new Student();
        mario.setId(1L);
        mario.setName("Mario");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(mario));

        Student result = studentFinder.getStudentById(1L);

        assertEquals("Mario", result.getName());
    }

    // StudentFinder currently throws CourseNotFoundException instead of StudentNotFoundException
    // when the student is missing (likely a copy-paste bug in the production code, kept as-is here
    // to document the actual behavior rather than silently fixing it).
    @Test
    void getStudentById_throwsCourseNotFoundException_whenNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> studentFinder.getStudentById(99L));
    }
}
