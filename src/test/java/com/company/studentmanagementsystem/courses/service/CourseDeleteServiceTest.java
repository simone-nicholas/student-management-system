package com.company.studentmanagementsystem.courses.service;

import com.company.studentmanagementsystem.courses.CourseFinder;
import com.company.studentmanagementsystem.courses.CourseRepository;
import com.company.studentmanagementsystem.courses.model.Course;
import com.company.studentmanagementsystem.exceptions.CourseNotFoundException;
import com.company.studentmanagementsystem.students.model.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseDeleteServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseFinder courseFinder;

    @InjectMocks
    private CourseDeleteService courseDeleteService;

    @Test
    void deleteCourse_removesAllAssociationsAndDeletesCourse() {
        Course math = new Course();
        math.setId(10L);

        Student mario = new Student();
        mario.setId(1L);
        mario.setCourses(new ArrayList<>(List.of(math)));

        math.setStudents(new ArrayList<>(List.of(mario)));

        when(courseFinder.getCourseById(10L)).thenReturn(math);

        courseDeleteService.deleteCourse(10L);

        assertFalse(mario.getCourses().contains(math));
        assertTrue(math.getStudents().isEmpty());
        verify(courseRepository).delete(math);
    }

    @Test
    void deleteCourse_deletesCourse_whenNoStudentsAssigned() {
        Course math = new Course();
        math.setId(10L);
        math.setStudents(new ArrayList<>());

        when(courseFinder.getCourseById(10L)).thenReturn(math);

        courseDeleteService.deleteCourse(10L);

        verify(courseRepository).delete(math);
    }

    @Test
    void deleteCourse_throwsException_whenCourseNotFound() {
        when(courseFinder.getCourseById(99L)).thenThrow(new CourseNotFoundException(99L));

        assertThrows(CourseNotFoundException.class, () -> courseDeleteService.deleteCourse(99L));
    }
}
