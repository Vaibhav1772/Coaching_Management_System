package com.example.springproject.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.springproject.Model.Status;
import com.example.springproject.Model.Students;
import com.example.springproject.Model.Users;
import com.example.springproject.respository.StudentsQueries;
import com.example.springproject.service.StudentServices;
import com.example.springproject.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
public class CreateController {

    @Autowired
    private UserServices userServices;

    @Autowired
    private StudentServices studentServices;
    @Autowired
    private StudentsQueries studentsQueries;

    @PostMapping(value = "/createUser",consumes = "application/json")
    public ResponseEntity<?> createUser(@RequestBody Users user){


        var rs=userServices.insertUser(user.getUsername(),user.getPassword());

        if(rs>-1)
            return ResponseEntity.ok(new Status(rs.toString(),HttpStatus.OK));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not created");
    }
    @PostMapping(value = "/createStudent",consumes = "application/json")
    public ResponseEntity<?> createStudent(@RequestBody Students students) {

        var rs=studentServices.saveStudent(students);
        if(rs.get()>0)
            return ResponseEntity.ok(new Status("1",HttpStatus.OK));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Student not created");
    }
}
