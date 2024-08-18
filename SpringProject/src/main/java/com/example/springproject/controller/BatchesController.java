package com.example.springproject.controller;

import com.example.springproject.service.BatchesServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BatchesController {

    @Autowired
    private BatchesServices batchesServices;

    @GetMapping(value = "/batches/{id}", consumes = "application/json")
    public ResponseEntity<?>getBatchesById(@PathVariable String id){
        var rs=batchesServices.getBatchesById(id);
        if(rs.isPresent())
            return ResponseEntity.ok(rs.get());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Batches not found");
    }

    @GetMapping(value = "/batches/all/{id}", consumes = "application/json")
    public ResponseEntity<?>getAllBatches(@PathVariable String id){
        var rs=batchesServices.getAllBatchesEnrolled(id);
        if(rs.isPresent())
            return ResponseEntity.ok(rs.get());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Batches not found");
    }
    @GetMapping(value = "/batches/all", consumes = "application/json")
    public ResponseEntity<?>getAllBatches(){
        var rs=batchesServices.getAllBatches();
        if(rs.isPresent())
            return ResponseEntity.ok(rs.get());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Batches not found");
    }
}
