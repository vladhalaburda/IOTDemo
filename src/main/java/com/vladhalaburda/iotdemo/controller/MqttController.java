package com.vladhalaburda.iotdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vladhalaburda.iotdemo.service.mqtt.MqttService;

@RestController
@RequestMapping("/api/mqtt")
public class MqttController {
    @Autowired
    private MqttService mqttService;

    @PostMapping("/publish")
    public String publishMessage(@RequestParam String topic, @RequestParam String message) {
        mqttService.publish(topic, message);
        return "Message was published";
    }

}
