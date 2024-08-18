package com.example.springproject.controller;

import com.example.springproject.Model.*;
import com.example.springproject.service.ResultsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class ResultsController {

    @Autowired
    private ResultsServices resultsServices;

    @GetMapping(value="/getStudentResults/{user_id}/{course_id}/{test_id}",consumes = "application/json")
    public ResponseEntity<?> getStudentResults(@PathVariable("user_id") String user_id,@PathVariable("course_id") String course_id,@PathVariable("test_id") String test_id){
        var rs=resultsServices.getStudentResults(user_id,course_id,test_id);
        Users user=new Users();
        user.setId(Integer.parseInt(user_id));
        Courses course=new Courses();
        course.setId(Integer.parseInt(course_id));
        Exams exam=new Exams();
        exam.setId(Integer.parseInt(test_id));
        if(rs.isPresent())
        return ResponseEntity.ok(new ExamResult(rs.get().getId(),rs.get().getMarksObtained(),
                rs.get().getGrade(),rs.get().getRemarks(),exam,user,rs.get().getCorrectAns(),
                course,rs.get().getWrongAns(),rs.get().getUnattempted(),
                rs.get().getTotalMarks(),rs.get().getAverageMarks()));
        else
            return ResponseEntity.status(NOT_FOUND).body("No results found");
    }

    @PostMapping(value = "/tests/updateResult/{user_id}/{course_id}/{test_id}",consumes = "application/json")
    public ResponseEntity<?> insertExamDetails(@PathVariable String user_id,@PathVariable String course_id, @PathVariable String test_id) {
        var rs=resultsServices.insertExamDetails(user_id,course_id,test_id);
        return rs.map(integer -> ResponseEntity.ok(new Status(integer.toString(), HttpStatus.OK))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Status("-1", HttpStatus.NOT_FOUND)));
    }
}
