package com.company.studentmanagementsystem.courses.service;

import com.company.studentmanagementsystem.courses.CourseFinder;
import com.company.studentmanagementsystem.courses.CourseRepository;
import com.company.studentmanagementsystem.courses.model.Course;
import com.company.studentmanagementsystem.exceptions.CourseNotFoundException;
import com.company.studentmanagementsystem.students.model.Student;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseGetService {
    private final CourseRepository courseRepository;
    private final CourseFinder courseFinder;

    public CourseGetService(
            CourseRepository courseRepository,
            CourseFinder courseFinder
    ) {
        this.courseRepository = courseRepository;
        this.courseFinder = courseFinder;
    }

    @Transactional(readOnly = true)
    public List<Student> getStudentsFromCourse(Long courseId) {
        Course course = courseFinder.getCourseById(courseId);

        return course.getStudents();
    }

    @Transactional(readOnly = true)
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
}
