package com.example.myapplication.Model;

import java.util.LinkedHashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Courses {

    private Integer id;

    private String courseName;

    private String description;

    private String syllabus;

    private Integer duration;

    private Double feeStructure;

    private String instructors;

    private Set<Batches> batches = new LinkedHashSet<>();

    private Set<Doubts> doubts = new LinkedHashSet<>();

    private Set<Exams> exams = new LinkedHashSet<>();

    private Set<Tests> tests = new LinkedHashSet<>();

}