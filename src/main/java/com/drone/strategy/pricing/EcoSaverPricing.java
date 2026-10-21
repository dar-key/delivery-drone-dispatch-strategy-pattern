package com.drone.strategy.pricing;

import com.drone.model.DeliveryRequest;

public class EcoSaverPricing implements DeliveryPricingStrategy {
    @Override
    public double calculatePrice(DeliveryRequest request) {
        double price = 7.0 + (request.distanceKm() * 0.90);
        if (request.isFlexibleOffPeak()) {
            price -= 3.0;
        }
        return Math.max(price, 5.0);
    }
}
