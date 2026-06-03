import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class BusUnitTest {
    private final BusService busService = new BusService(new BusRepository());

    // B1 - Valid BusID
    @Nested
    class ValidBusID {
        // Test Case 1 – Valid BusID
        @Test
        void Valid_BusID_returns_True() {
            assertTrue(busService.isValidBusID("12345678"));
            assertTrue(busService.isValidBusID("00000000"));
            assertTrue(busService.isValidBusID("99999999"));
        }
        // Test Case 2 – Too Short
        @Test
        void TooShort_BusID_returns_False() {
            assertFalse(busService.isValidBusID("1234567"));
            assertFalse(busService.isValidBusID("1"));
            assertFalse(busService.isValidBusID(""));
        }
        // Test Case 3 – Too Long
        @Test
        void TooLong_BusID_returns_False() {
            assertFalse(busService.isValidBusID("123456789"));
            assertFalse(busService.isValidBusID("1234567890"));
            assertFalse(busService.isValidBusID("123456789012"));
        }
        // Test Case 4 – Contains Letter
        @Test
        void BusID_ContainsLetter_returns_False() {
            assertFalse(busService.isValidBusID("1234567A"));
            assertFalse(busService.isValidBusID("ABCDEFGH"));
            assertFalse(busService.isValidBusID("1234!678"));
        }
        // Test Case 5 - Null and Empty
        @Test
        void GivenNullOrEmpty_ShouldReject() {
            assertFalse(busService.isValidBusID(""));
            assertFalse(busService.isValidBusID(null));
            assertFalse(busService.isValidBusID("        "));
        }
    }

    // B2 - Valid Capacity
    @Nested
    class ValidBusCapacity {
        // Test Case 1 – Valid Lower Boundary Capacity
        @Test
        void Capacity_LowerBoundary_returns_True() {
            assertTrue(busService.isValidCapacity(1));
            assertTrue(busService.isValidCapacity(2));
            assertTrue(busService.isValidCapacity(35));
        }
        // Test Case 2 – Valid Upper Boundary Capacity
        @Test
        void Capacity_UpperBoundary_returns_True() {
            assertTrue(busService.isValidCapacity(70));
            assertTrue(busService.isValidCapacity(69));
            assertTrue(busService.isValidCapacity(10));
        }
        // Test Case 3 – Too Low
        @Test
        void Capacity_Zero_returns_False() {
            assertFalse(busService.isValidCapacity(0));
            assertFalse(busService.isValidCapacity(-1));
            assertFalse(busService.isValidCapacity(-100));
        }
        // Test Case 4 – Too High
        @Test
        void Capacity_OverLimit_returns_False() {
            assertFalse(busService.isValidCapacity(71));
            assertFalse(busService.isValidCapacity(72));
            assertFalse(busService.isValidCapacity(200));
        }
        // Test Case 5 – Valid Capacity Update
        @Test
        void SetCapacity_Decrease_succeeds() {
            Bus bus = new Bus("12345678", 60, 50.0, FuelType.DIESEL);
            busService.updateCapacity(bus, 50);
            assertEquals(50, bus.getCapacity());

            Bus bus2 = new Bus("12345678", 60, 50.0, FuelType.DIESEL);
            busService.updateCapacity(bus2, 1);
            assertEquals(1, bus2.getCapacity());

            Bus bus3 = new Bus("12345678", 50, 50.0, FuelType.DIESEL);
            busService.updateCapacity(bus3, 50);
            assertEquals(50, bus3.getCapacity());
        }
        // Test Case 6 – Capacity Update Too High
        @Test
        void SetCapacity_Increase_throws_IllegalState() {
            Bus bus = new Bus("12345678", 50, 50.0, FuelType.DIESEL);
            assertThrows(IllegalStateException.class, () -> busService.updateCapacity(bus, 60));

            Bus bus2 = new Bus("12345678", 50, 50.0, FuelType.DIESEL);
            assertThrows(IllegalStateException.class, () -> busService.updateCapacity(bus2, 51));

            Bus bus3 = new Bus("12345678", 30, 50.0, FuelType.DIESEL);
            assertThrows(IllegalStateException.class, () -> busService.updateCapacity(bus3, 70));
        }
        // Test Case 7 – Capacity Update Too Low
        @Test
        void SetCapacity_Zero_throws_IllegalArgument() {
            Bus bus = new Bus("12345678", 50, 50.0, FuelType.DIESEL);
            assertThrows(IllegalArgumentException.class, () -> busService.updateCapacity(bus, 0));

            Bus bus2 = new Bus("12345678", 50, 50.0, FuelType.DIESEL);
            assertThrows(IllegalArgumentException.class, () -> busService.updateCapacity(bus2, -1));

            Bus bus3 = new Bus("12345678", 50, 50.0, FuelType.DIESEL);
            assertThrows(IllegalArgumentException.class, () -> busService.updateCapacity(bus3, -50));
        }
    }
}