package com.company.studentmanagementsystem.courses;

import com.company.studentmanagementsystem.students.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CourseController {
    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/students/{studentId}/courses")
    public ResponseEntity<List<Course>> getCourses(@PathVariable("studentId") Long studentId) {
        return ResponseEntity.ok(courseService.getCoursesFromStudent(studentId));
    }

    @GetMapping("/courses/{courseId}/students")
    public ResponseEntity<List<Student>> getStudents(@PathVariable("courseId") Long courseId) {
        return ResponseEntity.ok(courseService.getStudentsFromCourse(courseId));
    }

    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        return ResponseEntity.status(201).body(courseService.createCourse(course));
    }

    @PostMapping("/students/{studentId}/courses/{courseId}")
    public ResponseEntity<Course> assignCourseToStudent(@PathVariable Long studentId, @PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.assignCourseToStudent(studentId, courseId));
    }

    @GetMapping("/courses/{courseId}")
    public ResponseEntity<Course> getCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getCourseById(courseId));
    }

    @PutMapping("/courses/{courseId}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long courseId, @RequestBody Course course) {
        return ResponseEntity.ok(courseService.updateCourse(courseId, course));
    }

    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping("/students/{studentId}/courses/{courseId}")
    public ResponseEntity<Void> removeCourseFromStudent(@PathVariable Long studentId, @PathVariable Long courseId) {
        courseService.removeCourseFromStudent(studentId, courseId);
        return ResponseEntity.status(204).build();
    }
}
