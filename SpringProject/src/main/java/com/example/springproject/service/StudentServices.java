package com.example.springproject.service;

import com.example.springproject.Model.Status;
import com.example.springproject.Model.Students;
import com.example.springproject.respository.StudentsQueries;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Component
@NoArgsConstructor
@AllArgsConstructor
public class StudentServices {

    @Autowired
    private StudentsQueries query;

    public Optional<Students> getDetailsById(String id){

        return query.findById(Integer.parseInt(id));
    }
//    public Integer provideStudent(Students students) {
//
//        return (query.createStudent(students.getName(),students.getEmail(),students.getPhone(),students.getUsers().getId()).get());
//    }

    public Optional<Integer> saveStudent(Students students) {
        return query.createStudent(students.getName(),students.getEmail(),students.getPhone(),students.getUsers().getId(), String.valueOf(students.getDateOfBirth()),students.getAddress());
    }
}
