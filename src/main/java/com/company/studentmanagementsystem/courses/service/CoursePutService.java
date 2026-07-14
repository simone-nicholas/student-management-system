package com.company.studentmanagementsystem.courses.service;

import com.company.studentmanagementsystem.courses.CourseFinder;
import com.company.studentmanagementsystem.courses.CourseRepository;
import com.company.studentmanagementsystem.courses.model.Course;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CoursePutService {

    private final CourseRepository courseRepository;
    private final CourseFinder courseFinder;

    public CoursePutService(
            CourseRepository courseRepository,
            CourseFinder courseFinder) {

        this.courseRepository = courseRepository;
        this.courseFinder = courseFinder;
    }

    @Transactional
    public Course updateCourse(Long courseId, Course courseDetails) {
        Course course = courseFinder.getCourseById(courseId);
        if (courseDetails.getName() != null) course.setName(courseDetails.getName());
        if (courseDetails.getCode() != null) course.setCode(courseDetails.getCode());
        if (courseDetails.getDescription() != null) course.setDescription(courseDetails.getDescription());
        if (courseDetails.getCredits() != null) course.setCredits(courseDetails.getCredits());
        return courseRepository.save(course);
    }
}
