package com.example.myapplication.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamResult {

    private Integer id;

    private BigDecimal marksObtained;

    private String grade;

    private String remarks;

    private Exams exam;

    private Users user;

    private Integer correctAns;

    private Courses course;

    private Integer wrongAns;

    private Integer unattempted;

    private BigDecimal totalMarks;

    private BigDecimal averageMarks;

}