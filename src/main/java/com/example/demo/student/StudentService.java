package com.example.demo.student;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public void addNewStudents(Student student) {
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
        if(studentOptional.isPresent()) { throw new IllegalStateException("Email already exists!"); }
        studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        boolean exists = studentRepository.existsById(id);
        if(!exists) {
            throw new IllegalStateException("Student with Id: " + id + " doesn't exist!");
        }
        studentRepository.deleteById(id);
    }
    @Transactional
    public void updateStudent(Long id, String name, String email) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new IllegalStateException("student with id: " + id + " doesn't exist") );
        if(name != null && name.length() > 0 && !Objects.equals(student.getName(), name)) {
            student.setName(name);
        }
        if(email != null && email.length() > 0 && !Objects.equals(student.getName(), email)) {
            Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);
            if(studentOptional.isPresent()) {
                throw new IllegalStateException("Email is taken!");
            }
            student.setEmail(email);
        }
    }
}
