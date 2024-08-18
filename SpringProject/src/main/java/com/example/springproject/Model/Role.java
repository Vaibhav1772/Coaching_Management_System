package com.example.springproject.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    private Integer id;

    @Column(name = "role_name", length = 50)
    private String roleName;

    @Lob
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "role")
    private Set<Permission> permissions = new LinkedHashSet<>();

}