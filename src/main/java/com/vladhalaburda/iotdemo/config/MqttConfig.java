package com.vladhalaburda.iotdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vladhalaburda.iotdemo.service.mqtt.MqttService;

@Configuration
public class MqttConfig {
    @Bean
    public MqttService mqttService() {
        return new MqttService();
    }
}
