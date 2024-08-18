package com.example.springproject.respository;

import com.example.springproject.Model.Lecture;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LectureQueries extends JpaRepository<Lecture,Integer> {

    @Query(value = "SELECT * FROM lectures WHERE batch_id = :batch_id",nativeQuery = true)
    Optional<Lecture>getLecturesById(Integer batch_id);


}
