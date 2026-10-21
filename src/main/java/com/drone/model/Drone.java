package com.drone.model;

public record Drone(
        String id,
        String modelName,
        double maxSpeedKmh,
        double energyPerKmKwh,
        double totalFlightHours) {
}
