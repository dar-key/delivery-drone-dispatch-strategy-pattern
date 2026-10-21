package com.drone.strategy.pricing;

import com.drone.model.DeliveryRequest;

public class StandardDistancePricing implements DeliveryPricingStrategy {
    @Override
    public double calculatePrice(DeliveryRequest request) {
        double ratePerKm = request.distanceKm() > 20.0 ? 1.20 : 1.50;
        return 10.0 + (request.distanceKm() * ratePerKm);
    }
}
