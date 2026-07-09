package com.company.studentmanagementsystem.students;

import com.company.studentmanagementsystem.books.Book;
import com.company.studentmanagementsystem.books.dto.BookResponseDTO;
import com.company.studentmanagementsystem.books.mapper.BookMapper;
import com.company.studentmanagementsystem.courses.Course;
import com.company.studentmanagementsystem.courses.dto.CourseResponseDTO;
import com.company.studentmanagementsystem.courses.mapper.CourseMapper;
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

    @GetMapping("/{studentId}/courses")
    public ResponseEntity<List<CourseResponseDTO>> getCourses(
            @PathVariable("studentId") Long studentId) {
        List<CourseResponseDTO> response = studentService.getCoursesFromStudent(studentId)
                .stream()
                .map(CourseMapper::toDTO)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{studentId}/courses/{courseId}")
    public ResponseEntity<CourseResponseDTO> assignCourseToStudent(
            @PathVariable("studentId") Long studentId,
            @PathVariable("courseId") Long courseId
    ) {
        Course course = studentService.assignCourseToStudent(studentId, courseId);

        return ResponseEntity.ok(CourseMapper.toDTO(course));
    }

    @PostMapping("/{studentId}/books/{bookId}")
    public ResponseEntity<BookResponseDTO> assignBookToStudent(
            @PathVariable Long studentId,
            @PathVariable Long bookId
    ) {

        Book book = studentService.assignBookToStudent(studentId, bookId);

        return ResponseEntity.ok(
                BookMapper.toDTO(book)
        );
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

    @DeleteMapping("/{studentId}/courses/{courseId}")
    public ResponseEntity<Void> removeCourseFromStudent(
            @PathVariable("studentId") Long studentId,
            @PathVariable("courseId") Long courseId) {
        studentService.removeCourseFromStudent(studentId, courseId);
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/{studentId}/books")
    public ResponseEntity<List<BookResponseDTO>> getAllBooksByStudentId(
            @PathVariable Long studentId
    ) {

        List<BookResponseDTO> books = studentService.getStudentBooks(studentId)
                .stream()
                .map(BookMapper::toDTO)
                .toList();

        return ResponseEntity.ok(books);
    }

    @DeleteMapping("/students/{studentId}/books/{bookId}")
    public ResponseEntity<Void> removeBookFromStudent(
            @PathVariable Long studentId,
            @PathVariable Long bookId
    ) {

        studentService.removeBookFromStudent(studentId, bookId);

        return ResponseEntity.noContent().build();
    }
}