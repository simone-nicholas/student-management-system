package com.company.studentmanagementsystem.students.service;

import com.company.studentmanagementsystem.exceptions.StudentNotFoundException;
import com.company.studentmanagementsystem.students.StudentRepository;
import com.company.studentmanagementsystem.students.model.Student;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentPutService {
    private final StudentRepository studentRepository;

    public  StudentPutService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional(readOnly = true)
    public Student findById(Long id){
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Transactional
    public Student update(Long id, Student student) {
        Student existingStudent = findById(id);

        if(student.getName() != null)
            existingStudent.setName(student.getName());
        if(student.getSurname() != null)
            existingStudent.setSurname(student.getSurname());
        if(student.getEmail() != null)
            existingStudent.setEmail(student.getEmail());
        if(student.getPhoneNumber() != null)
            existingStudent.setPhoneNumber(student.getPhoneNumber());

        return existingStudent;
    }
}
