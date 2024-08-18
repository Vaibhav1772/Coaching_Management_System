package com.example.springproject.respository;

import com.example.springproject.Model.ExamResult;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ResultsQueries extends JpaRepository<ExamResult,Integer> {

    @Query(value = "SELECT * FROM exam_results WHERE course_id=:course_id AND user_id=:user_id AND exam_id=:exam_id", nativeQuery = true)
    Optional<ExamResult> getStudentResults(@Param("user_id") Integer user_id, @Param("course_id") Integer course_id, @Param("exam_id") Integer exam_id);

    @Modifying
    @Query(value = "INSERT INTO exam_results (user_id,course_id,exam_id,correct_ans,wrong_ans,unattempted,average,total) VALUES (:user_id,:course_id,:exam_id,:correct_ans,:wrong_ans,:unattempted,:average,:total)", nativeQuery = true)
    Optional<Integer> setStudentResults(@Param("user_id") Integer user_id, @Param("course_id") Integer course_id, @Param("exam_id") Integer exam_id, @Param("correct_ans") Integer correct_ans, @Param("wrong_ans") Integer wrong_ans, @Param("unattempted") Integer unattempted, @Param("average") BigDecimal average, @Param("total") Integer total);

    @Modifying
    @Transactional
    @Query(value = "UPDATE exam_results SET average_marks=:average,correct_ans=:correct_ans,wrong_ans=:wrong_ans,unattempted=:unattempted,total_marks=:total,remarks=:remarks,grade=:grade,marks_obtained=:marks_obtained WHERE user_id=:user_id AND course_id=:course_id AND exam_id=:exam_id",nativeQuery = true)
    Optional<Integer> updateExamDetails(@Param("user_id") Integer user_id, @Param("course_id") Integer course_id, @Param("exam_id") Integer exam_id, @Param("correct_ans") Integer correct_ans, @Param("wrong_ans") Integer wrong_ans, @Param("unattempted") Integer unattempted, @Param("average") BigDecimal average, @Param("total") Integer total,
                                        @Param("grade") String grade, @Param("marks_obtained") Integer marks_obtained,
                                        @Param("remarks") String remarks);

    @Modifying
    @Transactional
    @Query(value="INSERT INTO exam_results (user_id,course_id,exam_id,correct_ans,wrong_ans,unattempted,average_marks,total_marks,grade,marks_obtained,remarks) VALUES (:user_id,:course_id,:exam_id,:correct_ans,:wrong_ans,:unattempted,:average,:total,:grade,:marks_obtained,:remarks)",nativeQuery = true)
    Optional<Integer> insertExamDetails(@Param("user_id") Integer user_id, @Param("course_id") Integer course_id, @Param("exam_id") Integer exam_id, @Param("correct_ans") Integer correct_ans, @Param("wrong_ans") Integer wrong_ans, @Param("unattempted") Integer unattempted, @Param("average") BigDecimal average, @Param("total") Integer total,
                                        @Param("grade") String grade, @Param("marks_obtained") Integer marks_obtained,
                                        @Param("remarks") String remarks);




    @Query(value = "SELECT COUNT(exam_id) FROM exam_results WHERE user_id=:user_id AND course_id=:course_id AND exam_id=:exam_id",nativeQuery = true)
    Optional<Integer> fetchExamDetails(@Param("user_id") Integer user_id, @Param("course_id") Integer course_id, @Param("exam_id") Integer exam_id);

}
