package com.drone.model;

public record DeliveryRequest(
        String requestId,
        double distanceKm,
        double weightKg,
        boolean isEmergency,
        boolean isFlexibleOffPeak) {
}
