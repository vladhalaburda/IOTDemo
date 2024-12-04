package com.vladhalaburda.iotdemo.service.zigbee;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vladhalaburda.iotdemo.model.ZigbeeDevice;
import com.vladhalaburda.iotdemo.repository.ZigbeeRepository;

@Service
public class ZigbeeService {
    @Autowired
    private ZigbeeRepository zigbeeRepository;

    public void addDevice(String name, String type, Map<String, Object> parameters, String location) {
        ZigbeeDevice zigbeeDevice = new ZigbeeDevice();
        zigbeeDevice.setName(name);
        zigbeeDevice.setType(type);
        zigbeeDevice.setParameters(parameters);
        zigbeeDevice.setLocation(location);

        zigbeeRepository.save(zigbeeDevice);
        System.out.println("Device saved to MongoDB: " + zigbeeDevice);
    }
}
