import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class BusUnitTest {
    private BusService busService;
    private BusDriverRelationService relationService;

    @BeforeEach
    void setUp() {
        busService = new BusService(new BusRepository());
        relationService = new BusDriverRelationService(new BusDriverRelationRepository());
    }

    // B1 - Bus ID Rules
    @Nested
    class ValidBusID {
        // Test Case 1 – Valid BusID
        @Test
        void GivenValidBusID_ShouldAccept() {
            assertTrue(busService.isValidBusID("12345678"));
            assertTrue(busService.isValidBusID("00000000"));
            assertTrue(busService.isValidBusID("99999999"));
        }
        // Test Case 2 – Too Short
        @Test
        void GivenTooShortBusID_ShouldReject() {
            assertFalse(busService.isValidBusID("1234567"));
            assertFalse(busService.isValidBusID("1"));
            assertFalse(busService.isValidBusID(""));
        }
        // Test Case 3 – Too Long
        @Test
        void GivenTooLongBusID_ShouldReject() {
            assertFalse(busService.isValidBusID("123456789"));
            assertFalse(busService.isValidBusID("1234567890"));
            assertFalse(busService.isValidBusID("123456789012"));
        }
        // Test Case 4 – Contains Letter
        @Test
        void GivenBusIDWithLetter_ShouldReject() {
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

    // B2 - Capacity Update Restriction
    @Nested
    class ValidBusCapacity {
        // Test Case 1 – Valid Lower Boundary Capacity
        @Test
        void GivenLowerBoundaryCapacity_ShouldAccept() {
            assertTrue(busService.isValidCapacity(1));
            assertTrue(busService.isValidCapacity(2));
            assertTrue(busService.isValidCapacity(35));
        }
        // Test Case 2 – Valid Upper Boundary Capacity
        @Test
        void GivenUpperBoundaryCapacity_ShouldAccept() {
            assertTrue(busService.isValidCapacity(70));
            assertTrue(busService.isValidCapacity(69));
            assertTrue(busService.isValidCapacity(10));
        }
        // Test Case 3 – Boundary and below
        @Test
        void GivenZeroCapacity_ShouldAccept() {
            assertTrue(busService.isValidCapacity(0));
        }

        @Test
        void GivenNegativeCapacity_ShouldReject() {
            assertFalse(busService.isValidCapacity(-1));
            assertFalse(busService.isValidCapacity(-100));
        }
        // Test Case 4 – Too High
        @Test
        void GivenOverLimitCapacity_ShouldReject() {
            assertFalse(busService.isValidCapacity(71));
            assertFalse(busService.isValidCapacity(72));
            assertFalse(busService.isValidCapacity(200));
        }
        // Test Case 5 – Valid Capacity Update
        @Test
        void GivenCapacityDecreased_ShouldUpdate() {
            Bus bus = new Bus("12345678", 60, 50.0, FuelType.DIESEL);
            busService.updateCapacity(bus, 50);
            assertEquals(50, bus.getCapacity());

            Bus bus2 = new Bus("12345679", 60, 50.0, FuelType.DIESEL);
            busService.updateCapacity(bus2, 1);
            assertEquals(1, bus2.getCapacity());

            Bus bus3 = new Bus("12345670", 50, 50.0, FuelType.DIESEL);
            busService.updateCapacity(bus3, 50);
            assertEquals(50, bus3.getCapacity());
        }
        // Test Case 6 – Capacity Update Too High
        @Test
        void GivenCapacityIncreased_ShouldThrow() {
            Bus bus = new Bus("12345678", 50, 50.0, FuelType.DIESEL);
            assertThrows(IllegalStateException.class, () -> busService.updateCapacity(bus, 60));

            Bus bus2 = new Bus("12345679", 50, 50.0, FuelType.DIESEL);
            assertThrows(IllegalStateException.class, () -> busService.updateCapacity(bus2, 51));

            Bus bus3 = new Bus("12345670", 30, 50.0, FuelType.DIESEL);
            assertThrows(IllegalStateException.class, () -> busService.updateCapacity(bus3, 70));
        }
        // Test Case 7 – Capacity Update Too Low
        @Test
        void GivenNegativeCapacityUpdate_ShouldThrow() {
            Bus bus = new Bus("12345678", 50, 50.0, FuelType.DIESEL);
            assertThrows(IllegalArgumentException.class, () -> busService.updateCapacity(bus, -1));

            Bus bus2 = new Bus("12345679", 50, 50.0, FuelType.DIESEL);
            assertThrows(IllegalArgumentException.class, () -> busService.updateCapacity(bus2, -50));
        }
    }

    // B3 - Driver Age Restriction
    @Nested
    class ValidDriverAge {
        String address = "124 | La Trobe St | Melbourne | Victoria | Australia";

        // Test Case 1 - Driver Over 50 With Large Bus
        @Test
        void GivenDriverOver50WithLargeBus_ShouldReject() {
            Driver driver1 = new Driver("34ab!!cdAB", "Alice", 15, LicenseType.HEAVY, address, "01-01-1970");
            Driver driver2 = new Driver("34ab!!cdAC", "Bob", 20, LicenseType.HEAVY, address, "01-01-1965");
            Driver driver3 = new Driver("34ab!!cdAD", "Carol", 10, LicenseType.HEAVY, address, "01-01-1960");
            Bus bus1 = new Bus("11111111", 50, 80.0, FuelType.DIESEL);
            Bus bus2 = new Bus("11111121", 55, 80.0, FuelType.DIESEL);
            Bus bus3 = new Bus("11111122", 60, 80.0, FuelType.DIESEL);
            assertFalse(relationService.isDriverEligible(driver1, bus1));
            assertFalse(relationService.isDriverEligible(driver2, bus2));
            assertFalse(relationService.isDriverEligible(driver3, bus3));
        }

        // Test Case 2 - Driver Over 50 With Small Bus
        @Test
        void GivenDriverOver50WithSmallBus_ShouldAccept() {
            Driver driver1 = new Driver("34ab!!cdAB", "Alice", 15, LicenseType.HEAVY, address, "01-01-1970");
            Driver driver2 = new Driver("34ab!!cdAC", "Bob", 20, LicenseType.HEAVY, address, "01-01-1965");
            Driver driver3 = new Driver("34ab!!cdAD", "Carol", 10, LicenseType.HEAVY, address, "01-01-1960");
            Bus bus1 = new Bus("11111112", 30, 80.0, FuelType.DIESEL);
            Bus bus2 = new Bus("11111123", 25, 80.0, FuelType.DIESEL);
            Bus bus3 = new Bus("11111124", 20, 80.0, FuelType.DIESEL);
            assertTrue(relationService.isDriverEligible(driver1, bus1));
            assertTrue(relationService.isDriverEligible(driver2, bus2));
            assertTrue(relationService.isDriverEligible(driver3, bus3));
        }

        // Test Case 3 - Driver Aged Exactly 50
        @Test
        void GivenDriverExactlyAge50_ShouldAccept() {
            LocalDate dob = LocalDate.now().minusYears(50);
            String birthdate = String.format("%02d-%02d-%04d", dob.getDayOfMonth(), dob.getMonthValue(), dob.getYear());

            Driver driver1 = new Driver("34ab!!cdAB", "Alice", 15, LicenseType.HEAVY, address, birthdate);
            Driver driver2 = new Driver("34ab!!cdAC", "Bob", 10, LicenseType.HEAVY, address, birthdate);
            Driver driver3 = new Driver("34ab!!cdAD", "Carol", 5, LicenseType.HEAVY, address, birthdate);
            Bus bus1 = new Bus("11111118", 50, 80.0, FuelType.DIESEL);
            Bus bus2 = new Bus("11111125", 50, 80.0, FuelType.DIESEL);
            Bus bus3 = new Bus("11111126", 50, 80.0, FuelType.DIESEL);
            assertTrue(relationService.isDriverEligible(driver1, bus1));
            assertTrue(relationService.isDriverEligible(driver2, bus2));
            assertTrue(relationService.isDriverEligible(driver3, bus3));
        }
    }

    // B4 - Electric Bus Restriction
    @Nested
    class ValidElectricDriver {
        String address = "124 | La Trobe St | Melbourne | Victoria | Australia";

        // Test Case 1 - Driver Does Not Have Enough Experience
        @Test
        void GivenElectricBusUnderExperience_ShouldReject() {
            Driver driver1 = new Driver("34ab!!cdAB", "Alice", 3, LicenseType.HEAVY, address, "01-01-1995");
            Driver driver2 = new Driver("34ab!!cdAC", "Bob", 2, LicenseType.HEAVY, address, "01-01-1995");
            Driver driver3 = new Driver("34ab!!cdAD", "Carol", 1, LicenseType.HEAVY, address, "01-01-1995");
            Bus bus1 = new Bus("11111113", 30, 80.0, FuelType.ELECTRICITY);
            Bus bus2 = new Bus("11111127", 30, 80.0, FuelType.ELECTRICITY);
            Bus bus3 = new Bus("11111128", 30, 80.0, FuelType.ELECTRICITY);
            assertFalse(relationService.isDriverEligible(driver1, bus1));
            assertFalse(relationService.isDriverEligible(driver2, bus2));
            assertFalse(relationService.isDriverEligible(driver3, bus3));
        }

        // Test Case 2 - Driver Has Enough Experience
        @Test
        void GivenElectricBusSufficientExperience_ShouldAccept() {
            Driver driver1 = new Driver("34ab!!cdAB", "Alice", 6, LicenseType.HEAVY, address, "01-01-1995");
            Driver driver2 = new Driver("34ab!!cdAC", "Bob", 8, LicenseType.HEAVY, address, "01-01-1995");
            Driver driver3 = new Driver("34ab!!cdAD", "Carol", 10, LicenseType.HEAVY, address, "01-01-1995");
            Bus bus1 = new Bus("11111115", 30, 80.0, FuelType.ELECTRICITY);
            Bus bus2 = new Bus("11111129", 30, 80.0, FuelType.ELECTRICITY);
            Bus bus3 = new Bus("11111130", 30, 80.0, FuelType.ELECTRICITY);
            assertTrue(relationService.isDriverEligible(driver1, bus1));
            assertTrue(relationService.isDriverEligible(driver2, bus2));
            assertTrue(relationService.isDriverEligible(driver3, bus3));
        }

        // Test Case 3 - Driver Has Exactly 5 Years Experience
        @Test
        void GivenElectricBusExactlyFiveYears_ShouldAccept() {
            Driver driver1 = new Driver("34ab!!cdAB", "Alice", 5, LicenseType.HEAVY, address, "01-01-1995");
            Driver driver2 = new Driver("34ab!!cdAC", "Bob", 5, LicenseType.HEAVY, address, "01-01-1990");
            Driver driver3 = new Driver("34ab!!cdAD", "Carol", 5, LicenseType.HEAVY, address, "01-01-1985");
            Bus bus1 = new Bus("11111119", 30, 80.0, FuelType.ELECTRICITY);
            Bus bus2 = new Bus("11111131", 30, 80.0, FuelType.ELECTRICITY);
            Bus bus3 = new Bus("11111132", 30, 80.0, FuelType.ELECTRICITY);
            assertTrue(relationService.isDriverEligible(driver1, bus1));
            assertTrue(relationService.isDriverEligible(driver2, bus2));
            assertTrue(relationService.isDriverEligible(driver3, bus3));
        }
    }

    // B5 - Driver License Restriction
    @Nested
    class ValidDriverLicence {
        String address = "124 | La Trobe St | Melbourne | Victoria | Australia";

        // Test Case 1 - Light/Medium License on Hybrid Bus
        @Test
        void GivenHybridBusLightOrMediumLicense_ShouldReject() {
            Driver driver1 = new Driver("34ab!!cdAB", "Alice", 8, LicenseType.LIGHT, address, "01-01-1990");
            Driver driver2 = new Driver("34ab!!cdAC", "Bob", 6, LicenseType.MEDIUM, address, "01-01-1988");
            Bus bus1 = new Bus("11111120", 30, 80.0, FuelType.HYBRID);
            Bus bus2 = new Bus("11111135", 30, 80.0, FuelType.HYBRID);
            assertFalse(relationService.isDriverEligible(driver1, bus1));
            assertFalse(relationService.isDriverEligible(driver2, bus2));
        }

        // Test Case 2 - Heavy/Public Transport License on Hybrid Bus
        @Test
        void GivenHybridBusHeavyOrPublicLicense_ShouldAccept() {
            Driver driver1 = new Driver("34ab!!cdAB", "Alice", 5, LicenseType.HEAVY, address, "01-01-1990");
            Driver driver2 = new Driver("34ab!!cdAC", "Bob", 5, LicenseType.PUBLIC_TRANSPORT, address, "01-01-1990");
            Bus bus1 = new Bus("11111142", 30, 80.0, FuelType.HYBRID);
            Bus bus2 = new Bus("11111143", 30, 80.0, FuelType.HYBRID);
            assertTrue(relationService.isDriverEligible(driver1, bus1));
            assertTrue(relationService.isDriverEligible(driver2, bus2));
        }

        // Test Case 3 - Light/Medium License on Electric Bus
        @Test
        void GivenElectricBusLightOrMediumLicense_ShouldReject() {
            Driver driver1 = new Driver("34ab!!cdAB", "Alice", 10, LicenseType.LIGHT, address, "01-01-1990");
            Driver driver2 = new Driver("34ab!!cdAC", "Bob", 10, LicenseType.MEDIUM, address, "01-01-1990");
            Bus bus1 = new Bus("11111139", 30, 80.0, FuelType.ELECTRICITY);
            Bus bus2 = new Bus("11111140", 30, 80.0, FuelType.ELECTRICITY);
            assertFalse(relationService.isDriverEligible(driver1, bus1));
            assertFalse(relationService.isDriverEligible(driver2, bus2));
        }

        // Test Case 4 - Heavy/Public Transport License on Electric Bus
        @Test
        void GivenElectricBusHeavyOrPublicLicense_ShouldAccept() {
            Driver driver1 = new Driver("34ab!!cdAB", "Alice", 5, LicenseType.HEAVY, address, "01-01-1990");
            Driver driver2 = new Driver("34ab!!cdAC", "Bob", 5, LicenseType.PUBLIC_TRANSPORT, address, "01-01-1990");
            Bus bus1 = new Bus("11111144", 30, 80.0, FuelType.ELECTRICITY);
            Bus bus2 = new Bus("11111145", 30, 80.0, FuelType.ELECTRICITY);
            assertTrue(relationService.isDriverEligible(driver1, bus1));
            assertTrue(relationService.isDriverEligible(driver2, bus2));
        }

        // Test Case 5 - Any License on Diesel Bus
        @Test
        void GivenDieselBusAnyLicense_ShouldAccept() {
            Driver driver1 = new Driver("34ab!!cdAB", "Alice", 2, LicenseType.LIGHT, address, "01-01-1995");
            Driver driver2 = new Driver("34ab!!cdAC", "Bob", 4, LicenseType.MEDIUM, address, "01-01-1990");
            Driver driver3 = new Driver("34ab!!cdAD", "Carol", 6, LicenseType.HEAVY, address, "01-01-1985");
            Driver driver4 = new Driver("34ab!!cdAE", "Danny", 6, LicenseType.PUBLIC_TRANSPORT, address, "01-01-1980");
            Bus bus1 = new Bus("11111117", 30, 80.0, FuelType.DIESEL);
            Bus bus2 = new Bus("11111137", 30, 80.0, FuelType.DIESEL);
            Bus bus3 = new Bus("11111138", 30, 80.0, FuelType.DIESEL);
            Bus bus4 = new Bus("11111146", 30, 80.0, FuelType.DIESEL);
            assertTrue(relationService.isDriverEligible(driver1, bus1));
            assertTrue(relationService.isDriverEligible(driver2, bus2));
            assertTrue(relationService.isDriverEligible(driver3, bus3));
            assertTrue(relationService.isDriverEligible(driver4, bus4));
        }
    }
}
