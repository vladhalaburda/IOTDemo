package com.vladhalaburda.iotdemo.service.zigbee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladhalaburda.iotdemo.model.ZigbeeDevice;
import com.vladhalaburda.iotdemo.repository.ZigbeeRepository;
// 172.27.132.179
@Service
public class ZigbeeMqttEmulator {
    private static final String BROKER = "tcp://localhost:1883";
    private static final String BASE_TOPIC = "zigbee2mqtt/";
    private final Map<String, ZigbeeDevice> devices = new HashMap<>();
    private final Random random = new Random();
    private MqttClient mqttClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ZigbeeRepository deviceRepository;


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
        deviceRepository.save(device);
        // devices.put(device.getId(), device);

    }

    public ZigbeeDevice enableDevice(String deviceId) {
        // Ищем устройство в базе
        ZigbeeDevice device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("Device not found with ID: " + deviceId));

        // Устанавливаем состояние в true
        device.setState(true);
        deviceRepository.save(device);

        return device;
    }

    public void removeDevice(String deviceId) {
        devices.remove(deviceId);
    }

    public List<ZigbeeDevice> deviceList() {
        return deviceRepository.findAll();

    }

    public void startSimulation() {
        // Периодически обновляем данные для всех устройств
        new Thread(() -> {
            while (true) {
                try {
                    List<ZigbeeDevice> devices = deviceRepository.findAll();
                    for (ZigbeeDevice device : devices) {
                        if (device.isState()) { // Только включенные устройства
                            publishData(device);
                        }
                    }
                    Thread.sleep(5000); // Интервал публикации данных
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void publishData(ZigbeeDevice device) {
        Map<String, Object> data = new HashMap<>(device.getParameters());
        data.put("state", device.isState());

        // Генерация случайных значений для температуры и влажности
        if (device.getType().equals("temperature")) {
            data.put("temperature", 20 + random.nextDouble() * 10);
        } else if (device.getType().equals("humidity")) {
            data.put("humidity", 30 + random.nextDouble() * 20);
        }
        data.put("topic", BASE_TOPIC + device.getId());
    
        try {
            // Преобразование данных в JSON-строку
            String jsonData = objectMapper.writeValueAsString(data);
    
            // Публикация сообщения в формате JSON
            mqttClient.publish(BASE_TOPIC + device.getId(), new MqttMessage(jsonData.getBytes()));
            System.out.println("Published data for " + BASE_TOPIC + device.getId() + ": " + jsonData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startSimulationWithRanges(Map<String, Map<String, Double>> ranges) {
        List<ZigbeeDevice> devices = deviceRepository.findAll();

        for (ZigbeeDevice device : devices) {
            if (device.isState()) {
                double min = ranges.getOrDefault(device.getName(), Map.of("min", 0.0, "max", 100.0)).get("min");
                double max = ranges.getOrDefault(device.getName(), Map.of("min", 0.0, "max", 100.0)).get("max");

                simulateDevice(device, min, max);
            }
        }
    }


    private void simulateDevice(ZigbeeDevice device, double min, double max) {
        new Thread(() -> {
            while (device.isState()) {
                try {
                    Map<String, Object> data = new HashMap<>(device.getParameters());
                    data.put("state", device.isState());

                    if ("temperature".equals(device.getType())) {
                        data.put("temperature", min + random.nextDouble() * (max - min));
                    } else if ("humidity".equals(device.getType())) {
                        data.put("humidity", min + random.nextDouble() * (max - min));
                    }
                    data.put("topic", BASE_TOPIC + device.getId());

                    String jsonData = objectMapper.writeValueAsString(data);

                    System.out.println("Published data for " + BASE_TOPIC + device.getId() + ": " + jsonData);
                    mqttClient.publish("zigbee2mqtt/" + device.getId(), new MqttMessage(jsonData.getBytes()));
                    Thread.sleep(5000); // Публикация каждые 5 секунд
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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