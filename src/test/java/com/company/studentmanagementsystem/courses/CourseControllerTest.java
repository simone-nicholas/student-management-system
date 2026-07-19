package com.company.studentmanagementsystem.courses;

import com.company.studentmanagementsystem.courses.dto.CourseRequestDTO;
import com.company.studentmanagementsystem.courses.model.Course;
import com.company.studentmanagementsystem.courses.service.CourseDeleteService;
import com.company.studentmanagementsystem.courses.service.CourseGetService;
import com.company.studentmanagementsystem.courses.service.CoursePostService;
import com.company.studentmanagementsystem.courses.service.CoursePutService;
import com.company.studentmanagementsystem.exceptions.CourseNotFoundException;
import com.company.studentmanagementsystem.students.model.Student;
import tools.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper objectMapper;

    @MockitoBean
    private CoursePostService coursePostService;

    @MockitoBean
    private CourseGetService courseGetService;

    @MockitoBean
    private CoursePutService coursePutService;

    @MockitoBean
    private CourseDeleteService courseDeleteService;

    @MockitoBean
    private CourseFinder courseFinder;

    @Test
    void getStudents_returnsStudentsOfCourse() throws Exception {
        Student mario = new Student("Mario", "Rossi", "mario.rossi@email.com", "1234567890");
        mario.setId(1L);

        when(courseGetService.getStudentsFromCourse(10L)).thenReturn(List.of(mario));

        mockMvc.perform(get("/api/v1/courses/{courseId}/students", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Mario"));
    }

    @Test
    void getStudents_returns404_whenCourseNotFound() throws Exception {
        when(courseGetService.getStudentsFromCourse(99L)).thenThrow(new CourseNotFoundException(99L));

        mockMvc.perform(get("/api/v1/courses/{courseId}/students", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllCourses_returnsListOfCourses() throws Exception {
        Course math = new Course("Matematica", "MAT101", "Corso base", 6);
        math.setId(1L);

        when(courseGetService.getAllCourses()).thenReturn(List.of(math));

        mockMvc.perform(get("/api/v1/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Matematica"));
    }

    @Test
    void getCourse_returnsCourse_whenExists() throws Exception {
        Course math = new Course("Matematica", "MAT101", "Corso base", 6);
        math.setId(1L);

        when(courseFinder.getCourseById(1L)).thenReturn(math);

        mockMvc.perform(get("/api/v1/courses/{courseId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Matematica"));
    }

    @Test
    void getCourse_returns404_whenNotFound() throws Exception {
        when(courseFinder.getCourseById(99L)).thenThrow(new CourseNotFoundException(99L));

        mockMvc.perform(get("/api/v1/courses/{courseId}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCourse_returns201_whenValid() throws Exception {
        CourseRequestDTO request = new CourseRequestDTO("Matematica", "MAT101", "Corso base", 6);

        Course created = new Course("Matematica", "MAT101", "Corso base", 6);
        created.setId(1L);

        when(coursePostService.createCourse(any(Course.class))).thenReturn(created);

        mockMvc.perform(post("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Matematica"));
    }

    @Test
    void createCourse_returns400_whenNameIsBlank() throws Exception {
        CourseRequestDTO request = new CourseRequestDTO("", "MAT101", "Corso base", 6);

        mockMvc.perform(post("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCourse_returns200_whenValid() throws Exception {
        CourseRequestDTO request = new CourseRequestDTO("Matematica Avanzata", "MAT202", "Corso avanzato", 9);

        Course updated = new Course("Matematica Avanzata", "MAT202", "Corso avanzato", 9);
        updated.setId(1L);

        when(coursePutService.updateCourse(eq(1L), any(Course.class))).thenReturn(updated);

        mockMvc.perform(put("/api/v1/courses/{courseId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Matematica Avanzata"));
    }

    @Test
    void updateCourse_returns404_whenCourseNotFound() throws Exception {
        CourseRequestDTO request = new CourseRequestDTO("Matematica Avanzata", "MAT202", "Corso avanzato", 9);

        when(coursePutService.updateCourse(eq(99L), any(Course.class))).thenThrow(new CourseNotFoundException(99L));

        mockMvc.perform(put("/api/v1/courses/{courseId}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCourse_returns204() throws Exception {
        mockMvc.perform(delete("/api/v1/courses/{courseId}", 1L))
                .andExpect(status().isNoContent());
    }
}
