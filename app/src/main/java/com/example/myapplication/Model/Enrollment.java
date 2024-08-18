package com.example.myapplication.Model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {

    private Integer id;

    private String enrollmentDate;

    private String enrollmentStatus;

    private Batches batch;

    private Users user;

    private Courses course;
}