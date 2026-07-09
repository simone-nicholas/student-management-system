package com.company.studentmanagementsystem.students;

import com.company.studentmanagementsystem.books.Book;
import com.company.studentmanagementsystem.books.BookRepository;
import com.company.studentmanagementsystem.courses.Course;
import com.company.studentmanagementsystem.courses.CourseRepository;
import com.company.studentmanagementsystem.exceptions.BookNotAssignedException;
import com.company.studentmanagementsystem.exceptions.BookNotFoundException;
import com.company.studentmanagementsystem.exceptions.CourseNotFoundException;
import com.company.studentmanagementsystem.exceptions.StudentNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final BookRepository bookRepository;

    public StudentService(StudentRepository studentRepository, BookRepository bookRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
    }

    @Transactional(readOnly = true)
    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
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

    @Transactional(readOnly = true)
    public List<Course> getCoursesFromStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
        return student.getCourses();
    }

    @Transactional(readOnly = true)
    public List<Book> getStudentBooks(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));

        return student.getBooks();
    }

    @Transactional
    public Book assignBookToStudent(Long studentId, Long bookId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
        Book book = getBookById(bookId);

        Student previousStudent = book.getStudent();

        if (student.equals(book.getStudent())) {
            return book;
        }

        if (previousStudent != null) {
            previousStudent.getBooks().remove(book);
        }

        book.setStudent(student);

        if (!student.getBooks().contains(book)) {
            student.getBooks().add(book);
        }

        return bookRepository.save(book);
    }

    @Transactional
    public Course assignCourseToStudent(Long studentId, Long courseId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
        Course course = getCourseById(courseId);

        if (!student.getCourses().contains(course)) {
            student.getCourses().add(course);
        }

        if (!course.getStudents().contains(student)) {
            course.getStudents().add(student);
        }

        studentRepository.save(student);
        courseRepository.save(course);

        return course;
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

    @Transactional
    public void removeCourseFromStudent(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
        Course course = getCourseById(courseId);

        student.getCourses().remove(course);
        course.getStudents().remove(student);

        studentRepository.save(student);
        courseRepository.save(course);
    }

    @Transactional
    public void removeBookFromStudent(Long studentId, Long bookId) {
        Book book = getBookById(bookId);

        if (book.getStudent() == null ||
                !book.getStudent().getId().equals(studentId)) {
            throw new BookNotAssignedException(bookId, studentId);
        }

        book.getStudent().getBooks().remove(book);
        book.setStudent(null);
        bookRepository.save(book);
    }
}
