package com.vladhalaburda.iotdemo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vladhalaburda.iotdemo.model.ZigbeeDevice;
import com.vladhalaburda.iotdemo.service.zigbee.ZigbeeMqttEmulator;
import com.vladhalaburda.iotdemo.service.zigbee.ZigbeeService;

@RestController
@RequestMapping("/api/zagbee")
public class ZagbeeController {
    
    private final ZigbeeMqttEmulator zigbeeMqttEmulator;
    private final ZigbeeService zigbeeService;

    public ZagbeeController(ZigbeeMqttEmulator zigbeeMqttEmulator, ZigbeeService zigbeeService) {
        this.zigbeeMqttEmulator = zigbeeMqttEmulator;
        this.zigbeeService = zigbeeService;
    }

    @PostMapping("/add_device")
    public ResponseEntity<String> addDevice(@RequestBody ZigbeeDevice device) {
        zigbeeMqttEmulator.addDevice(device);
        // zigbeeService.addDevice(device.getName(), device.getType(), device.getParameters(), device.getLocation());
        return ResponseEntity.ok("Sensor added: " + device.getName() + ": "+ device.getLocation());
    }

    @GetMapping("/list_device")
    public List<ZigbeeDevice> deviceList() {
        return zigbeeMqttEmulator.deviceList();
    }

    @GetMapping("/start_sim")
    public ResponseEntity<String> startSimulation() {
        zigbeeMqttEmulator.startSimulation();
        return ResponseEntity.ok("Simulation for all devices was started");
    }

    @PostMapping("/enable/{id}")
    public ResponseEntity<String> enableDevice(@PathVariable String id) {
        try {
            ZigbeeDevice updatedDevice = zigbeeMqttEmulator.enableDevice(id);
            return ResponseEntity.ok("Device " + updatedDevice.getName() + " enabled successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}
