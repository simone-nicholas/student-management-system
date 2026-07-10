package com.company.studentmanagementsystem.courses.service;

import com.company.studentmanagementsystem.courses.CourseRepository;
import com.company.studentmanagementsystem.courses.model.Course;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CoursePostService {
    private final CourseRepository courseRepository;

    public CoursePostService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Transactional
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }
}
