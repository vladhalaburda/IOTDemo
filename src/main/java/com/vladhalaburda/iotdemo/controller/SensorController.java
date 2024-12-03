package com.vladhalaburda.iotdemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vladhalaburda.iotdemo.model.SensorModel;
import com.vladhalaburda.iotdemo.repository.SensorRepository;

@RestController
@RequestMapping("/api/sensors")
public class SensorController {
    @Autowired
    private SensorRepository sensorRepository;

    @GetMapping
    public List<SensorModel> getAllSensors() {
        return sensorRepository.findAll();
    }

    @PostMapping
    public SensorModel createSensor(@RequestBody SensorModel sensor) {
        return sensorRepository.save(sensor);
    }

    @GetMapping("/{id}")
    public SensorModel getSensorById(@PathVariable String id) {
        return sensorRepository.findById(id).orElseThrow(() -> new RuntimeException("Sensor not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteSensor(@PathVariable String id) {
        sensorRepository.deleteById(id);
    }



}
