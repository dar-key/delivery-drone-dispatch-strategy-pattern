package com.drone.strategy.selection;

import com.drone.model.DeliveryRequest;
import com.drone.model.Drone;

import java.util.Comparator;
import java.util.List;

public class EnergyEfficiencySelection implements DroneSelectionStrategy {
    @Override
    public Drone selectDrone(List<Drone> fleet, DeliveryRequest request) {
        if (fleet == null || fleet.isEmpty()) {
            throw new IllegalArgumentException("Fleet cannot be empty");
        }
        // Energy scaled by cargo mass
        return fleet.stream()
                .min(Comparator.comparingDouble(d -> d.energyPerKmKwh() * (1.0 + 0.1 * request.weightKg())))
                .orElseThrow();
    }
}
