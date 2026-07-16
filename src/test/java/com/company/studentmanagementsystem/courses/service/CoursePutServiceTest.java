package com.company.studentmanagementsystem.courses.service;

import com.company.studentmanagementsystem.courses.CourseFinder;
import com.company.studentmanagementsystem.courses.CourseRepository;
import com.company.studentmanagementsystem.courses.model.Course;
import com.company.studentmanagementsystem.exceptions.CourseNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CoursePutServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseFinder courseFinder;

    @InjectMocks
    private CoursePutService coursePutService;

    @Test
    void updateCourse_updatesAllFields_whenAllProvided() {
        Course existingCourse = new Course();
        existingCourse.setId(1L);
        existingCourse.setName("Matematica");
        existingCourse.setCode("MAT101");
        existingCourse.setDescription("Corso base");
        existingCourse.setCredits(6);

        Course incomingData = new Course();
        incomingData.setName("Matematica Avanzata");
        incomingData.setCode("MAT202");
        incomingData.setDescription("Corso avanzato");
        incomingData.setCredits(9);

        when(courseFinder.getCourseById(1L)).thenReturn(existingCourse);
        when(courseRepository.save(existingCourse)).thenReturn(existingCourse);

        Course result = coursePutService.updateCourse(1L, incomingData);

        assertEquals("Matematica Avanzata", result.getName());
        assertEquals("MAT202", result.getCode());
        assertEquals("Corso avanzato", result.getDescription());
        assertEquals(9, result.getCredits());
    }

    @Test
    void updateCourse_keepsExistingFields_whenIncomingFieldsAreNull() {
        Course existingCourse = new Course();
        existingCourse.setId(1L);
        existingCourse.setName("Matematica");
        existingCourse.setCode("MAT101");
        existingCourse.setDescription("Corso base");
        existingCourse.setCredits(6);

        Course incomingData = new Course();

        when(courseFinder.getCourseById(1L)).thenReturn(existingCourse);
        when(courseRepository.save(existingCourse)).thenReturn(existingCourse);

        Course result = coursePutService.updateCourse(1L, incomingData);

        assertEquals("Matematica", result.getName());
        assertEquals("MAT101", result.getCode());
        assertEquals("Corso base", result.getDescription());
        assertEquals(6, result.getCredits());
    }

    @Test
    void updateCourse_updatesOnlyProvidedFields_whenSomeAreNull() {
        Course existingCourse = new Course();
        existingCourse.setId(1L);
        existingCourse.setName("Matematica");
        existingCourse.setCode("MAT101");
        existingCourse.setDescription("Corso base");
        existingCourse.setCredits(6);

        Course incomingData = new Course();
        incomingData.setCredits(12);

        when(courseFinder.getCourseById(1L)).thenReturn(existingCourse);
        when(courseRepository.save(existingCourse)).thenReturn(existingCourse);

        Course result = coursePutService.updateCourse(1L, incomingData);

        assertEquals("Matematica", result.getName());
        assertEquals("MAT101", result.getCode());
        assertEquals("Corso base", result.getDescription());
        assertEquals(12, result.getCredits());
    }

    @Test
    void updateCourse_throwsException_whenCourseNotFound() {
        Course incomingData = new Course();
        incomingData.setName("Matematica Avanzata");

        when(courseFinder.getCourseById(99L)).thenThrow(new CourseNotFoundException(99L));

        assertThrows(CourseNotFoundException.class, () -> coursePutService.updateCourse(99L, incomingData));
    }
}
