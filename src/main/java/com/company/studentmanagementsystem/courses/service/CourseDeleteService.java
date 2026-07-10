package com.company.studentmanagementsystem.courses.service;

import com.company.studentmanagementsystem.courses.CourseRepository;
import com.company.studentmanagementsystem.courses.model.Course;
import com.company.studentmanagementsystem.exceptions.CourseNotFoundException;
import com.company.studentmanagementsystem.students.model.Student;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseDeleteService {
    private final CourseRepository courseRepository;

    public CourseDeleteService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Transactional(readOnly = true)
    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
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
