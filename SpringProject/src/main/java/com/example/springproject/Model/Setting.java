package com.example.springproject.Model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "settings")
public class Setting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "setting_id", nullable = false)
    private Integer id;

    @Column(name = "setting_name", length = 50)
    private String settingName;

    @Column(name = "value")
    private String value;

    @Lob
    @Column(name = "description")
    private String description;

}