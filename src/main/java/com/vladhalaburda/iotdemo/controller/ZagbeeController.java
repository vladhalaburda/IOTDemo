package com.vladhalaburda.iotdemo.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vladhalaburda.iotdemo.model.ZigbeeDevice;
import com.vladhalaburda.iotdemo.service.zigbee.ZigbeeMqttEmulator;

@RestController
@RequestMapping("/api/zagbee")
public class ZagbeeController {
    
    private final ZigbeeMqttEmulator zigbeeMqttEmulator;
    public ZagbeeController(ZigbeeMqttEmulator zigbeeMqttEmulator) {
        this.zigbeeMqttEmulator = zigbeeMqttEmulator;
    }

    @PostMapping("/add_device")
    public ResponseEntity<String> addDevice(@RequestBody ZigbeeDevice device) {
        zigbeeMqttEmulator.addDevice(device);
        return ResponseEntity.ok("Sensor added: " + device.getName());
    }

    @GetMapping("/list_device")
    public Map<String, ZigbeeDevice> deviceList() {
        return zigbeeMqttEmulator.deviceList();
    }


}
