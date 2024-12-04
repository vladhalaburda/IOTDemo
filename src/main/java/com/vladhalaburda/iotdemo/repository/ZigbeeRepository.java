package com.vladhalaburda.iotdemo.repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.vladhalaburda.iotdemo.model.ZigbeeDevice;

public interface ZigbeeRepository extends MongoRepository<ZigbeeDevice, String> {

}
