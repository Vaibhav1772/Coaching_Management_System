package com.example.springproject.respository;

import com.example.springproject.Model.Courses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoursesQueries extends JpaRepository<Courses,Integer> {

    @Query(value = "SELECT * FROM courses WHERE course_id=:course_id",nativeQuery = true)
    Optional<Courses>findById(Integer course_id);

    @Query(value = "SELECT * FROM courses",nativeQuery = true)
    Optional<List<Courses>>findAllCourses();

}
