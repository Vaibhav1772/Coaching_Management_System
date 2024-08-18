package com.example.springproject.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "courses")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Courses {
    @Id
    @Column(name = "course_id", nullable = false)
    private Integer id;

    @Column(name = "course_name", length = 100)
    private String courseName;

    @Lob
    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "syllabus")
    private String syllabus;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "fee_structure", precision = 10, scale = 2)
    private BigDecimal feeStructure;

    @Lob
    @Column(name = "instructors")
    private String instructors;

    @OneToMany(mappedBy = "course")
    @JsonManagedReference(value = "course-batches")
    private Set<Batches> batches = new LinkedHashSet<>();

    @OneToMany(mappedBy = "course")
    @JsonManagedReference(value = "course-doubts")
    private Set<Doubts> doubts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "course")
    @JsonManagedReference(value = "course-exams")
    private Set<Exams> exams = new LinkedHashSet<>();

    @OneToMany(mappedBy = "course")
    @JsonManagedReference(value = "course-tests")
    private Set<Tests> tests = new LinkedHashSet<>();

}