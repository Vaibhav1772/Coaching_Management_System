package com.example.springproject.Model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "students")
public class Students {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id", nullable = false)
    private Integer id;

    @Column(name = "name", length = 50)
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;

    @Lob
    @Column(name = "payment_history")
    private String paymentHistory;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonManagedReference(value = "user-students")
    private Users users;

}