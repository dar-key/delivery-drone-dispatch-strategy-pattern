package com.drone.pricing;

import com.drone.model.DeliveryRequest;
import com.drone.sensor.AirspaceSensorFeed;
import com.drone.strategy.pricing.DeliveryPricingStrategy;
import com.drone.strategy.pricing.PricingSelector;

public class PricingCalculator {
    private DeliveryPricingStrategy strategy;
    private PricingSelector selector;

    // Pure Context constructor
    public PricingCalculator(DeliveryPricingStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy cannot be null");
        }
        this.strategy = strategy;
    }

    // Constructor supporting selector-based resolution
    public PricingCalculator(AirspaceSensorFeed sensor) {
        this.selector = new PricingSelector(sensor);
        this.strategy = selector.getStrategy(PricingType.STANDARD);
    }

    // Context setter for runtime switching
    public void setStrategy(DeliveryPricingStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy cannot be null");
        }
        this.strategy = strategy;
    }

    // Context delegation (Zero conditionals)
    public double calculatePrice(DeliveryRequest request) {
        return strategy.calculatePrice(request);
    }

    // Backwards-compatible delegation keeping original test suite passing
    public double calculatePrice(DeliveryRequest request, PricingType type) {
        return selector.getStrategy(type).calculatePrice(request);
    }
}
