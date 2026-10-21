package com.drone.strategy.selection;

import com.drone.model.DeliveryRequest;
import com.drone.model.Drone;

import java.util.List;

public class DroneFleetDispatcher {
    private DroneSelectionStrategy strategy;

    public DroneFleetDispatcher(DroneSelectionStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy cannot be null");
        }
        this.strategy = strategy;
    }

    public void setStrategy(DroneSelectionStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy cannot be null");
        }
        this.strategy = strategy;
    }

    public Drone dispatchDrone(List<Drone> fleet, DeliveryRequest request) {
        return strategy.selectDrone(fleet, request);
    }
}
