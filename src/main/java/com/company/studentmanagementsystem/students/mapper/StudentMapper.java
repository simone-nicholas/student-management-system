package com.company.studentmanagementsystem.students.mapper;

import com.company.studentmanagementsystem.students.model.Student;
import com.company.studentmanagementsystem.students.dto.StudentRequestDTO;
import com.company.studentmanagementsystem.students.dto.StudentResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public static Student toEntity(StudentRequestDTO dto) {
        Student student = new Student();

        student.setName(dto.name());
        student.setSurname(dto.surname());
        student.setEmail(dto.email());
        student.setPhoneNumber(dto.phoneNumber());

        return student;
    }

    public static StudentResponseDTO toDTO(Student student) {
        return new StudentResponseDTO(
                student.getId(),
                student.getName(),
                student.getSurname(),
                student.getEmail(),
                student.getPhoneNumber()
        );
    }
}