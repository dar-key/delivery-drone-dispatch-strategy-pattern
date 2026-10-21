# Drone Delivery Strategy Pattern

Assignment 5: Drone delivery system using Strategy pattern for fleet selection and delivery pricing.

## How to run

Run tests:

```bash
mvn clean test
```

Run demo app:

```bash
mvn compile exec:java
```

---

## Refactoring with Git history (Requirement 4)

- Before refactoring (monolithic switch statement): `5d34eee`
- After refactoring (strategy pattern with context and selector): `c3fc69a`

The exact same tests in `PricingTest.java` pass on both commits without any changes to the test code.

---

## Single Responsibility Principle table

Every description is one sentence, under 25 words, without "if", "and", "or", "but".

| Class                         | Responsibility                                                                  |
| :---------------------------- | :------------------------------------------------------------------------------ |
| `Drone`                       | Stores static physical specifications of an aerial delivery drone.              |
| `DeliveryRequest`             | Holds client delivery order parameters.                                         |
| `AirspaceSensorFeed`          | Declares the contract for reading real-time airspace congestion data.           |
| `SimulatedAirspaceSensor`     | Emulates dynamic airspace sensor readings for runtime environments.             |
| `FakeAirspaceSensor`          | Returns predetermined congestion multiplier values for repeatable unit testing. |
| `DroneSelectionStrategy`      | Defines the common contract for drone allocation algorithms.                    |
| `SpeedPrioritySelection`      | Selects the drone with highest maximum cruise speed.                            |
| `EnergyEfficiencySelection`   | Chooses the drone minimizing energy burn per kilometer.                         |
| `FleetWearBalancingSelection` | Picks the drone with lowest cumulative flight hours.                            |
| `DroneFleetDispatcher`        | Delegates dispatch assignments to its configured selection strategy.            |
| `DroneAllocationSelector`     | Chooses a selection strategy based on parcel urgency.                           |
| `DeliveryPricingStrategy`     | Defines the contract for delivery price calculation algorithms.                 |
| `StandardDistancePricing`     | Computes delivery prices using tiered kilometer rates.                          |
| `CongestionSurgePricing`      | Computes surge prices using external airspace sensor data.                      |
| `EcoSaverPricing`             | Applies discounted rates for flexible delivery time slots.                      |
| `PricingCalculator`           | Delegates price calculation to its configured pricing strategy.                 |
| `PricingSelector`             | Chooses a pricing strategy based on order delivery conditions.                  |
| `PricingType`                 | Enumerates available pricing options for legacy clients.                        |

---

## Reflection: Strategy vs State vs Template Method

All three patterns can change behavior, but their intent and structure are different.

### Strategy vs State

Both patterns use composition and delegate work to another object, but the difference is how state changes happen.
In the State pattern, the object changes its behavior based on internal state changes. The concrete state objects usually know about each other and handle transitions between themselves.
In Strategy, the algorithms are independent and do not know about each other. `SpeedPrioritySelection` does not switch itself to `EcoSaverPricing`. The strategy is picked from the outside by the client or a selector class based on input data.

### Strategy vs Template Method

Template Method relies on inheritance. A base abstract class defines the main algorithm skeleton, while subclasses override specific steps. This means the algorithm structure is locked at compile time.
Strategy uses composition instead. The context class holds an interface reference, so we can swap out the whole algorithm at runtime using `setStrategy()`.

For our drone system, we need to choose whole pricing and drone allocation algorithms dynamically based on external data like weather, package weight, or customer choice. That is why Strategy fits best here.
