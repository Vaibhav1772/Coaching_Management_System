package com.example.springproject.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "lectures")
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id", nullable = false)
    private Integer lecture_id;

    @OneToOne(mappedBy = "lectures")
    @JsonManagedReference(value = "batch-lecture")
    private Batches batch;

    @Lob
    @Column(name = "lecture_link")
    private String lectureLink;

}