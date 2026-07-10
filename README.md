# Student Management System

A RESTful Student Management System built with Spring Boot, Spring Data JPA and PostgreSQL.

## Overview

This project is a backend application that exposes REST APIs to manage students, books and courses.

The main goal is to practice backend development using modern Java technologies and learn how to build a layered Spring Boot application following best practices.

## Technologies

- Java 21+ (or your current version)
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Hibernate
- Jakarta Validation
- Maven

## Features

### Students

- Create a student
- Retrieve all students
- Retrieve a student by ID
- Update a student
- Delete a student

### Books

- Create a book
- Retrieve all books
- Retrieve a book by ID
- Update a book
- Delete a book

### Courses

- Create a course
- Retrieve all courses
- Retrieve a course by ID
- Update a course
- Delete a course

### Relationships

#### Student → Books (One-to-Many)

- Assign a book to a student
- Retrieve all books of a student
- Retrieve the owner of a book
- Remove a book from a student

#### Students ↔ Courses (Many-to-Many)

- Enroll a student in a course
- Retrieve all students enrolled in a course
- Retrieve all courses of a student
- Remove a student from a course

## Project Structure

```
src
└── main
    ├── java
    │   └── com.company.studentmanagementsystem
    │       ├── students
    │       ├── books
    │       ├── courses
    │       ├── exceptions
    │       └── configuration
    └── resources
        └── application.properties
```

## REST Endpoints

### Students

| Method | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/v1/students` | Create a student |
| GET | `/api/v1/students` | Get all students |
| GET | `/api/v1/students/{id}` | Get student by ID |
| PUT | `/api/v1/students/{id}` | Update student |
| DELETE | `/api/v1/students/{id}` | Delete student |

### Books

| Method | Endpoint |
|---------|----------|
| POST | `/api/v1/books` |
| GET | `/api/v1/books` |
| GET | `/api/v1/books/{id}` |
| PUT | `/api/v1/books/{id}` |
| DELETE | `/api/v1/books/{id}` |

### Courses

| Method | Endpoint |
|---------|----------|
| POST | `/api/v1/courses` |
| GET | `/api/v1/courses` |
| GET | `/api/v1/courses/{id}` |
| PUT | `/api/v1/courses/{id}` |
| DELETE | `/api/v1/courses/{id}` |

### Relationships

| Method | Endpoint |
|---------|----------|
| POST | `/api/v1/students/{studentId}/books/{bookId}` |
| GET | `/api/v1/books/{bookId}/owner` |
| GET | `/api/v1/students/{studentId}/books` |
| DELETE | `/api/v1/students/{studentId}/books/{bookId}` |
| POST | `/api/v1/students/{studentId}/courses/{courseId}` |
| GET | `/api/v1/courses/{courseId}/students` |
| GET | `/api/v1/students/{studentId}/courses` |
| DELETE | `/api/v1/students/{studentId}/courses/{courseId}` |

## Database

PostgreSQL is used as the relational database.

Example configuration:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/student_management
spring.datasource.username=postgres
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## Validation

The project uses Jakarta Bean Validation.

Examples:

- `@NotBlank`
- `@Email`
- `@Pattern`

## Learning Goals

This project is intended to practice:

- Spring Boot
- REST API development
- Spring Data JPA
- Hibernate relationships
- Layered Architecture
- Dependency Injection
- Transaction Management
- Validation
- Exception Handling
- PostgreSQL
- Docker

## Author

Nicholas Simone
