package com.company.studentmanagementsystem.students;

import com.company.studentmanagementsystem.exceptions.StudentNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
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

    @Transactional
    public Student create(Student student) {
        return studentRepository.save(student);
    }

    @Transactional
    public Student update(Long id, Student student) {
        return studentRepository.findById(id)
                .map(existingStudent -> {
                    if(student.getName() != null) existingStudent.setName(student.getName());
                    if(student.getSurname() != null) existingStudent.setSurname(student.getSurname());
                    if(student.getEmail() != null) existingStudent.setEmail(student.getEmail());
                    if(student.getPhoneNumber() != null) existingStudent.setPhoneNumber(student.getPhoneNumber());

                    return studentRepository.save(existingStudent);
                })
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Transactional
    public void deleteById(Long id) {
        studentRepository.delete(findById(id));
    }
}
