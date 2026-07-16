package com.company.studentmanagementsystem.courses.service;

import com.company.studentmanagementsystem.courses.CourseRepository;
import com.company.studentmanagementsystem.courses.model.Course;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CoursePostServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CoursePostService coursePostService;

    @Test
    void createCourse_savesAndReturnsCourse() {
        Course math = new Course();
        math.setName("Matematica");

        when(courseRepository.save(math)).thenReturn(math);

        Course result = coursePostService.createCourse(math);

        assertEquals("Matematica", result.getName());
        verify(courseRepository).save(math);
    }
}
