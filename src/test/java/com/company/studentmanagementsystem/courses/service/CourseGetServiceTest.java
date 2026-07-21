package com.company.studentmanagementsystem.courses.service;

import com.company.studentmanagementsystem.courses.CourseRepository;
import com.company.studentmanagementsystem.courses.model.Course;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseGetService courseService;

    @Test
    void getAllCourses_shouldReturnPagedContent() {
        int pageNo = 0;
        int pageSize = 2;
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        List<Course> mockedCourses = List.of(
                new Course(1L, "Introduction to Java", "CS101", "Basic Java course", 6, new ArrayList<>()),
                new Course(2L, "Advanced Databases", "CS202", "Relational databases in depth", 8, new ArrayList<>())
        );
        Page<Course> mockedPage = new PageImpl<>(mockedCourses, pageable, 5); // 5 = totale finto nel "db"

        when(courseRepository.findAll(pageable)).thenReturn(mockedPage);

        Page<Course> result = courseService.getAllCourses(pageNo, pageSize);

        assertEquals(2, result.getContent().size());
        assertEquals("Introduction to Java", result.getContent().get(0).getName());
        assertEquals(3, result.getTotalPages());
    }
}