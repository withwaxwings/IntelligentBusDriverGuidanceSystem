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
    class ValidDriverAge{
        //Test Case 1 - Invalid Driver Over 50 With Too High Capacity
        @Test
        void Eligibility_DriverOver50_LargeBus_returns_False() {
            BusDriverRelationService service =
                    new BusDriverRelationService(new BusDriverRelationRepository());
            Driver driver = new Driver("34ab!!cdAB", "Alice", 15, LicenseType.HEAVY,
                    "1|St|City|ST|USA", "01-01-1970");
            Bus bus = new Bus("11111111", 50, 80.0, FuelType.DIESEL);
            assertFalse(service.isDriverEligible(driver, bus));
        }
        //Test Case 2 - Valid Driver Over 50 With Valid Capacity
        @Test
        void Eligibility_DriverOver50_SmallBus_returns_True() {
            BusDriverRelationService service =
                    new BusDriverRelationService(new BusDriverRelationRepository());
            Driver driver = new Driver("34ab!!cdAB", "Alice", 15, LicenseType.HEAVY,
                    "1|St|City|ST|USA", "01-01-1970");
            Bus bus = new Bus("11111112", 30, 80.0, FuelType.DIESEL);
            assertTrue(service.isDriverEligible(driver, bus));
        }
        //Test Case 3 - Valid Driver Aged Exactly 50
        @Test
        void Eligibility_DriverExactlyAge50_CapacityFiftyBus_returns_True() {
            java.time.LocalDate dob =
                    java.time.LocalDate.now().minusYears(50);

            String birthdate = String.format(
                    "%02d-%02d-%04d",
                    dob.getDayOfMonth(),
                    dob.getMonthValue(),
                    dob.getYear()
            );

            Driver driver = new Driver(
                    "34ab!!cdAB",
                    "Alice",
                    15,
                    LicenseType.HEAVY,
                    "1|St|City|ST|Australia",
                    birthdate
            );

            Bus bus = new Bus("11111118", 50, 80.0, FuelType.DIESEL);
            assertTrue(BusDriverRelationService.isDriverEligible(driver, bus));
        }
    }
    // B4 - Electric Bus Restriction
    @Nested
    class ValidElectricDriver{
        //Test Case 1 - Driver Does Not Have Enough Experience
        @Test
        void Eligibility_ElectricBus_UnderExperience_returns_False() {
            BusDriverRelationService service =
                    new BusDriverRelationService(new BusDriverRelationRepository());
            Driver driver = new Driver("34ab!!cdAB", "Bob", 3, LicenseType.HEAVY,
                    "1|St|City|ST|USA", "01-01-1995");
            Bus bus = new Bus("11111113", 30, 80.0, FuelType.ELECTRICITY);
            assertFalse(service.isDriverEligible(driver, bus));
        }
        //Test Case 2 - Driver Has Enough Experience
        @Test
        void Eligibility_ElectricBus_HeavyLicense_SufficientExp_returns_True() {
            BusDriverRelationService service =
                    new BusDriverRelationService(new BusDriverRelationRepository());
            Driver driver = new Driver("34ab!!cdAB", "Dave", 6, LicenseType.HEAVY,
                    "1|St|City|ST|USA", "01-01-1995");
            Bus bus = new Bus("11111115", 30, 80.0, FuelType.ELECTRICITY);
            assertTrue(service.isDriverEligible(driver, bus));
        }
        //Test Case 3 - Driver Has Exactly 5 Years Experience
        @Test
        void Eligibility_ElectricBus_ExactlyFiveYears_returns_True() {
            Driver driver = new Driver(
                    "34ab!!cdAB",
                    "Bob",
                    5,
                    LicenseType.HEAVY,
                    "1|St|City|ST|Australia",
                    "01-01-1995"
            );

            Bus bus = new Bus("11111119", 30, 80.0, FuelType.ELECTRICITY);

            assertTrue(BusDriverRelationService.isDriverEligible(driver, bus));
        }
    }
    // B5 - Driver Licence Restriction
    @Nested
    class ValidDriverLicence{
        //Driver Does Not Have Correct License Type
        @Test
        void Eligibility_HybridBus_LightLicense_returns_False() {
            BusDriverRelationService service =
                    new BusDriverRelationService(new BusDriverRelationRepository());
            Driver driver = new Driver("34ab!!cdAB", "Eve", 10, LicenseType.LIGHT,
                    "1|St|City|ST|USA", "01-01-1995");
            Bus bus = new Bus("11111116", 30, 80.0, FuelType.HYBRID);
            assertFalse(service.isDriverEligible(driver, bus));
        }
        //Driver Does Not Have Correct License Type
        @Test
        void Eligibility_HybridBus_MediumLicense_returns_False() {
            Driver driver = new Driver(
                    "34ab!!cdAB",
                    "Carol",
                    8,
                    LicenseType.MEDIUM,
                    "1|St|City|ST|Australia",
                    "01-01-1990"
            );

            Bus bus = new Bus("11111120", 30, 80.0, FuelType.HYBRID);

            assertFalse(BusDriverRelationService.isDriverEligible(driver, bus));
        }
        //Driver Has Correct License Type
        @Test
        void Eligibility_DieselBus_LightLicense_returns_True() {
            BusDriverRelationService service =
                    new BusDriverRelationService(new BusDriverRelationRepository());
            Driver driver = new Driver("34ab!!cdAB", "Frank", 2, LicenseType.LIGHT,
                    "1|St|City|ST|USA", "01-01-1995");
            Bus bus = new Bus("11111117", 30, 80.0, FuelType.DIESEL);
            assertTrue(BusDriverRelationService.isDriverEligible(driver, bus));
        }
    }


}
