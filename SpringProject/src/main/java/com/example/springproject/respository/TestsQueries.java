package com.example.springproject.respository;

import com.example.springproject.Model.ExamResult;
import com.example.springproject.Model.Tests;
import org.springframework.data.annotation.Transient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TestsQueries extends JpaRepository<Tests,Integer> {


    @Query(value = "SELECT * FROM tests where course_id=:course_id AND ques_no=:ques_no",nativeQuery = true)
    Optional<Tests> fetchQuestions(Integer course_id,Integer ques_no);

    @Query(value="SELECT COUNT(test_id) AS count_ques FROM tests WHERE course_id=:course_id",nativeQuery = true)
    Optional<Integer> countQuestionsByCourse(Integer course_id);

    @Modifying
    @Transactional
    @Query(value="UPDATE tests SET user_ans=:user_ans WHERE course_id=:course_id AND ques_no=:ques_no",nativeQuery = true)
    Optional<Integer>updateAnsweredQuestions(@Param("course_id") Integer course_id, @Param("ques_no")
    Integer ques_no, @Param("user_ans") String user_ans);

    @Query(value="SELECT COUNT(correct_ans) AS correct FROM tests WHERE course_id=:course_id AND test_id=:test_id AND user_ans=correct_ans ",nativeQuery = true)
    Optional<Integer> countCorrectAnswers(@Param("course_id") Integer course_id, @Param("test_id") Integer test_id);

    @Query(value="SELECT COUNT(correct_ans) AS correct FROM tests WHERE course_id=:course_id AND test_id=:test_id AND user_ans<>correct_ans ",nativeQuery = true)
    Optional<Integer> countWrongAnswers(@Param("course_id") Integer course_id, @Param("test_id") Integer test_id);

    @Query(value="SELECT COUNT(correct_ans) AS unapttempted FROM tests WHERE course_id=:course_id  AND test_id=:test_id AND user_ans IS NULL",nativeQuery = true)
    Optional<Integer> countUnattemptedQuestions(@Param("course_id") Integer course_id, @Param("test_id") Integer test_id);


}
