package com.example.springproject.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "instructors")
public class Instructor {
    @Id
    @Column(name = "instructor_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @JsonBackReference
    private Courses course;

    @Column(name = "schedule")
    private String schedule;

    @Column(name = "classroom", length = 100)
    private String classroom;

    @Lob
    @Column(name = "lecture_notes")
    private String lectureNotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    @JsonBackReference
    private AdminUser admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    @JsonBackReference
    private Batches batch;

}