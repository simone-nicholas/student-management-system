package com.company.studentmanagementsystem.courses.service;

import com.company.studentmanagementsystem.courses.CourseFinder;
import com.company.studentmanagementsystem.courses.CourseRepository;
import com.company.studentmanagementsystem.courses.model.Course;
import com.company.studentmanagementsystem.exceptions.CourseNotFoundException;
import com.company.studentmanagementsystem.students.model.Student;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseDeleteService {
    private final CourseRepository courseRepository;
    private final CourseFinder courseFinder;

    public CourseDeleteService(
            CourseRepository courseRepository,
           CourseFinder courseFinder
    ) {
        this.courseRepository = courseRepository;
        this.courseFinder = courseFinder;
    }

    @Transactional
    public void deleteCourse(Long courseId) {
        Course course = courseFinder.getCourseById(courseId);

        for (Student student : course.getStudents()) {
            student.getCourses().remove(course);
        }

        course.getStudents().clear();

        courseRepository.delete(course);
    }
}
