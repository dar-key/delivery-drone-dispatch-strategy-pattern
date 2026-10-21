package com.drone.strategy.selection;

import com.drone.model.DeliveryRequest;

public class DroneAllocationSelector {
    public DroneSelectionStrategy selectStrategy(DeliveryRequest request) {
        if (request.isEmergency()) {
            return new SpeedPrioritySelection();
        }
        if (request.weightKg() > 3.5) {
            return new EnergyEfficiencySelection();
        }
        return new FleetWearBalancingSelection();
    }
}
