package com.company.studentmanagementsystem.courses;

import com.company.studentmanagementsystem.courses.dto.CourseRequestDTO;
import com.company.studentmanagementsystem.courses.dto.CourseResponseDTO;
import com.company.studentmanagementsystem.PagedResponse;
import com.company.studentmanagementsystem.courses.mapper.CourseMapper;
import com.company.studentmanagementsystem.courses.model.Course;
import com.company.studentmanagementsystem.courses.service.CourseDeleteService;
import com.company.studentmanagementsystem.courses.service.CourseGetService;
import com.company.studentmanagementsystem.courses.service.CoursePostService;
import com.company.studentmanagementsystem.courses.service.CoursePutService;
import com.company.studentmanagementsystem.students.dto.StudentResponseDTO;
import com.company.studentmanagementsystem.students.mapper.StudentMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {
    private final CoursePostService coursePostService;
    private final CourseGetService courseGetService;
    private final CoursePutService coursePutService;
    private final CourseDeleteService courseDeleteService;
    private final CourseFinder courseFinder;

    public CourseController(
            CoursePostService coursePostService,
            CourseGetService courseGetService,
            CoursePutService coursePutService,
            CourseDeleteService courseDeleteService,
            CourseFinder courseFinder
    ) {
        this.coursePostService = coursePostService;
        this.courseGetService = courseGetService;
        this.coursePutService = coursePutService;
        this.courseDeleteService = courseDeleteService;
        this.courseFinder = courseFinder;
    }

    @GetMapping
    public ResponseEntity<PagedResponse<CourseResponseDTO>> getAllCourses(
            @RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize
    ) {
        Page<Course> page = courseGetService.getAllCourses(pageNo, pageSize);

        PagedResponse<CourseResponseDTO> response = new PagedResponse<>(
                page.getContent().stream().map(CourseMapper::toDTO).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{courseId}/students")
    public ResponseEntity<List<StudentResponseDTO>> getStudents(@PathVariable("courseId") Long courseId) {

        List<StudentResponseDTO> response = courseGetService.getStudentsFromCourse(courseId)
                .stream()
                .map(StudentMapper::toDTO)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseResponseDTO> getCourse(@PathVariable("courseId") Long courseId) {
        Course course = courseFinder.getCourseById(courseId);

        return ResponseEntity.ok(CourseMapper.toDTO(course));
    }

    @PostMapping
    public ResponseEntity<CourseResponseDTO> createCourse(@Valid @RequestBody CourseRequestDTO request) {
        Course course = CourseMapper.toEntity(request);

        Course created = coursePostService.createCourse(course);

        return ResponseEntity.status(201)
                .body(CourseMapper.toDTO(created));
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<CourseResponseDTO> updateCourse(
            @PathVariable("courseId") Long courseId,
            @Valid @RequestBody CourseRequestDTO request
    ) {
        Course course = CourseMapper.toEntity(request);

        Course updated = coursePutService.updateCourse(courseId, course);

        return ResponseEntity.ok(CourseMapper.toDTO(updated));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable("courseId") Long courseId) {
        courseDeleteService.deleteCourse(courseId);
        return ResponseEntity.status(204).build();
    }
}
