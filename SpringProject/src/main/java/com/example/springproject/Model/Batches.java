package com.example.springproject.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "batches")
public class Batches {
    @Id
    @Column(name = "batch_id", nullable = false)
    private Integer id;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "schedule")
    private String schedule;

    @Lob
    @Column(name = "instructors")
    private String instructors;

    @Column(name = "classroom", length = 100)
    private String classroom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @JsonBackReference(value = "course-batches")
    private Courses course;

    @OneToMany(mappedBy = "batch")
    @JsonManagedReference(value = "batch-attendance")
    private Set<Attendance> attendances = new LinkedHashSet<>();

    @OneToMany(mappedBy = "batch")
    @JsonManagedReference(value = "batch-doubts")
    private Set<Doubts> doubts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "batch", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "batch-enrollments")
    private Set<Enrollment> enrollments = new LinkedHashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    @JsonBackReference(value = "batch-lecture")
    private Lecture lectures;

}