package com.example.springproject.controller;

import com.example.springproject.Model.Status;
import com.example.springproject.Model.Users;
import com.example.springproject.service.CourseServices;
import com.example.springproject.service.StudentServices;
import com.example.springproject.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DetailsController {

    @Autowired
    private StudentServices studentServices;


    @GetMapping(value="/studentDetails/{id}",consumes = "application/json")
    public ResponseEntity<?> login(@PathVariable String id) {
        var rs=studentServices.getDetailsById(id);

        if(rs.isPresent())
            return ResponseEntity.ok(rs.get());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
}
