package com.example.springproject.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @Column(name = "notification_id", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "message")
    private String message;

    @Column(name = "date_time")
    private Instant dateTime;

    @Column(name = "read_status")
    private Boolean readStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-notifications")
    private Users user;

}