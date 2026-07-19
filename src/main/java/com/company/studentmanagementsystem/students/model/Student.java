package com.company.studentmanagementsystem.students.model;

import com.company.studentmanagementsystem.books.model.Book;
import com.company.studentmanagementsystem.courses.model.Course;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"books", "courses"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String surname;

    @NotBlank
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String phoneNumber;

    @NotBlank
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @OneToMany(
            mappedBy = "student",
            fetch = FetchType.LAZY
    )
    @JsonManagedReference
    private List<Book> books = new ArrayList<>();


    @ManyToMany(
            mappedBy = "students",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Course> courses = new ArrayList<>();
}
