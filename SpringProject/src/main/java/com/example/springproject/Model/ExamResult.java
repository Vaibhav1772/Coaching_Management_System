package com.example.springproject.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exam_results")
public class ExamResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id", nullable = false)
    private Integer id;

    @Column(name = "marks_obtained", precision = 5, scale = 2)
    private BigDecimal marksObtained;

    @Column(name = "grade", length = 10)
    private String grade;


    @Lob
    @Column(name = "remarks")
    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    @JsonBackReference(value = "exam-examResults")
    private Exams exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-examResults")
    private Users user;


    @Column(name = "correct_ans")
    private Integer correctAns;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @JsonBackReference(value = "course-examResults")
    private Courses course;

    @Column(name = "wrong_ans")
    private Integer wrongAns;

    @Column(name = "unattempted")
    private Integer unattempted;

    @Column(name = "total_marks", precision = 5, scale = 2, columnDefinition = "decimal(5,2) default 10.00")
    private BigDecimal totalMarks;

    @Column(name = "average_marks", precision = 5, scale = 2)
    private BigDecimal averageMarks;

}