package com.java.studenthandler.controller;

import com.java.studenthandler.entity.Student;
import com.java.studenthandler.exception.IDNotFoundException;
import com.java.studenthandler.exception.MandatoryFieldMissingException;
import com.java.studenthandler.exception.StudentListEmptyException;
import com.java.studenthandler.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController

public class StudentController {


    StudentRepository repo;

    @Autowired
    StudentController(StudentRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/index")
    public  String renderIndex(){
        return "index";
    }

    @GetMapping("/home")
    public  String renderHome(){
        return "homepage";
    }

    @GetMapping("/students")
    public List<Student> getAllStudents() {
        List<Student> students = repo.findAll();
        if(students.isEmpty()) {
            throw new StudentListEmptyException("No student present in the system");
        } else {
            return students;
        }
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudentByID(@PathVariable int id) {
        Optional<Student> student = repo.findById(id);
        if(student.isPresent()) {
            return ResponseEntity.status(HttpStatus.FOUND).body(student.get());
        } else {
            throw new IDNotFoundException("Student ID is not present in the system");
        }

    }

    @PostMapping("/students/add")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        if(student.getName().isEmpty()) {
            throw new MandatoryFieldMissingException("Name field is mandatory");
        }
        Student savedStudent=repo.save(student);
        return ResponseEntity.ok(savedStudent);
    }

    @PutMapping("/students/update/{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public ResponseEntity<Student> updateStudent(@PathVariable int id) {
        Optional<Student> studentOptional = repo.findById(id);
        if (studentOptional.isPresent()) {
            Student actualStudent = studentOptional.get();
            actualStudent.setAge(100);
            Student updatedStudent = repo.save(actualStudent);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedStudent);
        } else {
            throw new IDNotFoundException("Student ID is not present in the system");
        }
    }


    @DeleteMapping("/students/delete/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable int id) {
        Optional<Student> student = repo.findById(id);
        if(student.isPresent()) {
            repo.delete(student.get());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deletion successfull");
        } else {
            throw new IDNotFoundException("Student ID is not present in the system");
        }

    }
}
