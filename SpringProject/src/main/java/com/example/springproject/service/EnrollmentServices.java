package com.example.springproject.service;

import com.example.springproject.Model.Batches;
import com.example.springproject.Model.Enrollment;
import com.example.springproject.respository.EnrollmentQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentServices {

    @Autowired
    private EnrollmentQueries query;

    @Autowired
    private BatchesServices batchesServices;

    public Optional<List<Enrollment>>getEnrollments(String id){
        return query.getEnrollmentByUserId(Integer.parseInt(id));
    }

    public Optional<List<Enrollment>> getAllEnrollments() {
        return query.findAllEnrollments();
    }

    public Optional<Integer> insertEnrollment(String user_id,String course_id) {
        Optional<List<Batches>> batchesList=batchesServices.getBatchesById(course_id);
        return query.insertEnrollment(Integer.parseInt(user_id), batchesList.get().getFirst().getId(),Integer.parseInt(course_id), "SUCCESS", String.valueOf(LocalDate.now()));
    }
}
