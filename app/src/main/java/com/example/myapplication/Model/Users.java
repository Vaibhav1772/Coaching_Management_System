package com.example.myapplication.Model;

import java.util.LinkedHashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    private Integer id;
    private String username;
    private String password;
    private String role;

    private Set<Attendance> attendances = new LinkedHashSet<>();


    private Set<Doubts> doubts = new LinkedHashSet<>();


    private Set<Enrollment> enrollments = new LinkedHashSet<>();


    private Set<ExamResult> examResults = new LinkedHashSet<>();


    private Set<Notification> notifications = new LinkedHashSet<>();


    private Set<Payment> payments = new LinkedHashSet<>();


    private Students student;

    public Users(Integer idx, String usr, String pass, String userRole) {
        this.id = idx;
        this.username = usr;
        this.password = pass;
        this.role = userRole;
    }
}

