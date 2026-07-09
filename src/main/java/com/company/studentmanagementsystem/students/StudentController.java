package com.company.studentmanagementsystem.students;

import com.company.studentmanagementsystem.students.dto.StudentRequestDTO;
import com.company.studentmanagementsystem.students.dto.StudentResponseDTO;
import com.company.studentmanagementsystem.students.mapper.StudentMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {
        List<StudentResponseDTO> students = studentService.findAll()
                .stream()
                .map(student -> StudentMapper.toDTO(student))
                .toList();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable("id") Long id) {
        Student student = studentService.findById(id);
        return ResponseEntity.ok(StudentMapper.toDTO(student));
    }

    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(@Valid @RequestBody StudentRequestDTO requestDTO) {
        Student studentEntity = StudentMapper.toEntity(requestDTO);
        Student created = studentService.create(studentEntity);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(StudentMapper.toDTO(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @PathVariable("id") Long id,
            @Valid @RequestBody StudentRequestDTO requestDTO
    ) {
        Student studentEntity = StudentMapper.toEntity(requestDTO);
        Student updated = studentService.update(id, studentEntity);
        //  CORRETTO: Adesso usa la chiamata standard al metodo statico
        return ResponseEntity.ok(StudentMapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable("id") Long id) {
        studentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}