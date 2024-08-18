package com.example.springproject.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admin_users")
public class AdminUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id", nullable = false)
    private Integer id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "username", length = 50)
    private String username;

    @Column(name = "password_hash", length = 100)
    private String passwordHash;

    @Column(name = "role", length = 20)
    private String role;

    @OneToMany(mappedBy = "admin")
    private Set<Instructor> instructors = new LinkedHashSet<>();

    @OneToMany(mappedBy = "admin")
    private Set<Log> logs = new LinkedHashSet<>();


}