package com.example.springproject.service;

import com.example.springproject.Model.ExamResult;
import com.example.springproject.respository.ResultsQueries;
import com.example.springproject.respository.TestsQueries;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Service
public class ResultsServices {

    @Autowired
    private ResultsQueries query;

    @Autowired
    private TestsQueries testsQueries;

    public Optional<ExamResult> getStudentResults(String user_id, String course_id, String test_id) {
        return query.getStudentResults(Integer.parseInt(user_id), Integer.parseInt(course_id), Integer.parseInt(test_id));
    }

    public Optional<Integer> updateExamDetails(int i, int i1, int i2, Integer correctAns, Integer wrongAns, Integer unattempted, BigDecimal average, int total, String grade, int i3, String remarks) {

        return query.updateExamDetails(i, i1, i2, correctAns, wrongAns, unattempted, average, total, grade, i3, remarks);
    }

    public Optional<Integer> fetchExamDetails(int i, int i1, int i2) {
        return query.fetchExamDetails(i, i1, i2);
    }

    public Optional<Integer> insertExamDetails(String userId, String courseId, String testId) {
        int correct_ans = 0, wrong_ans = 0, unattempted = 0;
        correct_ans = testsQueries.countCorrectAnswers(Integer.parseInt(courseId), Integer.parseInt(testId)).get();
        wrong_ans = testsQueries.countWrongAnswers(Integer.parseInt(courseId), Integer.parseInt(testId)).get();
        unattempted = testsQueries.countUnattemptedQuestions(Integer.parseInt(courseId), Integer.parseInt(testId)).get();

        int total = correct_ans + wrong_ans + unattempted;

        BigDecimal average = BigDecimal.valueOf((correct_ans * 100L) / total, 2);

        String grade = getGrades(average);
        String remarks = average.compareTo(BigDecimal.valueOf(0.50)) > 0 ? "Pass" : "Fail";
        if (fetchExamDetails(Integer.parseInt(userId), Integer.parseInt(courseId), Integer.parseInt(testId)).get() == 0)
            return query.insertExamDetails(Integer.parseInt(userId), Integer.parseInt(courseId), Integer.parseInt(testId), correct_ans, wrong_ans,
                    unattempted, average, total*2, grade,
                    correct_ans * 2, remarks);

        return query.updateExamDetails(Integer.parseInt(userId), Integer.parseInt(courseId), Integer.parseInt(testId), correct_ans, wrong_ans,
                unattempted, average, total*2, grade,
                correct_ans * 2, remarks);
    }


    public String getGrades(BigDecimal average) {

        if(average.compareTo(BigDecimal.valueOf(0.90))>=0)
            return "A";
        else if(average.compareTo(BigDecimal.valueOf(0.80))>=0)
            return "B";
        else if(average.compareTo(BigDecimal.valueOf(0.70))>=0)
            return "C";
        else if(average.compareTo(BigDecimal.valueOf(0.60))>=0)
            return "D";
        else if(average.compareTo(BigDecimal.valueOf(0.50))>=0)
            return "E";
        else
            return "F";

    }

    public static BigDecimal getBigDecimal(Integer correct_ans, long total) {
        System.out.println(total);
        return BigDecimal.valueOf(((correct_ans)* 2L*100L)/(total),2);
    }

}
