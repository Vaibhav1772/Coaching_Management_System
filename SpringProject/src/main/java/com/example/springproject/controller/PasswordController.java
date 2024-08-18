package com.example.springproject.controller;

import com.example.springproject.Model.Status;
import com.example.springproject.Model.Users;
import com.example.springproject.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Component
@RestController
public class PasswordController {

    @Autowired
    UserServices userServices;

    @PutMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestBody Users user) {
         Integer val=userServices.updatePassword(user);
         if(val!=-1)
                return ResponseEntity.ok(new Status(val.toString(), HttpStatus.OK));
         else
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Status("-1",HttpStatus.NOT_FOUND));
    }
}
