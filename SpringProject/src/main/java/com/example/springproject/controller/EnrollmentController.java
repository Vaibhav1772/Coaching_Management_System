package com.example.springproject.controller;


import com.example.springproject.Model.Status;
import com.example.springproject.service.EnrollmentServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class EnrollmentController {

    @Autowired
    private EnrollmentServices service;

    @GetMapping(value = "/enrollments/{id}",consumes = "application/json")
    public ResponseEntity<?>getEnrollments(@PathVariable String id){
        var rs=service.getEnrollments(id);
        if(rs.isPresent()){
            return ResponseEntity.ok(rs.get());
        }
        else
            return ResponseEntity.status(NOT_FOUND).body("No enrollments found");
    }
    @GetMapping(value = "/enrollments/all",consumes = "application/json")
    public ResponseEntity<?>getAllEnrollments(){
        var rs=service.getAllEnrollments();
        if(rs.isPresent()){
            return ResponseEntity.ok(rs.get());
        }
        else
            return ResponseEntity.status(NOT_FOUND).body("No enrollments found");
    }

    @PostMapping(value = "/enrollments/{user_id}/{course_id}",consumes = "application/json")
    public ResponseEntity<?>insertEnrollment(@PathVariable String user_id,@PathVariable String course_id){
        var rs=service.insertEnrollment(user_id,course_id);
        if(rs.isPresent()){
            return ResponseEntity.ok(new Status(rs.get().toString(),OK));
        }
        else
            return ResponseEntity.status(NOT_FOUND).body("Enrollment not created");
    }
}
