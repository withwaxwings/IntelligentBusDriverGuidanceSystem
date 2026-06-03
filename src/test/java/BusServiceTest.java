import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class BusServiceTest {
    private final BusService busService = new BusService(new BusRepository());
    @Nested
    class ValidFuelLevel {
        // Test Case 1 - Valid Fuel Level
        @Test
        void FuelLevel_Valid_returns_True() {
            assertTrue(busService.isValidFuelLevel(0.0));
            assertTrue(busService.isValidFuelLevel(50.0));
            assertTrue(busService.isValidFuelLevel(100.0));
        }
        // Test Case 2 - Fuel Level Below Zero
        @Test
        void FuelLevel_Negative_returns_False() {
            assertFalse(busService.isValidFuelLevel(-1.0));
            assertFalse(busService.isValidFuelLevel(-50.0));
            assertFalse(busService.isValidFuelLevel(-100.0));
        }
        // Test Case 3 - Fuel Level Above 100
        @Test
        void FuelLevel_OverHundred_returns_False() {
            assertFalse(busService.isValidFuelLevel(100.1));
            assertFalse(busService.isValidFuelLevel(150.0));
            assertFalse(busService.isValidFuelLevel(200.0));
        }
    }

    @Nested
    class UpdateFuelLevel {
        // Test Case 1 - Valid Fuel Level Update
        @Test
        void SetFuelLevel_Valid_succeeds() {
            Bus bus1 = new Bus("12345678", 50, 50.0, FuelType.DIESEL);
            Bus bus2 = new Bus("12345678", 50, 50.0, FuelType.DIESEL);
            Bus bus3 = new Bus("12345678", 50, 50.0, FuelType.DIESEL);
            busService.updateFuelLevel(bus1, 75.0);
            busService.updateFuelLevel(bus2, 0.0);
            busService.updateFuelLevel(bus3, 100.0);
            assertEquals(75.0, bus1.getFuelLevel());
            assertEquals(0.0, bus2.getFuelLevel());
            assertEquals(100.0, bus3.getFuelLevel());
        }
        // Test Case 2 - Invalid Fuel Level Update
        @Test
        void SetFuelLevel_Negative_throws_IllegalArgument() {
            Bus bus1 = new Bus("12345678", 50, 50.0, FuelType.DIESEL);
            Bus bus2 = new Bus("12345678", 50, 50.0, FuelType.DIESEL);
            Bus bus3 = new Bus("12345678", 50, 50.0, FuelType.DIESEL);
            assertThrows(IllegalArgumentException.class, () -> busService.updateFuelLevel(bus1, -5.0));
            assertThrows(IllegalArgumentException.class, () -> busService.updateFuelLevel(bus2, -1.0));
            assertThrows(IllegalArgumentException.class, () -> busService.updateFuelLevel(bus3, -100.0));
        }
    }

    @Nested
    class UpdateFuelType {
        // Test Case 1 - Valid Fuel Type Update
        @Test
        void SetFuelType_Valid_succeeds() {
            Bus bus1 = new Bus("12345678", 50, 50.0, FuelType.DIESEL);
            Bus bus2 = new Bus("12345678", 50, 50.0, FuelType.DIESEL);
            Bus bus3 = new Bus("12345678", 50, 50.0, FuelType.HYBRID);
            busService.updateFuelType(bus1, FuelType.HYBRID);
            busService.updateFuelType(bus2, FuelType.ELECTRICITY);
            busService.updateFuelType(bus3, FuelType.DIESEL);
            assertEquals(FuelType.HYBRID, bus1.getFuelType());
            assertEquals(FuelType.ELECTRICITY, bus2.getFuelType());
            assertEquals(FuelType.DIESEL, bus3.getFuelType());
        }
    }
}