package com.vladhalaburda.iotdemo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vladhalaburda.iotdemo.model.SensorModel;

public interface SensorRepository extends MongoRepository<SensorModel, String> {
    
}
