package com.example.myapplication.Model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Students {

    private Integer id;

    private String name;

    private String dateOfBirth;

    private String phone;

    private String email;

    private String address;

    private LocalDate enrollmentDate;

    private String paymentHistory;

    private Users users;
}
