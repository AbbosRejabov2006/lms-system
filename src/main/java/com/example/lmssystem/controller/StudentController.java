package com.example.lmssystem.controller;

import com.example.lmssystem.dto.StudentDTO;
import com.example.lmssystem.entity.User;
import com.example.lmssystem.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<?> getAllStudents(@RequestBody StudentDTO studentDTO) {
        User student = studentService.checkUser(studentDTO);
        if (studentService.chekCourse(student , studentDTO.courseId())) {
            studentService.add(student , studentDTO.courseId());
        }
        return ResponseEntity.ok("");
    }
    /*@GetMapping
    public ResponseEntity<?> getStudents(@RequestBody StudentDTO studentDTO) {
        User student = studentService.checkUser(studentDTO);
    }*/
}
