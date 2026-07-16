package com.company.studentmanagementsystem.students.service;

import com.company.studentmanagementsystem.exceptions.StudentNotFoundException;
import com.company.studentmanagementsystem.students.StudentRepository;
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
class StudentPutServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentPutService studentPutService;

    @Test
    void findById_returnsStudent_whenExists() {
        Student mario = new Student();
        mario.setId(1L);
        mario.setName("Mario");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(mario));

        Student result = studentPutService.findById(1L);

        assertEquals("Mario", result.getName());
    }

    @Test
    void findById_throwsException_whenNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentPutService.findById(99L));
    }

    @Test
    void update_updatesAllFields_whenAllProvided() {
        Student existingStudent = new Student();
        existingStudent.setId(1L);
        existingStudent.setName("Mario");
        existingStudent.setSurname("Rossi");
        existingStudent.setEmail("mario.rossi@email.com");
        existingStudent.setPhoneNumber("1234567890");

        Student incomingData = new Student();
        incomingData.setName("Luigi");
        incomingData.setSurname("Verdi");
        incomingData.setEmail("luigi.verdi@email.com");
        incomingData.setPhoneNumber("0987654321");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent));

        Student result = studentPutService.update(1L, incomingData);

        assertEquals("Luigi", result.getName());
        assertEquals("Verdi", result.getSurname());
        assertEquals("luigi.verdi@email.com", result.getEmail());
        assertEquals("0987654321", result.getPhoneNumber());
    }

    @Test
    void update_keepsExistingFields_whenIncomingFieldsAreNull() {
        Student existingStudent = new Student();
        existingStudent.setId(1L);
        existingStudent.setName("Mario");
        existingStudent.setSurname("Rossi");
        existingStudent.setEmail("mario.rossi@email.com");
        existingStudent.setPhoneNumber("1234567890");

        Student incomingData = new Student();

        when(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent));

        Student result = studentPutService.update(1L, incomingData);

        assertEquals("Mario", result.getName());
        assertEquals("Rossi", result.getSurname());
        assertEquals("mario.rossi@email.com", result.getEmail());
        assertEquals("1234567890", result.getPhoneNumber());
    }

    @Test
    void update_updatesOnlyProvidedFields_whenSomeAreNull() {
        Student existingStudent = new Student();
        existingStudent.setId(1L);
        existingStudent.setName("Mario");
        existingStudent.setSurname("Rossi");
        existingStudent.setEmail("mario.rossi@email.com");
        existingStudent.setPhoneNumber("1234567890");

        Student incomingData = new Student();
        incomingData.setEmail("nuovo.mario@email.com");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent));

        Student result = studentPutService.update(1L, incomingData);

        assertEquals("Mario", result.getName());
        assertEquals("Rossi", result.getSurname());
        assertEquals("nuovo.mario@email.com", result.getEmail());
        assertEquals("1234567890", result.getPhoneNumber());
    }

    @Test
    void update_throwsException_whenStudentNotFound() {
        Student incomingData = new Student();
        incomingData.setName("Luigi");

        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentPutService.update(99L, incomingData));
    }
}