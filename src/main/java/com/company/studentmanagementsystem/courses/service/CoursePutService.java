package com.company.studentmanagementsystem.courses.service;

import com.company.studentmanagementsystem.courses.CourseRepository;
import com.company.studentmanagementsystem.courses.model.Course;
import com.company.studentmanagementsystem.exceptions.CourseNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CoursePutService {

    private final CourseRepository courseRepository;

    public CoursePutService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
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
}
