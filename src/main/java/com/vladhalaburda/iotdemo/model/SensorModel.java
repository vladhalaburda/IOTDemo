package com.vladhalaburda.iotdemo.model;


import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;

@Document(collection="sensors")
public class SensorModel {
    @Id
    private Long id;
    private String name;
    private String type; // temperature 
    private String status;

    private String connectionType; // wifi, mqtt, and more

    private Map<String, String> connectionParams;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public Map<String, String> getConnectionParams() {
        return connectionParams;
    }

    public void setConnectionParams(Map<String, String> connectionParams) {
        this.connectionParams = connectionParams;
    }
}
