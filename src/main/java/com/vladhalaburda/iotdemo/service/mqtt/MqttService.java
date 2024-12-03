package com.vladhalaburda.iotdemo.service.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.tinylog.Logger;

public class MqttService {

    private final String brokerUrl = "tcp://localhost:1883";
    private final String clientId = "smart-home-server";
    private MqttClient client;

    public MqttService() {
        try {
            client = new MqttClient(brokerUrl, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            client.connect(options);
            Logger.info("[MQTT] Mqtt was connected");
        } catch (MqttException e) {
            Logger.error("[MQTT] Mqtt was not connected");
            e.printStackTrace();
        }
    }

    public void publish(String topic, String message) {
        try {
            client.publish(topic, new MqttMessage(message.getBytes()));
            Logger.info("[MQTT] Message was published");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(String topic) {
        try {
            client.subscribe(topic, (topic1, message) -> {
                Logger.info("[MQTT] Received message: "+ new String(message.getPayload())+ " from playload: "+ topic1);
            });
        } catch(MqttException e) {
            e.printStackTrace();
        }
    }


}
