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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseGetServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseFinder courseFinder;

    @InjectMocks
    private CourseGetService courseGetService;

    @Test
    void getStudentsFromCourse_returnsStudentsOfCourse() {
        Student mario = new Student();
        mario.setId(1L);
        mario.setName("Mario");

        Course math = new Course();
        math.setId(10L);
        math.setStudents(List.of(mario));

        when(courseFinder.getCourseById(10L)).thenReturn(math);

        List<Student> result = courseGetService.getStudentsFromCourse(10L);

        assertEquals(1, result.size());
        assertEquals("Mario", result.getFirst().getName());
    }

    @Test
    void getStudentsFromCourse_throwsException_whenNotFound() {
        when(courseFinder.getCourseById(99L)).thenThrow(new CourseNotFoundException(99L));

        assertThrows(CourseNotFoundException.class, () -> courseGetService.getStudentsFromCourse(99L));
    }

    @Test
    void getAllCourses_returnsAllCourses() {
        Course math = new Course();
        math.setId(1L);
        math.setName("Matematica");

        Course physics = new Course();
        physics.setId(2L);
        physics.setName("Fisica");

        when(courseRepository.findAll()).thenReturn(List.of(math, physics));

        List<Course> result = courseGetService.getAllCourses();

        assertEquals(2, result.size());
        assertEquals("Matematica", result.get(0).getName());
        assertEquals("Fisica", result.get(1).getName());
    }

    @Test
    void getAllCourses_returnsEmptyList_whenNoCoursesExist() {
        when(courseRepository.findAll()).thenReturn(List.of());

        List<Course> result = courseGetService.getAllCourses();

        assertEquals(0, result.size());
    }
}
