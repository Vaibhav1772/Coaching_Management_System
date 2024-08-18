package com.example.myapplication.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lecture {

    private Integer lecture_id;

    private Batches batch;

    private String lectureLink;

}
