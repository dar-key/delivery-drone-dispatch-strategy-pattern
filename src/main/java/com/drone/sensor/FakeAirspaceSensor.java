package com.drone.sensor;

public class FakeAirspaceSensor implements AirspaceSensorFeed {
    private double multiplier;

    public FakeAirspaceSensor(double multiplier) {
        this.multiplier = multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public double getCongestionMultiplier() {
        return multiplier;
    }
}
