package com.example.springproject.controller;

import com.example.springproject.service.CourseServices;
import com.example.springproject.service.LectureServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LectureController {

    @Autowired
    private LectureServices lectureServices;


    @GetMapping(value = "/lecture/{id}", consumes = "application/json")
    public ResponseEntity<?> getAllCourses(@PathVariable String id) {
        var rs= lectureServices.getLectureById(id);
        if (rs.isPresent())
            return ResponseEntity.ok(rs.get());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lecture not found");
    }
}
