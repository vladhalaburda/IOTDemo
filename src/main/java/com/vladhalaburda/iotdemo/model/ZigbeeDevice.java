package com.vladhalaburda.iotdemo.model;

import java.util.Map;

public class ZigbeeDevice {
    private String id;
    private String name;
    private String type;
    private boolean state;
    private Map<String, Object> parameters;
    private String location;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public boolean isState() { return state; }
    public void setState(boolean state) { this.state = state; }
    public Map<String, Object> getParameters() { return parameters; }
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
