import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class BusDriverRelationUnitTest {
    private final BusDriverRelationService service = new BusDriverRelationService(new BusDriverRelationRepository());

    @Nested
    class ValidDriverAge{
        //Test Case 1 - Invalid Driver Over 50 With Too High Capacity
        @Test
        void Eligibility_DriverOver50_LargeBus_returns_False() {
            Driver driver1 = new Driver("34ab!!cdAB", "Alice", 15, LicenseType.HEAVY,
                    "124 | La Trobe St | Melbourne | Victoria | Australia", "01-01-1970");
            Driver driver2 = new Driver("34ab!!cdAB", "Bob", 20, LicenseType.HEAVY,
                    "124 | La Trobe St | Melbourne | Victoria | Australia", "01-01-1965");
            Driver driver3 = new Driver("34ab!!cdAB", "Carol", 10, LicenseType.HEAVY,
                    "124 | La Trobe St | Melbourne | Victoria | Australia", "01-01-1960");
            Bus bus1 = new Bus("11111111", 50, 80.0, FuelType.DIESEL);
            Bus bus2 = new Bus("11111121", 55, 80.0, FuelType.DIESEL);
            Bus bus3 = new Bus("11111122", 60, 80.0, FuelType.DIESEL);
            assertFalse(service.isDriverEligible(driver1, bus1));
            assertFalse(service.isDriverEligible(driver2, bus2));
            assertFalse(service.isDriverEligible(driver3, bus3));
        }

        //Test Case 2 - Valid Driver Over 50 With Valid Capacity
        @Test
        void Eligibility_DriverOver50_SmallBus_returns_True() {
            Driver driver1 = new Driver("34ab!!cdAB", "Alice", 15, LicenseType.HEAVY,
                    "124 | La Trobe St | Melbourne | Victoria | Australia", "01-01-1970");
            Driver driver2 = new Driver("34ab!!cdAB", "Bob", 20, LicenseType.HEAVY,
                    "124 | La Trobe St | Melbourne | Victoria | Australia", "01-01-1965");
            Driver driver3 = new Driver("34ab!!cdAB", "Carol", 10, LicenseType.HEAVY,
                    "124 | La Trobe St | Melbourne | Victoria | Australia", "01-01-1960");
            Bus bus1 = new Bus("11111112", 30, 80.0, FuelType.DIESEL);
            Bus bus2 = new Bus("11111123", 25, 80.0, FuelType.DIESEL);
            Bus bus3 = new Bus("11111124", 20, 80.0, FuelType.DIESEL);
            assertTrue(service.isDriverEligible(driver1, bus1));
            assertTrue(service.isDriverEligible(driver2, bus2));
            assertTrue(service.isDriverEligible(driver3, bus3));
        }

        //Test Case 3 - Valid Driver Aged Exactly 50
        @Test
        void Eligibility_DriverExactlyAge50_CapacityFiftyBus_returns_True() {
            java.time.LocalDate dob1 = java.time.LocalDate.now().minusYears(50);
            java.time.LocalDate dob2 = java.time.LocalDate.now().minusYears(50);
            java.time.LocalDate dob3 = java.time.LocalDate.now().minusYears(50);

            String birthdate1 = String.format("%02d-%02d-%04d",
                    dob1.getDayOfMonth(), dob1.getMonthValue(), dob1.getYear());
            String birthdate2 = String.format("%02d-%02d-%04d",
                    dob2.getDayOfMonth(), dob2.getMonthValue(), dob2.getYear());
            String birthdate3 = String.format("%02d-%02d-%04d",
                    dob3.getDayOfMonth(), dob3.getMonthValue(), dob3.getYear());

            Driver driver1 = new Driver("34ab!!cdAB", "Alice", 15, LicenseType.HEAVY,
                    "124 | La Trobe St | Melbourne | Victoria | Australia", birthdate1);
            Driver driver2 = new Driver("34ab!!cdAB", "Bob", 10, LicenseType.HEAVY,
                    "124 | La Trobe St | Melbourne | Victoria | Australia", birthdate2);
            Driver driver3 = new Driver("34ab!!cdAB", "Carol", 5, LicenseType.HEAVY,
                    "124 | La Trobe St | Melbourne | Victoria | Australia", birthdate3);

            Bus bus1 = new Bus("11111118", 50, 80.0, FuelType.DIESEL);
            Bus bus2 = new Bus("11111125", 50, 80.0, FuelType.DIESEL);
            Bus bus3 = new Bus("11111126", 50, 80.0, FuelType.DIESEL);

            assertTrue(service.isDriverEligible(driver1, bus1));
            assertTrue(service.isDriverEligible(driver2, bus2));
            assertTrue(service.isDriverEligible(driver3, bus3));
        }
    }

    @Nested
    class ValidElectricDriver{
        //Test Case 1 - Driver Does Not Have Enough Experience
        @Test
        void Eligibility_ElectricBus_UnderExperience_returns_False() {
            Driver driver1 = new Driver("34ab!!cdAB", "Bob", 3, LicenseType.HEAVY,
                    "124 | La Trobe St | Melbourne | Victoria | Australia", "01-01-1995");
            Driver driver2 = new Driver("34ab!!cdAB", "Eve", 2, LicenseType.HEAVY,
                    "124 | La Trobe St | Melbourne | Victoria | Australia", "01-01-1995");
            Driver driver3 = new Driver("34ab!!cdAB", "Frank", 1, LicenseType.HEAVY,
                    "124 | La Trobe St | Melbourne | Victoria | Australia", "01-01-1995");
            Bus bus1 = new Bus("11111113", 30, 80.0, FuelType.ELECTRICITY);
            Bus bus2 = new Bus("11111127", 30, 80.0, FuelType.ELECTRICITY);
            Bus bus3 = new Bus("11111128", 30, 80.0, FuelType.ELECTRICITY);
            assertFalse(service.isDriverEligible(driver1, bus1));
            assertFalse(service.isDriverEligible(driver2, bus2));
            assertFalse(service.isDriverEligible(driver3, bus3));
        }

        //Test Case 2 - Driver Has Enough Experience
        @Test
        void Eligibility_ElectricBus_HeavyLicense_SufficientExp_returns_True() {
            Driver driver1 = new Driver("34ab!!cdAB", "Dave", 6, LicenseType.HEAVY,
                    "124 | La Trobe St | Melbourne | Victoria | Australia", "01-01-1995");
            Driver driver2 = new Driver("34ab!!cdAB", "Grace", 8, LicenseType.HEAVY,
                    "124 | La Trobe St | Melbourne | Victoria | Australia", "01-01-1995");
            Driver driver3 = new Driver("34ab!!cdAB", "Hank", 10, LicenseType.HEAVY,
                    "124 | La Trobe St | Melbourne | Victoria | Australia", "01-01-1995");
            Bus bus1 = new Bus("11111115", 30, 80.0, FuelType.ELECTRICITY);
            Bus bus2 = new Bus("11111129", 30, 80.0, FuelType.ELECTRICITY);
            Bus bus3 = new Bus("11111130", 30, 80.0, FuelType.ELECTRICITY);
            assertTrue(service.isDriverEligible(driver1, bus1));
            assertTrue(service.isDriverEligible(driver2, bus2));
            assertTrue(service.isDriverEligible(driver3, bus3));
        }

        //Test Case 3 - Driver Has Exactly 5 Years Experience
        @Test
        void Eligibility_ElectricBus_ExactlyFiveYears_returns_True() {
            Driver driver1 = new Driver("34ab!!cdAB", "Bob", 5, LicenseType.HEAVY,
                    "124 | La Trobe St | Melbourne | Victoria | Australia", "01-01-1995");
            Driver driver2 = new Driver("34ab!!cdAB", "Ivy", 5, LicenseType.HEAVY,
                    "124 | La Trobe St | Melbourne | Victoria | Australia", "01-01-1990");
            Driver driver3 = new Driver("34ab!!cdAB", "Jake", 5, LicenseType.HEAVY,
                    "124 | La Trobe St | Melbourne | Victoria | Australia", "01-01-1985");
            Bus bus1 = new Bus("11111119", 30, 80.0, FuelType.ELECTRICITY);
            Bus bus2 = new Bus("11111131", 30, 80.0, FuelType.ELECTRICITY);
            Bus bus3 = new Bus("11111132", 30, 80.0, FuelType.ELECTRICITY);
            assertTrue(service.isDriverEligible(driver1, bus1));
            assertTrue(service.isDriverEligible(driver2, bus2));
            assertTrue(service.isDriverEligible(driver3, bus3));
        }
    }

    @Nested
    class ValidDriverLicence{
        //Test Case 1 - Driver Does Not Have Correct License Type (Light)
        @Test
        void Eligibility_HybridBus_LightLicense_returns_False() {
            Driver driver1 = new Driver("34ab!!cdAB", "Eve", 10, LicenseType.LIGHT,
                    "124 | La Trobe St | Melbourne | Victoria | Australia", "01-01-1995");
            Driver driver2 = new Driver("34ab!!cdAB", "Kim", 8, LicenseType.LIGHT,
                    "124 | La Trobe St | Melbourne | Victoria | Australia", "01-01-1990");
            Driver driver3 = new Driver("34ab!!cdAB", "Leo", 6, LicenseType.LIGHT,
                    "124 | La Trobe St | Melbourne | Victoria | Australia", "01-01-1985");
            Bus bus1 = new Bus("11111116", 30, 80.0, FuelType.HYBRID);
            Bus bus2 = new Bus("11111133", 30, 80.0, FuelType.HYBRID);
            Bus bus3 = new Bus("11111134", 30, 80.0, FuelType.HYBRID);
            assertFalse(service.isDriverEligible(driver1, bus1));
            assertFalse(service.isDriverEligible(driver2, bus2));
            assertFalse(service.isDriverEligible(driver3, bus3));
        }

        //Test Case 2 - Driver Does Not Have Correct License Type (Medium)
        @Test
        void Eligibility_HybridBus_MediumLicense_returns_False() {
            Driver driver1 = new Driver("34ab!!cdAB", "Carol", 8, LicenseType.MEDIUM,
                    "124 | La Trobe St | Melbourne | Victoria | Australia", "01-01-1990");
            Driver driver2 = new Driver("34ab!!cdAB", "Mia", 6, LicenseType.MEDIUM,
                    "124 | La Trobe St | Melbourne | Victoria | Australia", "01-01-1988");
            Driver driver3 = new Driver("34ab!!cdAB", "Ned", 4, LicenseType.MEDIUM,
                    "124 | La Trobe St | Melbourne | Victoria | Australia", "01-01-1985");
            Bus bus1 = new Bus("11111120", 30, 80.0, FuelType.HYBRID);
            Bus bus2 = new Bus("11111135", 30, 80.0, FuelType.HYBRID);
            Bus bus3 = new Bus("11111136", 30, 80.0, FuelType.HYBRID);
            assertFalse(service.isDriverEligible(driver1, bus1));
            assertFalse(service.isDriverEligible(driver2, bus2));
            assertFalse(service.isDriverEligible(driver3, bus3));
        }

        //Test Case 3 - Driver Has Correct License Type
        @Test
        void Eligibility_DieselBus_LightLicense_returns_True() {
            Driver driver1 = new Driver("34ab!!cdAB", "Frank", 2, LicenseType.LIGHT,
                    "124 | La Trobe St | Melbourne | Victoria | Australia", "01-01-1995");
            Driver driver2 = new Driver("34ab!!cdAB", "Ora", 4, LicenseType.LIGHT,
                    "124 | La Trobe St | Melbourne | Victoria | Australia", "01-01-1990");
            Driver driver3 = new Driver("34ab!!cdAB", "Pat", 6, LicenseType.LIGHT,
                    "124 | La Trobe St | Melbourne | Victoria | Australia", "01-01-1985");
            Bus bus1 = new Bus("11111117", 30, 80.0, FuelType.DIESEL);
            Bus bus2 = new Bus("11111137", 30, 80.0, FuelType.DIESEL);
            Bus bus3 = new Bus("11111138", 30, 80.0, FuelType.DIESEL);
            assertTrue(service.isDriverEligible(driver1, bus1));
            assertTrue(service.isDriverEligible(driver2, bus2));
            assertTrue(service.isDriverEligible(driver3, bus3));
        }
    }
}