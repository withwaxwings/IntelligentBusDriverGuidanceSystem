import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class BusUnitTest {
    private final BusService busService = new BusService(new BusRepository());

    // B1 - Valid BusID
    @Nested
    class ValidBusID{
        // Test Case 1 – Valid BusID
        @Test
        void Valid_BusID_returns_True() {
            assertTrue(busService.isValidBusID("12345678"));
        }
        // Test Case 2 – Too Short
        @Test
        void TooShort_BusID_returns_False() {
            assertFalse(busService.isValidBusID("1234567"));
        }
        // Test Case 3 – Too Long
        @Test
        void TooLong_BusID_returns_False() {
            assertFalse(busService.isValidBusID("123456789"));
        }
        // Test Case 4 – Contains Letter
        @Test
        void BusID_ContainsLetter_returns_False() {
            assertFalse(busService.isValidBusID("1234567A"));
        }
        // Test Case 5 - Null and Empty
        @Test
        void GivenEmpty_ShouldReject() {
            assertFalse(busService.isValidBusID(""));
            assertFalse(busService.isValidBusID(null));
        }
        }
    // B2 - Valid Capacity
    @Nested
    class ValidBusCapacity{
        // Test Case 1 - Valid Lower Boundary Capacity
        @Test
        void Capacity_LowerBoundary_returns_True() {
            assertTrue(busService.isValidCapacity(1));
        }
        // Test Case 2 - Valid Upper Boundary Capacity
        @Test
        void Capacity_UpperBoundary_returns_True() {
            // Upper bound is 70 per BusService implementation
            assertTrue(busService.isValidCapacity(70));
        }
        // Test Case 3 -Too Low
        @Test
        void Capacity_Zero_returns_False() {
            assertFalse(busService.isValidCapacity(0));
        }
        // Test Case 4 -Too High
        @Test
        void Capacity_OverLimit_returns_False() {
            assertFalse(busService.isValidCapacity(71));
        }
        // Test Case 5 - Valid Capacity Update
        @Test
        void SetCapacity_Decrease_succeeds() {
            Bus bus = new Bus("12345678", 60, 50.0, FuelType.DIESEL);
            busService.updateCapacity(bus, 50);
            assertEquals(50, bus.getCapacity());
        }
        // Test Case 6 - Capacity Update Too High
        @Test
        void SetCapacity_Increase_throws_IllegalState() {
            Bus bus = new Bus("12345678", 50, 50.0, FuelType.DIESEL);
            assertThrows(IllegalStateException.class, () -> busService.updateCapacity(bus, 60));
        }
        // Test Case 7 - Capacity Update Too Low
        @Test
        void SetCapacity_Zero_throws_IllegalArgument() {
            Bus bus = new Bus("12345678", 50, 50.0, FuelType.DIESEL);
            assertThrows(IllegalArgumentException.class, () -> busService.updateCapacity(bus, 0));
        }
    }
    // B3 - Valid Age
    @Nested
    class ValidDriverAge{}
    // B4 - Electric Bus Restriction
    @Nested
    class ValidElectricDriver{}
    // B5 - Driver Licence Restriction
    @Nested
    class ValidDriverLicence{}





}
