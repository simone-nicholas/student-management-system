package com.company.studentmanagementsystem.courses;

import com.company.studentmanagementsystem.exceptions.CourseNotFoundException;
import com.company.studentmanagementsystem.exceptions.StudentNotFoundException;
import com.company.studentmanagementsystem.students.Student;
import com.company.studentmanagementsystem.students.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public CourseService(CourseRepository courseRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional(readOnly = true)
    public List<Student> getStudentsFromCourse(Long courseId) {
        Course course = getCourseById(courseId);

        return course.getStudents();
    }

    @Transactional(readOnly = true)
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Transactional
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    @Transactional(readOnly = true)
    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
    }

    @Transactional
    public Course updateCourse(Long courseId, Course courseDetails) {
        Course course = getCourseById(courseId);
        if (courseDetails.getName() != null) course.setName(courseDetails.getName());
        if (courseDetails.getCode() != null) course.setCode(courseDetails.getCode());
        if (courseDetails.getDescription() != null) course.setDescription(courseDetails.getDescription());
        if (courseDetails.getCredits() != null) course.setCredits(courseDetails.getCredits());
        return courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(Long courseId) {
        Course course = getCourseById(courseId);

        for (Student student : course.getStudents()) {
            student.getCourses().remove(course);
        }

        course.getStudents().clear();

        courseRepository.delete(course);
    }
}