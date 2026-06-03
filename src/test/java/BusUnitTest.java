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
        void GivenValidBusID_ShouldReturnTrue() {
            assertTrue(busService.isValidBusID("12345678"));
            assertTrue(busService.isValidBusID("00000000"));
            assertTrue(busService.isValidBusID("99999999"));
        }
        // Test Case 2 – Too Short
        @Test
        void GivenTooShortBusID_ShouldReturnFalse() {
            assertFalse(busService.isValidBusID("1234567"));
            assertFalse(busService.isValidBusID("1234"));
            assertFalse(busService.isValidBusID("0"));
        }
        // Test Case 3 – Too Long
        @Test
        void GivenTooLongBusID_ShouldReturnFalse() {
            assertFalse(busService.isValidBusID("123456789"));
            assertFalse(busService.isValidBusID("1234567890"));
            assertFalse(busService.isValidBusID("123456789012"));
        }
        // Test Case 4 – Contains Letter
        @Test
        void GivenBusIDWithLetter_ShouldReturnFalse() {
            assertFalse(busService.isValidBusID("1234567A"));
            assertFalse(busService.isValidBusID("ABCDEFGH"));
            assertFalse(busService.isValidBusID("1234!678"));
        }
        // Test Case 5 - Null and Empty
        @Test
        void GivenNullOrEmpty_ShouldReturnFalse() {
            assertFalse(busService.isValidBusID(""));
            assertFalse(busService.isValidBusID(null));
        }
    }

    // B2 - Capacity Update Restriction
    @Nested
    class ValidBusCapacity {
        // Test Case 1 – Valid Lower Boundary Capacity
        @Test
        void GivenLowerBoundaryCapacity_ShouldReturnTrue() {
            assertTrue(busService.isValidCapacity(0));
            assertTrue(busService.isValidCapacity(1));
            assertTrue(busService.isValidCapacity(35));
        }
        // Test Case 2 – Valid Upper Boundary Capacity
        @Test
        void GivenUpperBoundaryCapacity_ShouldReturnTrue() {
            assertTrue(busService.isValidCapacity(70));
            assertTrue(busService.isValidCapacity(69));
        }
        // Test Case 3 – Negative Capacity
        @Test
        void GivenNegativeCapacity_ShouldReturnFalse() {
            assertFalse(busService.isValidCapacity(-1));
            assertFalse(busService.isValidCapacity(-100));
        }
        // Test Case 4 – Too High
        @Test
        void GivenOverLimitCapacity_ShouldReturnFalse() {
            assertFalse(busService.isValidCapacity(71));
            assertFalse(busService.isValidCapacity(72));
            assertFalse(busService.isValidCapacity(200));
        }
        // Test Case 5 – Valid Capacity Update
        @Test
        void GivenCapacityDecreased_ShouldUpdate() {
            Bus bus = new Bus("12345678", 60, 50.0, FuelType.DIESEL);
            busService.updateCapacity(bus, 30);
            assertEquals(30, bus.getCapacity());

            Bus bus2 = new Bus("12345679", 60, 50.0, FuelType.DIESEL);
            busService.updateCapacity(bus2, 0);
            assertEquals(0, bus2.getCapacity());

            Bus bus3 = new Bus("12345670", 50, 50.0, FuelType.DIESEL);
            busService.updateCapacity(bus3, 50);
            assertEquals(50, bus3.getCapacity());
        }
        // Test Case 6 – Capacity Update Too High
        @Test
        void GivenCapacityIncreased_ShouldThrow() {
            Bus bus = new Bus("12345678", 60, 50.0, FuelType.DIESEL);
            assertThrows(IllegalStateException.class, () -> busService.updateCapacity(bus, 70));

            Bus bus2 = new Bus("12345679", 50, 50.0, FuelType.DIESEL);
            assertThrows(IllegalStateException.class, () -> busService.updateCapacity(bus2, 51));
        }
        // Test Case 7 – Capacity Negative
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
        void GivenDriverOver50WithLargeBus_ShouldReturnFalse() {
            Driver driver1 = new Driver("34ab!!cdAB", "Alice", 15, LicenceType.HEAVY, address, "01-01-1970");
            Driver driver2 = new Driver("34ab!!cdAC", "Bob", 20, LicenceType.HEAVY, address, "01-01-1965");
            Bus bus1 = new Bus("11111111", 50, 80.0, FuelType.DIESEL);
            Bus bus2 = new Bus("11111121", 55, 80.0, FuelType.DIESEL);
            assertFalse(relationService.isDriverEligible(driver1, bus1));
            assertFalse(relationService.isDriverEligible(driver2, bus2));
        }

        // Test Case 2 - Driver Over 50 With Small Bus
        @Test
        void GivenDriverOver50WithSmallBus_ShouldReturnTrue() {
            Driver driver1 = new Driver("34ab!!cdAB", "Alice", 15, LicenceType.HEAVY, address, "01-01-1970");
            Driver driver2 = new Driver("34ab!!cdAC", "Bob", 20, LicenceType.HEAVY, address, "01-01-1965");
            Bus bus1 = new Bus("11111112", 30, 80.0, FuelType.DIESEL);
            Bus bus2 = new Bus("11111123", 25, 80.0, FuelType.DIESEL);
            assertTrue(relationService.isDriverEligible(driver1, bus1));
            assertTrue(relationService.isDriverEligible(driver2, bus2));
        }

        // Test Case 3 - Driver Aged Exactly 50
        @Test
        void GivenDriverExactlyAge50_ShouldReturnTrue() {
            LocalDate dob = LocalDate.now().minusYears(50);
            String birthdate = String.format("%02d-%02d-%04d", dob.getDayOfMonth(), dob.getMonthValue(), dob.getYear());

            Driver driver1 = new Driver("34ab!!cdAB", "Alice", 15, LicenceType.HEAVY, address, birthdate);
            Driver driver2 = new Driver("34ab!!cdAC", "Bob", 10, LicenceType.HEAVY, address, birthdate);
            Bus bus1 = new Bus("11111118", 50, 80.0, FuelType.DIESEL);
            Bus bus2 = new Bus("11111119", 60, 80.0, FuelType.DIESEL);
            assertTrue(relationService.isDriverEligible(driver1, bus1));
            assertTrue(relationService.isDriverEligible(driver2, bus2));
        }
    }

    // B4 - Electric Bus Restriction
    @Nested
    class ValidElectricDriver {
        String address = "124 | La Trobe St | Melbourne | Victoria | Australia";

        // Test Case 1 - Driver Does Not Have Enough Experience
        @Test
        void GivenElectricBusUnderExperience_ShouldReturnFalse() {
            Driver driver1 = new Driver("34ab!!cdAB", "Alice", 4, LicenceType.HEAVY, address, "01-01-1995");
            Driver driver2 = new Driver("34ab!!cdAC", "Bob", 2, LicenceType.HEAVY, address, "01-01-1995");
            Driver driver3 = new Driver("34ab!!cdAD", "Carol", 1, LicenceType.HEAVY, address, "01-01-1995");
            Bus bus1 = new Bus("11111113", 30, 80.0, FuelType.ELECTRICITY);
            Bus bus2 = new Bus("11111127", 30, 80.0, FuelType.ELECTRICITY);
            Bus bus3 = new Bus("11111128", 30, 80.0, FuelType.ELECTRICITY);
            assertFalse(relationService.isDriverEligible(driver1, bus1));
            assertFalse(relationService.isDriverEligible(driver2, bus2));
            assertFalse(relationService.isDriverEligible(driver3, bus3));
        }

        // Test Case 2 - Driver Has Enough Experience
        @Test
        void GivenElectricBusSufficientExperience_ShouldReturnTrue() {
            Driver driver1 = new Driver("34ab!!cdAB", "Alice", 5, LicenceType.HEAVY, address, "01-01-1995");
            Driver driver2 = new Driver("34ab!!cdAC", "Bob", 8, LicenceType.HEAVY, address, "01-01-1995");
            Driver driver3 = new Driver("34ab!!cdAD", "Carol", 10, LicenceType.HEAVY, address, "01-01-1995");
            Bus bus1 = new Bus("11111115", 30, 80.0, FuelType.ELECTRICITY);
            Bus bus2 = new Bus("11111129", 30, 80.0, FuelType.ELECTRICITY);
            Bus bus3 = new Bus("11111130", 30, 80.0, FuelType.ELECTRICITY);
            assertTrue(relationService.isDriverEligible(driver1, bus1));
            assertTrue(relationService.isDriverEligible(driver2, bus2));
            assertTrue(relationService.isDriverEligible(driver3, bus3));
        }
    }

    // B5 - Driver Licence Restriction
    @Nested
    class ValidDriverLicence {
        String address = "124 | La Trobe St | Melbourne | Victoria | Australia";

        // Test Case 1 - Light/Medium Licence on Hybrid Bus
        @Test
        void GivenHybridBusLightOrMediumLicence_ShouldReturnFalse() {
            Driver driver1 = new Driver("34ab!!cdAB", "Alice", 8, LicenceType.LIGHT, address, "01-01-1990");
            Driver driver2 = new Driver("34ab!!cdAC", "Bob", 6, LicenceType.MEDIUM, address, "01-01-1988");
            Bus bus1 = new Bus("11111120", 30, 80.0, FuelType.HYBRID);
            Bus bus2 = new Bus("11111135", 30, 80.0, FuelType.HYBRID);
            assertFalse(relationService.isDriverEligible(driver1, bus1));
            assertFalse(relationService.isDriverEligible(driver2, bus2));
        }

        // Test Case 2 - Heavy/Public Transport Licence on Hybrid Bus
        @Test
        void GivenHybridBusHeavyOrPublicLicence_ShouldReturnTrue() {
            Driver driver1 = new Driver("34ab!!cdAB", "Alice", 5, LicenceType.HEAVY, address, "01-01-1990");
            Driver driver2 = new Driver("34ab!!cdAC", "Bob", 5, LicenceType.PUBLIC_TRANSPORT, address, "01-01-1990");
            Bus bus1 = new Bus("11111142", 30, 80.0, FuelType.HYBRID);
            Bus bus2 = new Bus("11111143", 30, 80.0, FuelType.HYBRID);
            assertTrue(relationService.isDriverEligible(driver1, bus1));
            assertTrue(relationService.isDriverEligible(driver2, bus2));
        }

        // Test Case 3 - Light/Medium Licence on Electric Bus
        @Test
        void GivenElectricBusLightOrMediumLicence_ShouldReturnFalse() {
            Driver driver1 = new Driver("34ab!!cdAB", "Alice", 10, LicenceType.LIGHT, address, "01-01-1990");
            Driver driver2 = new Driver("34ab!!cdAC", "Bob", 10, LicenceType.MEDIUM, address, "01-01-1990");
            Bus bus1 = new Bus("11111139", 30, 80.0, FuelType.ELECTRICITY);
            Bus bus2 = new Bus("11111140", 30, 80.0, FuelType.ELECTRICITY);
            assertFalse(relationService.isDriverEligible(driver1, bus1));
            assertFalse(relationService.isDriverEligible(driver2, bus2));
        }

        // Test Case 4 - Heavy/Public Transport Licence on Electric Bus
        @Test
        void GivenElectricBusHeavyOrPublicLicence_ShouldReturnTrue() {
            Driver driver1 = new Driver("34ab!!cdAB", "Alice", 5, LicenceType.HEAVY, address, "01-01-1990");
            Driver driver2 = new Driver("34ab!!cdAC", "Bob", 5, LicenceType.PUBLIC_TRANSPORT, address, "01-01-1990");
            Bus bus1 = new Bus("11111144", 30, 80.0, FuelType.ELECTRICITY);
            Bus bus2 = new Bus("11111145", 30, 80.0, FuelType.ELECTRICITY);
            assertTrue(relationService.isDriverEligible(driver1, bus1));
            assertTrue(relationService.isDriverEligible(driver2, bus2));
        }

        // Test Case 5 - Any Licence on Diesel Bus
        @Test
        void GivenDieselBusAnyLicence_ShouldReturnTrue() {
            Driver driver1 = new Driver("34ab!!cdAB", "Alice", 2, LicenceType.LIGHT, address, "01-01-1995");
            Driver driver2 = new Driver("34ab!!cdAC", "Bob", 4, LicenceType.MEDIUM, address, "01-01-1990");
            Driver driver3 = new Driver("34ab!!cdAD", "Carol", 6, LicenceType.HEAVY, address, "01-01-1985");
            Driver driver4 = new Driver("34ab!!cdAE", "Danny", 6, LicenceType.PUBLIC_TRANSPORT, address, "01-01-1980");
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
