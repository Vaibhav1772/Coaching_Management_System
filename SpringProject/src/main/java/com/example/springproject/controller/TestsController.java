package com.example.springproject.controller;

import com.example.springproject.Model.Status;
import com.example.springproject.service.TestsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestsController {

    @Autowired
    private TestsServices testsServices;

    @GetMapping(value = "/tests/questions/{id}/{ques_no}",consumes = "application/json")
    public ResponseEntity<?> getTestsQuestions(@PathVariable ("id")String id, @PathVariable("ques_no") String ques_no){
        var rs=testsServices.getTestsQuestions(id,ques_no);
        if(rs.isPresent())
            return ResponseEntity.ok(rs.get());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Tests not found");
    }

    @GetMapping(value = "/tests/count/{id}",consumes = "application/json")
    public ResponseEntity<?> getQuestionsCount(@PathVariable String id) {
        var rs=testsServices.getQuestionsCount(id);
        if(rs.isPresent())
            return ResponseEntity.ok(new Status(rs.get().toString(),HttpStatus.OK));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tests Question not found");
    }

    @PutMapping(value = "/tests/answered/{course_id}/{ques_no}/{user_ans}",consumes = "application/json")
    public ResponseEntity<?> getAnsweredQuestions(@PathVariable String course_id, @PathVariable String ques_no, @PathVariable String user_ans) {
        var rs=testsServices.setAnsweredQuestions(course_id,ques_no,user_ans);
        if(rs.isPresent())
            return ResponseEntity.ok(new Status(rs.get().toString(),HttpStatus.OK));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Status("-1",HttpStatus.NOT_FOUND));
    }



}
