package com.company.studentmanagementsystem.courses;

import com.company.studentmanagementsystem.courses.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

}