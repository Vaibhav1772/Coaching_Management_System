package com.example.springproject.service;

import com.example.springproject.Model.Batches;
import com.example.springproject.Model.Enrollment;
import com.example.springproject.respository.BatchesQueries;
import com.mysql.cj.x.protobuf.MysqlxCursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BatchesServices {

    @Autowired
    private BatchesQueries query;

    @Autowired
    private UserServices userServices;

    public Optional<List<Batches>> getBatchesById(String id){
        return query.findBatchesById(Integer.parseInt(id));
    }

    public Optional<Set<Batches>> getAllBatchesEnrolled(String id) {
        var rs=query.findAllBatches();
        Set<Batches> batches = new LinkedHashSet<>();
        var result=userServices.getUser(id);
        if(result.isPresent()){
            var user=result.get();
            Set<Enrollment> enrollmentSet = new LinkedHashSet<>(user.getEnrollments());
            for(Enrollment e:enrollmentSet){
                for(var b:rs.get()){
                    if(b.getEnrollments().contains(e)){
                        batches.add(b);
                    }
                }
            }
        }
        return Optional.of(batches);
    }


    public Optional<Set<Batches>> getAllBatches() {
        return query.findAllBatches();
    }
}
