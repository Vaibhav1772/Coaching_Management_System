package com.example.springproject.respository;

import com.example.springproject.Model.Students;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface StudentsQueries extends JpaRepository<Students,Long> {

    @Query(value="SELECT * FROM students WHERE user_id = :id",nativeQuery = true)
    Optional<Students> findById(Integer id);

    @Modifying
    @Transactional
    @Query(value="INSERT INTO students (name,email,phone,user_id,date_of_birth,address) VALUES (:name,:email,:phone,:user_id,:dob,:address)",nativeQuery = true)
    Optional<Integer> createStudent(String name, String email, String phone, Integer user_id, String dob, String address);

}
