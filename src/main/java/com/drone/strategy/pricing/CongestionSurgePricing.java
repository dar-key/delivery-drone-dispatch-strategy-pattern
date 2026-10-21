package com.drone.strategy.pricing;

import com.drone.model.DeliveryRequest;
import com.drone.sensor.AirspaceSensorFeed;

public class CongestionSurgePricing implements DeliveryPricingStrategy {
    private final AirspaceSensorFeed sensor;

    public CongestionSurgePricing(AirspaceSensorFeed sensor) {
        if (sensor == null) {
            throw new IllegalArgumentException("Sensor feed cannot be null");
        }
        this.sensor = sensor;
    }

    @Override
    public double calculatePrice(DeliveryRequest request) {
        double congestion = sensor.getCongestionMultiplier();
        return 12.0 + (request.distanceKm() * 2.0 * congestion) + (request.weightKg() * 0.5);
    }
}
