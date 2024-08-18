package com.example.springproject.controller;

import com.example.springproject.Model.Students;
import com.example.springproject.service.StudentServices;
import com.example.springproject.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
public class MainActivity {

    private StudentServices studentServices;
    @Autowired
    private LoginController login;
    @Autowired
    private UserServices userServices;

    public MainActivity(StudentServices studentServices) {
        this.studentServices = studentServices;
    }
//    @GetMapping("/message")
//    public Message message() {
//        return new Message("Hello Vaibhav");
//    }
    @GetMapping("/getById")
    public Optional<Students>getById() {
        return null;
    }

}
