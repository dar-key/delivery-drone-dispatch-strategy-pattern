package com.drone.strategy.pricing;

import com.drone.model.DeliveryRequest;

public interface DeliveryPricingStrategy {
    double calculatePrice(DeliveryRequest request);
}
