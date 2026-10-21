package com.drone;

import com.drone.model.DeliveryRequest;
import com.drone.model.Drone;
import com.drone.strategy.selection.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DroneSelectionTest {
    private List<Drone> fleet;

    @BeforeEach
    void setUp() {
        fleet = List.of(
                new Drone("DRONE-SPEED", "Veloce-4", 130.0, 0.40, 180.0),
                new Drone("DRONE-ECO", "AeroGlider", 65.0, 0.12, 220.0),
                new Drone("DRONE-FRESH", "Titan-Heavy", 80.0, 0.28, 15.0));
    }

    @Test
    void testSpeedPrioritySelection() {
        DroneSelectionStrategy strategy = new SpeedPrioritySelection();
        Drone result = strategy.selectDrone(fleet, new DeliveryRequest("R1", 10.0, 2.0, true, false));
        assertEquals("DRONE-SPEED", result.id());
    }

    @Test
    void testEnergyEfficiencySelection() {
        DroneSelectionStrategy strategy = new EnergyEfficiencySelection();
        Drone result = strategy.selectDrone(fleet, new DeliveryRequest("R2", 10.0, 4.0, false, false));
        assertEquals("DRONE-ECO", result.id());
    }

    @Test
    void testWearBalancingSelection() {
        DroneSelectionStrategy strategy = new FleetWearBalancingSelection();
        Drone result = strategy.selectDrone(fleet, new DeliveryRequest("R3", 10.0, 2.0, false, false));
        assertEquals("DRONE-FRESH", result.id());
    }

    @Test
    void testContextRuntimeStrategySwitching() {
        DroneFleetDispatcher dispatcher = new DroneFleetDispatcher(new SpeedPrioritySelection());
        DeliveryRequest req = new DeliveryRequest("R4", 5.0, 1.0, false, false);

        assertEquals("DRONE-SPEED", dispatcher.dispatchDrone(fleet, req).id());

        dispatcher.setStrategy(new FleetWearBalancingSelection());
        assertEquals("DRONE-FRESH", dispatcher.dispatchDrone(fleet, req).id());
    }

    @Test
    void testSelectorAutomaticChoices() {
        DroneAllocationSelector selector = new DroneAllocationSelector();

        // Emergency -> Speed
        assertInstanceOf(SpeedPrioritySelection.class,
                selector.selectStrategy(new DeliveryRequest("R-EMG", 10.0, 1.0, true, false)));

        // Heavy parcel (> 3.5kg) -> Energy
        assertInstanceOf(EnergyEfficiencySelection.class,
                selector.selectStrategy(new DeliveryRequest("R-HVY", 10.0, 4.0, false, false)));

        // Standard -> Wear
        assertInstanceOf(FleetWearBalancingSelection.class,
                selector.selectStrategy(new DeliveryRequest("R-STD", 10.0, 1.5, false, false)));
    }

    static Stream<DroneSelectionStrategy> provideStrategies() {
        return Stream.of(
                new SpeedPrioritySelection(),
                new EnergyEfficiencySelection(),
                new FleetWearBalancingSelection());
    }

    @ParameterizedTest
    @MethodSource("provideStrategies")
    void testAllStrategiesPickValidDrone(DroneSelectionStrategy strategy) {
        DeliveryRequest req = new DeliveryRequest("R-PARAM", 10.0, 2.0, false, false);
        Drone picked = strategy.selectDrone(fleet, req);
        assertNotNull(picked);
        assertTrue(fleet.contains(picked));
    }
}
