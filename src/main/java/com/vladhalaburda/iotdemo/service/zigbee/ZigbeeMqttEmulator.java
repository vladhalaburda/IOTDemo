package com.vladhalaburda.iotdemo.service.zigbee;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

import com.vladhalaburda.iotdemo.model.ZigbeeDevice;

@Service
public class ZigbeeMqttEmulator {
    private static final String BROKER = "tcp://localhost:1883";
    private static final String BASE_TOPIC = "zigbee2mqtt/";
    private final Map<String, ZigbeeDevice> devices = new HashMap<>();
    private final Random random = new Random();
    private MqttClient mqttClient;

    public ZigbeeMqttEmulator() throws MqttException {
        mqttClient = new MqttClient(BROKER, MqttClient.generateClientId());
        mqttClient.connect();
        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                System.err.println("Connection lost: " + cause.getMessage());
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                handleCommand(topic, new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                // No-op
            }
        });
        mqttClient.subscribe(BASE_TOPIC + "+/set");
    }

    public void addDevice(ZigbeeDevice device) {
        devices.put(device.getId(), device);
    }

    public void removeDevice(String deviceId) {
        devices.remove(deviceId);
    }

    public Map<String, ZigbeeDevice>  deviceList() {
        return devices;
    }

    public void startSimulation() {
        new Thread(() -> {
            while (true) {
                for (ZigbeeDevice device : devices.values()) {
                    publishData(device);
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }

    private void publishData(ZigbeeDevice device) {
        Map<String, Object> data = new HashMap<>(device.getParameters());
        data.put("state", device.isState());
        if (device.getType().equals("temperature")) {
            data.put("temperature", 20 + random.nextDouble() * 10);
        } else if (device.getType().equals("humidity")) {
            data.put("humidity", 30 + random.nextDouble() * 20);
        }
        try {
            mqttClient.publish(BASE_TOPIC + device.getName(), new MqttMessage(data.toString().getBytes()));
            System.out.println("Published data for " + device.getName() + ": " + data);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void handleCommand(String topic, String payload) {
        String deviceId = topic.split("/")[1];
        ZigbeeDevice device = devices.get(deviceId);
        if (device == null) {
            System.err.println("Device not found: " + deviceId);
            return;
        }
        if (payload.contains("\"state\":\"ON\"")) {
            device.setState(true);
        } else if (payload.contains("\"state\":\"OFF\"")) {
            device.setState(false);
        }
        System.out.println("Updated state for " + deviceId + ": " + device.isState());
    }
}