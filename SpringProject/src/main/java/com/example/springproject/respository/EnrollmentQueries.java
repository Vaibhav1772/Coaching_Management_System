package com.example.springproject.respository;

import com.example.springproject.Model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentQueries extends JpaRepository<Enrollment,Integer> {

    @Query(value = "SELECT * FROM enrollments WHERE user_id=:user_id", nativeQuery = true)
    Optional<List<Enrollment>>getEnrollmentByUserId(Integer user_id);

    @Query(value = "SELECT * FROM enrollments ", nativeQuery = true)
    Optional<List<Enrollment>>findAllEnrollments();

    @Modifying
    @Transactional
    @Query(value="INSERT INTO enrollments (user_id,batch_id,course_id,enrollment_status,enrollment_date) VALUES (:user_id,:batch_id,:course_id,:enrollment_status,:enrollment_date)",nativeQuery = true)
    Optional<Integer>insertEnrollment(Integer user_id,Integer batch_id,Integer course_id,String enrollment_status,String enrollment_date);

}
