package com.company.studentmanagementsystem;

import com.company.studentmanagementsystem.books.dto.BookRequestDTO;
import com.company.studentmanagementsystem.courses.dto.CourseRequestDTO;
import com.company.studentmanagementsystem.students.dto.StudentRequestDTO;
import tools.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class StudentManagementSystemIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper objectMapper;

    private Long createStudent(String name, String surname, String email, String phone) throws Exception {
        StudentRequestDTO request = new StudentRequestDTO(name, surname, email, phone);

        MvcResult result = mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }

    private Long createCourse(String name, String code, String description, int credits) throws Exception {
        CourseRequestDTO request = new CourseRequestDTO(name, code, description, credits);

        MvcResult result = mockMvc.perform(post("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }

    private Long createBook(String title, String author, String isbn) throws Exception {
        BookRequestDTO request = new BookRequestDTO(title, author, isbn, BigDecimal.valueOf(19.99), LocalDate.of(2021, 6, 15));

        MvcResult result = mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }

    @Test
    void fullStudentLifecycle_createAssignRetrieveAndDelete() throws Exception {
        Long studentId = createStudent("Mario", "Rossi", "mario.rossi@email.com", "1234567890");
        Long courseId = createCourse("Matematica", "MAT101", "Corso base di matematica", 6);
        Long bookId = createBook("Cyber Security", "J. Doe", "1234567890123");

        mockMvc.perform(post("/api/v1/students/{studentId}/courses/{courseId}", studentId, courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Matematica"));

        mockMvc.perform(post("/api/v1/students/{studentId}/books/{bookId}", studentId, bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Cyber Security"));

        mockMvc.perform(get("/api/v1/students/{studentId}/courses", studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].code").value("MAT101"));

        mockMvc.perform(get("/api/v1/students/{studentId}/books", studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].isbn").doesNotExist());

        mockMvc.perform(get("/api/v1/books/{bookId}/owner", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("mario.rossi@email.com"));

        mockMvc.perform(get("/api/v1/courses/{courseId}/students", courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Mario"));

        StudentRequestDTO updateRequest = new StudentRequestDTO("Mario", "Rossi", "mario.new@email.com", "1234567890");
        mockMvc.perform(put("/api/v1/students/{id}", studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("mario.new@email.com"));

        mockMvc.perform(delete("/api/v1/students/{studentId}/courses/{courseId}", studentId, courseId))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/v1/students/{studentId}/books/{bookId}", studentId, bookId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/students/{studentId}/courses", studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        mockMvc.perform(delete("/api/v1/students/{id}", studentId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/students/{id}", studentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createStudent_returns400_whenRequestIsInvalid() throws Exception {
        StudentRequestDTO invalidRequest = new StudentRequestDTO("", "Rossi", "not-an-email", "abc");

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getStudentById_returns404_whenStudentDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/students/{id}", 999999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCourseById_returns404_whenCourseDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/courses/{courseId}", 999999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBookById_returns404_whenBookDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/books/{bookId}", 999999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCourse_removesCourseAndAssociationWithStudent() throws Exception {
        Long studentId = createStudent("Luigi", "Verdi", "luigi.verdi@email.com", "0987654321");
        Long courseId = createCourse("Fisica", "FIS101", "Corso base di fisica", 5);

        mockMvc.perform(post("/api/v1/students/{studentId}/courses/{courseId}", studentId, courseId))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/v1/courses/{courseId}", courseId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/students/{studentId}/courses", studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        mockMvc.perform(get("/api/v1/courses/{courseId}", courseId))
                .andExpect(status().isNotFound());
    }
}
