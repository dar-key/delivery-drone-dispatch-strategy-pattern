package com.drone.pricing;

import com.drone.model.DeliveryRequest;
import com.drone.sensor.AirspaceSensorFeed;

public class PricingCalculator {
    private final AirspaceSensorFeed sensor;

    public PricingCalculator(AirspaceSensorFeed sensor) {
        this.sensor = sensor;
    }

    // Monolithic switch violating Open/Closed Principle
    public double calculatePrice(DeliveryRequest request, PricingType type) {
        switch (type) {
            case STANDARD:
                double ratePerKm = request.distanceKm() > 20.0 ? 1.20 : 1.50;
                return 10.0 + (request.distanceKm() * ratePerKm);

            case SURGE:
                double congestion = sensor.getCongestionMultiplier();
                return 12.0 + (request.distanceKm() * 2.0 * congestion) + (request.weightKg() * 0.5);

            case ECO_SAVER:
                double price = 7.0 + (request.distanceKm() * 0.90);
                if (request.isFlexibleOffPeak()) {
                    price -= 3.0;
                }
                return Math.max(price, 5.0); // minimum $5 floor

            default:
                throw new IllegalArgumentException("Unknown pricing type: " + type);
        }
    }
}
