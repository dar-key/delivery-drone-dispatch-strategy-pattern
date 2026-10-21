package com.drone.strategy.pricing;

import com.drone.model.DeliveryRequest;
import com.drone.pricing.PricingType;
import com.drone.sensor.AirspaceSensorFeed;

public class PricingSelector {
    private final AirspaceSensorFeed sensor;

    public PricingSelector(AirspaceSensorFeed sensor) {
        this.sensor = sensor;
    }

    // Automatic selection based on the order situation
    public DeliveryPricingStrategy selectStrategy(DeliveryRequest request) {
        if (request.isEmergency()) {
            return new CongestionSurgePricing(sensor);
        }
        if (request.isFlexibleOffPeak()) {
            return new EcoSaverPricing();
        }
        return new StandardDistancePricing();
    }

    // Explicit selection by type
    public DeliveryPricingStrategy getStrategy(PricingType type) {
        return switch (type) {
            case STANDARD -> new StandardDistancePricing();
            case SURGE -> new CongestionSurgePricing(sensor);
            case ECO_SAVER -> new EcoSaverPricing();
        };
    }
}
