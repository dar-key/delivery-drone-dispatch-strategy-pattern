package com.drone.strategy.selection;

import com.drone.model.DeliveryRequest;
import com.drone.model.Drone;

import java.util.Comparator;
import java.util.List;

public class FleetWearBalancingSelection implements DroneSelectionStrategy {
    @Override
    public Drone selectDrone(List<Drone> fleet, DeliveryRequest request) {
        if (fleet == null || fleet.isEmpty()) {
            throw new IllegalArgumentException("Fleet cannot be empty");
        }
        return fleet.stream()
                .min(Comparator.comparingDouble(Drone::totalFlightHours))
                .orElseThrow();
    }
}
