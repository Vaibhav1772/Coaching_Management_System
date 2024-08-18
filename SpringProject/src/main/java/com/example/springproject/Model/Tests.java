package com.example.springproject.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "tests")
public class Tests {
    @Id
    @Column(name = "test_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @JsonBackReference(value = "course-tests")
    private Courses course;

    @Column(name = "question", length = 100)
    private String question;

    @Lob
    @Column(name = "opt_a")
    private String optA;

    @Lob
    @Column(name = "opt_b")
    private String optB;

    @Lob
    @Column(name = "opt_c")
    private String optC;

    @Lob
    @Column(name = "opt_d")
    private String optD;

    @Lob
    @Column(name = "correct_ans")
    private String correctAns;

    @Lob
    @Column(name = "user_ans")
    private String userAns;

}