package com.example.springproject.respository;

import com.example.springproject.Model.Batches;
import com.example.springproject.Model.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BatchesQueries extends JpaRepository<Batches,Integer> {

    @Query(value = "SELECT * FROM batches WHERE course_id=:course_id",nativeQuery = true)
    Optional<List<Batches>> findBatchesById(@Param("course_id") Integer id);

    @Query(value = "SELECT * FROM batches",nativeQuery = true)
    Optional<Set<Batches>> findAllBatches();
}
