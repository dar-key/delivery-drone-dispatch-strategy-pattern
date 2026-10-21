package com.drone.strategy.selection;

import com.drone.model.DeliveryRequest;
import com.drone.model.Drone;
import java.util.List;

public interface DroneSelectionStrategy {
    Drone selectDrone(List<Drone> fleet, DeliveryRequest request);
}
