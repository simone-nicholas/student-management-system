package com.company.studentmanagementsystem.students;

import com.company.studentmanagementsystem.books.model.Book;
import com.company.studentmanagementsystem.courses.model.Course;
import com.company.studentmanagementsystem.exceptions.StudentNotFoundException;
import com.company.studentmanagementsystem.students.dto.StudentRequestDTO;
import com.company.studentmanagementsystem.students.model.Student;
import com.company.studentmanagementsystem.students.service.StudentDeleteService;
import com.company.studentmanagementsystem.students.service.StudentGetService;
import com.company.studentmanagementsystem.students.service.StudentPostService;
import com.company.studentmanagementsystem.students.service.StudentPutService;
import tools.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper objectMapper;

    @MockitoBean
    private StudentPostService studentPostService;

    @MockitoBean
    private StudentGetService studentGetService;

    @MockitoBean
    private StudentPutService studentPutService;

    @MockitoBean
    private StudentDeleteService studentDeleteService;

    @Test
    void getAllStudents_returnsListOfStudents() throws Exception {
        Student mario = new Student("Mario", "Rossi", "mario.rossi@email.com", "1234567890");
        mario.setId(1L);

        when(studentGetService.findAll()).thenReturn(List.of(mario));

        mockMvc.perform(get("/api/v1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Mario"));
    }

    @Test
    void getStudentById_returnsStudent_whenExists() throws Exception {
        Student mario = new Student("Mario", "Rossi", "mario.rossi@email.com", "1234567890");
        mario.setId(1L);

        when(studentGetService.findById(1L)).thenReturn(mario);

        mockMvc.perform(get("/api/v1/students/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mario"));
    }

    @Test
    void getStudentById_returns404_whenNotFound() throws Exception {
        when(studentGetService.findById(99L)).thenThrow(new StudentNotFoundException(99L));

        mockMvc.perform(get("/api/v1/students/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCourses_returnsCoursesOfStudent() throws Exception {
        Course math = new Course("Matematica", "MAT101", "Corso base", 6);
        math.setId(10L);

        when(studentGetService.getCoursesFromStudent(1L)).thenReturn(List.of(math));

        mockMvc.perform(get("/api/v1/students/{studentId}/courses", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Matematica"));
    }

    @Test
    void getAllBooksByStudentId_returnsBooksOfStudent() throws Exception {
        Book book = new Book("Cyber Security", "Autore", "1234567890123", BigDecimal.TEN, LocalDate.of(2020, 1, 1));
        book.setId(20L);

        when(studentGetService.getStudentBooks(1L)).thenReturn(List.of(book));

        mockMvc.perform(get("/api/v1/students/{studentId}/books", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Cyber Security"));
    }

    @Test
    void assignCourseToStudent_returns200() throws Exception {
        Course math = new Course("Matematica", "MAT101", "Corso base", 6);
        math.setId(10L);

        when(studentPostService.assignCourseToStudent(1L, 10L)).thenReturn(math);

        mockMvc.perform(post("/api/v1/students/{studentId}/courses/{courseId}", 1L, 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Matematica"));
    }

    @Test
    void assignBookToStudent_returns200() throws Exception {
        Book book = new Book("Cyber Security", "Autore", "1234567890123", BigDecimal.TEN, LocalDate.of(2020, 1, 1));
        book.setId(20L);

        when(studentPostService.assignBookToStudent(1L, 20L)).thenReturn(book);

        mockMvc.perform(post("/api/v1/students/{studentId}/books/{bookId}", 1L, 20L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Cyber Security"));
    }

    @Test
    void createStudent_returns201WithLocation_whenValid() throws Exception {
        StudentRequestDTO request = new StudentRequestDTO("Mario", "Rossi", "mario.rossi@email.com", "1234567890");

        Student created = new Student("Mario", "Rossi", "mario.rossi@email.com", "1234567890");
        created.setId(1L);

        when(studentPostService.create(any(Student.class))).thenReturn(created);

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/api/v1/students/1")))
                .andExpect(jsonPath("$.name").value("Mario"));
    }

    @Test
    void createStudent_returns400_whenEmailIsInvalid() throws Exception {
        StudentRequestDTO request = new StudentRequestDTO("Mario", "Rossi", "not-an-email", "1234567890");

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateStudent_returns200_whenValid() throws Exception {
        StudentRequestDTO request = new StudentRequestDTO("Luigi", "Verdi", "luigi.verdi@email.com", "0987654321");

        Student updated = new Student("Luigi", "Verdi", "luigi.verdi@email.com", "0987654321");
        updated.setId(1L);

        when(studentPutService.update(eq(1L), any(Student.class))).thenReturn(updated);

        mockMvc.perform(put("/api/v1/students/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Luigi"));
    }

    @Test
    void updateStudent_returns404_whenNotFound() throws Exception {
        StudentRequestDTO request = new StudentRequestDTO("Luigi", "Verdi", "luigi.verdi@email.com", "0987654321");

        when(studentPutService.update(eq(99L), any(Student.class))).thenThrow(new StudentNotFoundException(99L));

        mockMvc.perform(put("/api/v1/students/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteStudent_returns204() throws Exception {
        mockMvc.perform(delete("/api/v1/students/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void removeCourseFromStudent_returns204() throws Exception {
        mockMvc.perform(delete("/api/v1/students/{studentId}/courses/{courseId}", 1L, 10L))
                .andExpect(status().isNoContent());
    }

    @Test
    void removeBookFromStudent_returns204() throws Exception {
        mockMvc.perform(delete("/api/v1/students/{studentId}/books/{bookId}", 1L, 20L))
                .andExpect(status().isNoContent());
    }
}
