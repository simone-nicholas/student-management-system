package com.company.studentmanagementsystem.students;

import com.company.studentmanagementsystem.books.Book;
import com.company.studentmanagementsystem.books.BookRepository;
import com.company.studentmanagementsystem.courses.Course;
import com.company.studentmanagementsystem.exceptions.BookNotFoundException;
import com.company.studentmanagementsystem.exceptions.StudentNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final BookRepository bookRepository;

    public StudentService(StudentRepository studentRepository, BookRepository bookRepository) {
        this.studentRepository = studentRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Student findById(Long id){
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Transactional
    public Student create(Student student) {
        return studentRepository.save(student);
    }

    @Transactional
    public Student update(Long id, Student student) {
        Student existingStudent = findById(id);

        if(student.getName() != null)
            existingStudent.setName(student.getName());
        if(student.getSurname() != null)
            existingStudent.setSurname(student.getSurname());
        if(student.getEmail() != null)
            existingStudent.setEmail(student.getEmail());
        if(student.getPhoneNumber() != null)
            existingStudent.setPhoneNumber(student.getPhoneNumber());

        return existingStudent;
    }

    @Transactional
    public void deleteById(Long id) {
        Student student = findById(id);

        for (Course course : List.copyOf(student.getCourses())) {
            course.getStudents().remove(student);
        }

        student.getCourses().clear();

        for (Book book : student.getBooks()) {
            book.setStudent(null);
        }

        student.getBooks().clear();

        studentRepository.delete(student);
    }
}
