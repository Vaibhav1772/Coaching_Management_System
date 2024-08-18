package com.example.springproject.service;

import com.example.springproject.Model.Tests;
import com.example.springproject.respository.TestsQueries;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Component
@NoArgsConstructor
@AllArgsConstructor
public class TestsServices {

    @Autowired
    private TestsQueries query;

    @Autowired
    private ResultsServices services;

    public Optional<Tests> getTestsQuestions(String course_id, String ques_no) {
        return query.fetchQuestions(Integer.parseInt(course_id), Integer.parseInt(ques_no));
    }

    public Optional<Integer> getQuestionsCount(String course_id) {
        return query.countQuestionsByCourse(Integer.parseInt(course_id));
    }

    public Optional<Integer> setAnsweredQuestions(String course_id, String ques_no, String user_ans) {
        return query.updateAnsweredQuestions(Integer.parseInt(course_id), Integer.parseInt(ques_no), user_ans);
    }
}
