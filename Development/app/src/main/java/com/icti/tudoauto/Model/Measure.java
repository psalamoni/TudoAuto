package com.icti.tudoauto.Model;

public class Measure {

    private String fueltype;
    private float volume;
    private float distance;
    private float measureavg;

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
}
