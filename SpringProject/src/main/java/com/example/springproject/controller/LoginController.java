package com.example.springproject.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.springproject.Model.Status;
import com.example.springproject.Model.Users;
import com.example.springproject.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Component
@RestController
public class LoginController {

    @Autowired
    private UserServices userServices;

    @GetMapping(value = "/loginDetails/{username}",consumes = "application/json")
    public ResponseEntity<?> login(@PathVariable  String username) {
        var rs=userServices.findUser(username);

        if(rs.isPresent())
            return ResponseEntity.ok(new Status(rs.get().getId().toString(),HttpStatus.OK));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
    @PostMapping(value = "/login")
    public ResponseEntity<?> checkLogin(@RequestBody Users user) {
        var rs=userServices.findUser(user.getUsername());
        if(rs.isPresent()) {
            System.out.println(rs.get().getPassword());
            System.out.println(user.getPassword());
            BCrypt.Result result=BCrypt.verifyer().verify(user.getPassword().toCharArray(),rs.get().getPassword().toCharArray());
            System.out.println(result.verified);
            if(result.verified)
                return ResponseEntity.ok(new Status(rs.get().getId().toString(),HttpStatus.OK));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Status("-1",HttpStatus.UNAUTHORIZED));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }


    @GetMapping(value = "/users/{id}",consumes = "application/json")
    public ResponseEntity<?>getUserId(@PathVariable String id) {
        var rs=userServices.getUser(id);
        if(rs.isPresent()){
            return ResponseEntity.ok(rs.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
}
