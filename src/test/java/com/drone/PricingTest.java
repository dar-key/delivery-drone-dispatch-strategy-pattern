package com.drone;

import com.drone.model.DeliveryRequest;
import com.drone.pricing.PricingCalculator;
import com.drone.pricing.PricingType;
import com.drone.sensor.FakeAirspaceSensor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PricingTest {
    private FakeAirspaceSensor fakeSensor;
    private PricingCalculator calculator;

    @BeforeEach
    void setUp() {
        fakeSensor = new FakeAirspaceSensor(1.5); // Fixed 1.5x congestion
        calculator = new PricingCalculator(fakeSensor);
    }

    @Test
    void testStandardShortDistancePricing() {
        DeliveryRequest req = new DeliveryRequest("P1", 10.0, 2.0, false, false);
        // 10.0 base + 10.0 * 1.50 = 25.0
        assertEquals(25.0, calculator.calculatePrice(req, PricingType.STANDARD), 0.001);
    }

    @Test
    void testStandardLongDistanceDiscount() {
        DeliveryRequest req = new DeliveryRequest("P2", 30.0, 2.0, false, false);
        // 10.0 base + 30.0 * 1.20 = 46.0
        assertEquals(46.0, calculator.calculatePrice(req, PricingType.STANDARD), 0.001);
    }

    @Test
    void testSurgePricingWithSensorData() {
        DeliveryRequest req = new DeliveryRequest("P3", 10.0, 4.0, false, false);
        // 12.0 base + (10 * 2.0 * 1.5) + (4 * 0.5) = 12 + 30 + 2 = 44.0
        assertEquals(44.0, calculator.calculatePrice(req, PricingType.SURGE), 0.001);
    }

    @Test
    void testEcoSaverOffPeakDiscount() {
        DeliveryRequest req = new DeliveryRequest("P4", 10.0, 1.0, false, true); // flexible = true
        // 7.0 base + (10 * 0.90) - 3.0 rebate = 13.0
        assertEquals(13.0, calculator.calculatePrice(req, PricingType.ECO_SAVER), 0.001);
    }

    @Test
    void testEcoSaverFixedTimeNoDiscount() {
        DeliveryRequest req = new DeliveryRequest("P5", 10.0, 1.0, false, false); // flexible = false
        // 7.0 base + (10 * 0.90) = 16.0
        assertEquals(16.0, calculator.calculatePrice(req, PricingType.ECO_SAVER), 0.001);
    }
}
