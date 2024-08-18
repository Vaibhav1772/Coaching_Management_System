package com.example.springproject.service;

import com.example.springproject.Model.Courses;
import com.example.springproject.respository.CoursesQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServices {

    @Autowired
    private CoursesQueries query;

    public Optional<Courses>getCourseById(String id){
        return query.findById(Integer.parseInt(id));
    }

    public Optional<List<Courses>> getAllCourses(){
        return query.findAllCourses();
    }
}
