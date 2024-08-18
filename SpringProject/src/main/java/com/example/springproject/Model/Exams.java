package com.example.springproject.Model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;


@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "exams")
public class Exams {
    @Id
    @Column(name = "exam_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @JsonBackReference(value = "course-exams")
    private Courses course;

    @Column(name = "exam_date")
    private LocalDate examDate;

    @Column(name = "exam_type", length = 50)
    private String examType;

    @Lob
    @Column(name = "topics")
    private String topics;

    @Lob
    @Column(name = "grading_criteria")
    private String gradingCriteria;

    @OneToMany(mappedBy = "exam")
    @JsonManagedReference(value = "exam-examResults")
    private Set<ExamResult> examResults = new LinkedHashSet<>();

}