package com.drone;

import com.drone.model.DeliveryRequest;
import com.drone.pricing.PricingCalculator;
import com.drone.pricing.PricingType;
import com.drone.sensor.FakeAirspaceSensor;
import com.drone.strategy.pricing.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PricingStrategyTest {
    private FakeAirspaceSensor fakeSensor;

    @BeforeEach
    void setUp() {
        fakeSensor = new FakeAirspaceSensor(1.5);
    }

    @Test
    void testStandardDistanceStrategyDirectly() {
        DeliveryPricingStrategy strategy = new StandardDistancePricing();
        DeliveryRequest req = new DeliveryRequest("R1", 10.0, 2.0, false, false);

        // $10 base + 10.0 * $1.50 = $25.0
        assertEquals(25.0, strategy.calculatePrice(req), 0.001);
    }

    @Test
    void testCongestionSurgeStrategyDirectly() {
        DeliveryPricingStrategy strategy = new CongestionSurgePricing(fakeSensor);
        DeliveryRequest req = new DeliveryRequest("R2", 10.0, 4.0, true, false);

        // $12 base + (10 * 2.0 * 1.5) + (4 * 0.5) = $44.0
        assertEquals(44.0, strategy.calculatePrice(req), 0.001);
    }

    @Test
    void testEcoSaverStrategyDirectly() {
        DeliveryPricingStrategy strategy = new EcoSaverPricing();
        DeliveryRequest req = new DeliveryRequest("R3", 10.0, 1.0, false, true);

        // $7 base + (10 * $0.90) - $3 = $13.0
        assertEquals(13.0, strategy.calculatePrice(req), 0.001);
    }

    @Test
    void testContextRuntimeStrategySwitching() {
        PricingCalculator context = new PricingCalculator(new StandardDistancePricing());
        DeliveryRequest req = new DeliveryRequest("R4", 10.0, 2.0, false, false);

        assertEquals(25.0, context.calculatePrice(req), 0.001);

        // Swap strategy at runtime to EcoSaver
        context.setStrategy(new EcoSaverPricing());
        // $7 base + (10 * $0.90) = $16.0 (not flexible)
        assertEquals(16.0, context.calculatePrice(req), 0.001);
    }

    @Test
    void testSelectorAutomaticRules() {
        PricingSelector selector = new PricingSelector(fakeSensor);

        // Rule 1: Emergency -> CongestionSurgePricing
        assertInstanceOf(CongestionSurgePricing.class,
                selector.selectStrategy(new DeliveryRequest("S1", 10.0, 1.0, true, false)));

        // Rule 2: Flexible Off-Peak -> EcoSaverPricing
        assertInstanceOf(EcoSaverPricing.class,
                selector.selectStrategy(new DeliveryRequest("S2", 10.0, 1.0, false, true)));

        // Rule 3: Standard -> StandardDistancePricing
        assertInstanceOf(StandardDistancePricing.class,
                selector.selectStrategy(new DeliveryRequest("S3", 10.0, 1.0, false, false)));
    }

    @Test
    void testSelectorExplicitTypeLookup() {
        PricingSelector selector = new PricingSelector(fakeSensor);

        assertInstanceOf(StandardDistancePricing.class, selector.getStrategy(PricingType.STANDARD));
        assertInstanceOf(CongestionSurgePricing.class, selector.getStrategy(PricingType.SURGE));
        assertInstanceOf(EcoSaverPricing.class, selector.getStrategy(PricingType.ECO_SAVER));
    }

    // Requirement 6: Parameterized test verifying all strategies on the same
    // business invariant
    static Stream<DeliveryPricingStrategy> providePricingStrategies() {
        return Stream.of(
                new StandardDistancePricing(),
                new CongestionSurgePricing(new FakeAirspaceSensor(1.0)),
                new EcoSaverPricing());
    }

    @ParameterizedTest
    @MethodSource("providePricingStrategies")
    void testAllStrategiesProducePriceAboveMinimumFloor(DeliveryPricingStrategy strategy) {
        // Test with short distance
        DeliveryRequest shortTrip = new DeliveryRequest("T-SHORT", 1.0, 0.5, false, true);
        double shortPrice = strategy.calculatePrice(shortTrip);
        assertTrue(shortPrice >= 5.0, "Price must be at least the $5.0 base floor");

        // Test with long distance
        DeliveryRequest longTrip = new DeliveryRequest("T-LONG", 40.0, 3.0, false, false);
        double longPrice = strategy.calculatePrice(longTrip);
        assertTrue(longPrice > 0.0, "Price must always be positive");
    }
}
