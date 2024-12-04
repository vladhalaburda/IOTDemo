package com.vladhalaburda.iotdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vladhalaburda.iotdemo.model.ZigbeeDevice;
import com.vladhalaburda.iotdemo.service.zigbee.ZigbeeMqttEmulator;

@RestController
@RequestMapping("/api/zagbee")
public class ZagbeeController {
    @Autowired
    private ZigbeeMqttEmulator zigbeeMqttEmulator;

    @PostMapping("/add_device")
    public String addDevice(@RequestParam ZigbeeDevice device) {
        zigbeeMqttEmulator.addDevice(device);
        return "OK";
    }

}
