package com.drone.sensor;

public class SimulatedAirspaceSensor implements AirspaceSensorFeed {
    @Override
    public double getCongestionMultiplier() {
        return 1.6; // Simulates 60% surge in busy airspace
    }
}
