package com.example.springproject.controller;

import com.example.springproject.Model.Courses;
import com.example.springproject.respository.CoursesQueries;
import com.example.springproject.service.CourseServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CoursesController {

    @Autowired
    private CourseServices courseServices;

    @GetMapping(value = "/courses/all", consumes = "application/json")
    public ResponseEntity<?> getAllCourses() {
        var rs= courseServices.getAllCourses();
        if (rs.isPresent())
            return ResponseEntity.ok(rs.get().stream().toList());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Courses not found");
    }
    @GetMapping(value = "/courses/{id}",consumes = "application/json")
    public ResponseEntity<?> getCourseDetails(@PathVariable String id) {
        var rs=courseServices.getCourseById(id);
        if (rs.isPresent())
            return ResponseEntity.ok(rs.get());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Courses not found");
    }
}
