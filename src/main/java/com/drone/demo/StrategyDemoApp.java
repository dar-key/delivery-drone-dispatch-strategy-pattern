package com.drone.demo;

import com.drone.model.DeliveryRequest;
import com.drone.model.Drone;
import com.drone.pricing.PricingCalculator;
import com.drone.sensor.SimulatedAirspaceSensor;
import com.drone.strategy.pricing.*;
import com.drone.strategy.selection.*;

import java.util.List;
import java.util.Scanner;

public class StrategyDemoApp {
    private static final List<Drone> fleet = List.of(
            new Drone("D-SPEED", "Falcon-X", 120.0, 0.40, 150.0),
            new Drone("D-ECO", "AeroGlider", 65.0, 0.12, 300.0),
            new Drone("D-NEW", "Standard-V", 80.0, 0.25, 20.0));

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SimulatedAirspaceSensor sensor = new SimulatedAirspaceSensor();

        System.out.println("Drone Strategy Pattern Demo");

        while (true) {
            System.out.println("\nSelect an option:");
            System.out.println("1. Compare all pricing strategies on one request");
            System.out.println("2. Manual choice vs Selector automatic choice");
            System.out.println("3. Runtime strategy switching");
            System.out.println("4. Exit");
            System.out.print("> ");

            String input = scanner.nextLine().trim();

            if (input.equals("1")) {
                // req 7: same input processed by all strategies of a family
                DeliveryRequest req = new DeliveryRequest("R1", 15.0, 3.0, false, true);
                System.out.println("\nTesting request: 15km, 3kg, flexible time window");

                DeliveryPricingStrategy standard = new StandardDistancePricing();
                DeliveryPricingStrategy surge = new CongestionSurgePricing(sensor);
                DeliveryPricingStrategy eco = new EcoSaverPricing();

                System.out.println("Standard pricing: $" + standard.calculatePrice(req));
                System.out.println("Surge pricing: $" + surge.calculatePrice(req));
                System.out.println("Eco saver pricing: $" + eco.calculatePrice(req));

            } else if (input.equals("2")) {
                // req 7: manual choice vs automatic selector choice
                DeliveryRequest emergencyReq = new DeliveryRequest("R2", 20.0, 1.5, true, false);
                System.out.println("\nOrder flagged as emergency");

                // manual choice
                DroneFleetDispatcher dispatcher = new DroneFleetDispatcher(new FleetWearBalancingSelection());
                System.out
                        .println("Manual pick (wear balancing): " + dispatcher.dispatchDrone(fleet, emergencyReq).id());

                // automatic choice by selector
                DroneAllocationSelector selector = new DroneAllocationSelector();
                DroneSelectionStrategy autoStrategy = selector.selectStrategy(emergencyReq);
                dispatcher.setStrategy(autoStrategy);
                System.out.println("Selector pick (" + autoStrategy.getClass().getSimpleName() + "): "
                        + dispatcher.dispatchDrone(fleet, emergencyReq).id());

            } else if (input.equals("3")) {
                // req 7: runtime strategy switching
                PricingCalculator context = new PricingCalculator(new StandardDistancePricing());
                DeliveryRequest req = new DeliveryRequest("R3", 10.0, 2.0, false, false);

                System.out.println("\nInitial price (standard): $" + context.calculatePrice(req));

                context.setStrategy(new CongestionSurgePricing(sensor));
                System.out.println("After switching to surge: $" + context.calculatePrice(req));

                context.setStrategy(new EcoSaverPricing());
                System.out.println("After switching to eco saver: $" + context.calculatePrice(req));

            } else if (input.equals("4")) {
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid option, try again.");
            }
        }
        scanner.close();
    }
}
