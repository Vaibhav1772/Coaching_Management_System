package com.example.springproject.service;

import com.example.springproject.Model.Courses;
import com.example.springproject.Model.Lecture;
import com.example.springproject.respository.CoursesQueries;
import com.example.springproject.respository.LectureQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LectureServices {

    @Autowired
    private LectureQueries query;

    public Optional<Lecture> getLectureById(String id){
        return query.findById(Integer.parseInt(id));
    }
}
