package com.company.studentmanagementsystem.students;

import com.company.studentmanagementsystem.students.model.Student;
import com.company.studentmanagementsystem.students.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private StudentGetService studentGetService;

    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private StudentPostService studentPostService;

    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private StudentPutService studentPutService;

    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private StudentDeleteService studentDeleteService;

    @Test
    void getAllStudents_returnsListOfStudents() throws Exception {
        Student mario = new Student();
        mario.setId(1L);
        mario.setName("Mario");
        mario.setSurname("Rossi");
        mario.setEmail("mario.rossi@email.com");

        when(studentGetService.findAll()).thenReturn(List.of(mario));

        mockMvc.perform(get("/api/v1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Mario"))
                .andExpect(jsonPath("$[0].surname").value("Rossi"));
    }
}