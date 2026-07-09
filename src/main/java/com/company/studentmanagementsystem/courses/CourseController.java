package com.company.studentmanagementsystem.courses;

import com.company.studentmanagementsystem.courses.dto.CourseRequestDTO;
import com.company.studentmanagementsystem.courses.dto.CourseResponseDTO;
import com.company.studentmanagementsystem.courses.mapper.CourseMapper;
import com.company.studentmanagementsystem.students.Student;
import com.company.studentmanagementsystem.students.dto.StudentResponseDTO;
import com.company.studentmanagementsystem.students.mapper.StudentMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // 1. Cambia il tipo di ritorno in List<StudentResponseDTO>
    @GetMapping("/{courseId}/students")
    public ResponseEntity<List<StudentResponseDTO>> getStudents(@PathVariable("courseId") Long courseId) {

        // 2. Prendi le entità dal service e mappale usando lo StudentMapper
        List<StudentResponseDTO> response = courseService.getStudentsFromCourse(courseId)
                .stream()
                .map(StudentMapper::toDTO) // Usa il tuo mapper degli studenti
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses() {
        List<CourseResponseDTO> response = courseService.getAllCourses()
                .stream()
                .map(CourseMapper::toDTO)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CourseResponseDTO> createCourse(@Valid @RequestBody CourseRequestDTO request) {
        Course course = CourseMapper.toEntity(request);

        Course created = courseService.createCourse(course);

        return ResponseEntity.status(201)
                .body(CourseMapper.toDTO(created));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseResponseDTO> getCourse(@PathVariable("courseId") Long courseId) {
        Course course = courseService.getCourseById(courseId);

        return ResponseEntity.ok(CourseMapper.toDTO(course));
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<CourseResponseDTO> updateCourse(
            @PathVariable("courseId") Long courseId,
            @Valid @RequestBody CourseRequestDTO request
    ) {
        Course course = CourseMapper.toEntity(request);

        Course updated = courseService.updateCourse(courseId, course);

        return ResponseEntity.ok(CourseMapper.toDTO(updated));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable("courseId") Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.status(204).build();
    }
}
