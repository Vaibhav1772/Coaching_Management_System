package com.example.myapplication.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tests {

    private Integer id;
    private Courses course;

    private String question;

    private String optA;

    private String optB;

    private String optC;

    private String optD;

    private String correctAns;

    private String userAns;

}