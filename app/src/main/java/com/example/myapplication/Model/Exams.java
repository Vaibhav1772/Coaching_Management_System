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
public class Exams {

    private Integer id;

    private Courses course;

    private String examDate;

    private String examType;

    private String topics;

    private String gradingCriteria;

    private Set<ExamResult> examResults = new LinkedHashSet<>();

}