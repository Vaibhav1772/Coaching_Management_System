package com.example.myapplication.Model;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Batches {

    private Integer id;

    private String startDate;

    private String endDate;

    private String schedule;

    private String instructors;

    private String classroom;

    private Courses course;

    private Set<Attendance> attendances = new LinkedHashSet<>();

    private Set<Doubts> doubts = new LinkedHashSet<>();

    private Set<Enrollment> enrollments = new LinkedHashSet<>();

    private Lecture lectures;

}

