package com.icti.tudoauto.Model;

public class Measure {

    private String fueltype;
    private float volume;
    private float distance;
    private float measureavg;
    private Position position;
    private long timestamp;

    public Measure() {
    }

    public String getFueltype() {
        return fueltype;
    }

    public void setFueltype(String fueltype) {
        this.fueltype = fueltype;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getMeasureavg() {
        return measureavg;
    }

    public void setMeasureavg(float measureavg) {
        this.measureavg = measureavg;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
