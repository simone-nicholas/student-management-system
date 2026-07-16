package com.company.studentmanagementsystem.courses;

import com.company.studentmanagementsystem.courses.model.Course;
import com.company.studentmanagementsystem.exceptions.CourseNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseFinderTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseFinder courseFinder;

    @Test
    void getCourseById_returnsCourse_whenExists() {
        Course math = new Course();
        math.setId(1L);
        math.setName("Matematica");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(math));

        Course result = courseFinder.getCourseById(1L);

        assertEquals("Matematica", result.getName());
    }

    @Test
    void getCourseById_throwsException_whenNotFound() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> courseFinder.getCourseById(99L));
    }
}
