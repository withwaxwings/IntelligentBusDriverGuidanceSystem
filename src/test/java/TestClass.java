//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import org.junit.jupiter.api.Test;
//
//public class TestClass {
//
//    // Reusable service instances (stateless validators)
//    private final DriverService driverService = new DriverService(new DriverRepository());
//    private final BusService busService = new BusService(new BusRepository());
//    private final BusDriverRelationService relationService = new BusDriverRelationService(new BusDriverRelationRepository());
//
//    @Test
//    void TestFunction() {
//        boolean iAmTrue = true;
//        boolean iAmFalse = false;
//        assertTrue(iAmTrue);
//        assertFalse(iAmFalse);
//    }
//
//    //DriverService – isValidDriverID
//
//    @Test
//    void TooLong_DriverID_returns_False() {
//        assertFalse(driverService.isValidDriverID("12345678910"));
//    }
//
//    @Test
//    void TooShort_DriverID_returns_False() {
//        assertFalse(driverService.isValidDriverID("34ab!!AB"));
//    }
//
//    @Test
//    void Valid_DriverID_returns_True() {
//        assertTrue(driverService.isValidDriverID("34ab!!cdAB"));
//    }
//
//    @Test
//    void DriverID_FirstDigitOne_returns_False() {
//        // First two chars must be [2-9]; '1' is not in that range
//        assertFalse(driverService.isValidDriverID("14ab!!cdAB"));
//    }
//
//    @Test
//    void DriverID_LastCharsLowercase_returns_False() {
//        assertFalse(driverService.isValidDriverID("34ab!!cdab"));
//    }
//
//    @Test
//    void DriverID_OneSpecialInMiddle_returns_False() {
//        // Only 1 special char in positions 2-7 — needs at least 2
//        assertFalse(driverService.isValidDriverID("34abc!efAB"));
//    }
//
//    @Test
//    void DriverID_ExactlyTwoSpecials_returns_True() {
//        assertTrue(driverService.isValidDriverID("34a!!bcdAB"));
//    }
//
//     @Test
//    void DriverID_DifferentValidSpecials_returns_True() {
//        assertTrue(driverService.isValidDriverID("56##xyz1ZZ"));
//    }
//
//    @Test
//    void DriverID_Null_returns_False() {
//        assertFalse(driverService.isValidDriverID(null));
//    }
//
//    //DriverService – isValidAddress
//
//    @Test
//    void Incorrect_Address_Format_returnsFalse() {
//        assertFalse(driverService.isValidAddress("12 Pork Street"));
//    }
//
//    @Test
//    void Correct_Address_Format_returns_True() {
//        assertTrue(driverService.isValidAddress("12|Pork Parade|Echuca|Victoria|Australia"));
//    }
//
//    @Test
//    void Address_EmptyPart_returns_False() {
//        assertFalse(driverService.isValidAddress("12||Echuca|Victoria|Australia"));
//    }
//
//    @Test
//    void Address_SixParts_returns_False() {
//        assertFalse(driverService.isValidAddress("12|Pork Parade|Echuca|Victoria|Australia|Extra"));
//    }
//
//    @Test
//    void Address_Null_returns_False() {
//        assertFalse(driverService.isValidAddress(null));
//    }
//
//    //DriverService – isValidBirthdate
//
//    @Test
//    void Correct_BirthDate_returns_True() {
//        assertTrue(driverService.isValidBirthdate("11-09-2005"));
//    }
//
//    @Test
//    void Incorrect_BirthDate_returns_False() {
//        assertFalse(driverService.isValidBirthdate("11/09/2005"));
//    }
//
//    @Test
//    void BirthDate_InvalidDay_returns_False() {
//        assertFalse(driverService.isValidBirthdate("32-01-2000"));
//    }
//
//    @Test
//    void BirthDate_InvalidMonth_returns_False() {
//        assertFalse(driverService.isValidBirthdate("01-13-2000"));
//    }
//
//
//    @Test
//    void BirthDate_Null_returns_False() {
//        assertFalse(driverService.isValidBirthdate(null));
//    }
//
//    //DriverService – updateLicenseType
//
//    @Test
//    void Invalid_Driver_License_Change_throws_IllegalArgument() {
//        // Driver with >10 years experience cannot change license type
//        Driver driver = new Driver("34ab!!cdAB", "John", 11, LicenseType.HEAVY,
//                "12|Pork Parade|Echuca|Victoria|Australia", "01-01-1990");
//        assertThrows(IllegalArgumentException.class,
//                () -> driverService.updateLicenseType(driver, LicenseType.MEDIUM));
//    }
//
//    @Test
//    void Valid_Driver_License_Change_succeeds() {
//        Driver driver = new Driver("34ab!!cdAB", "John", 5, LicenseType.LIGHT,
//                "12|Pork Parade|Echuca|Victoria|Australia", "01-01-1990");
//        driverService.updateLicenseType(driver, LicenseType.HEAVY);
//        assertEquals(LicenseType.HEAVY, driver.getLicenseType());
//    }
//
//    @Test
//    void LicenseChange_ExactlyTenYears_succeeds() {
//        Driver driver = new Driver(
//                "34ab!!cdAB",
//                "Bob",
//                10,
//                LicenseType.LIGHT,
//                "1|A St|Melbourne|VIC|Australia",
//                "01-01-1985"
//        );
//
//        assertDoesNotThrow(() ->
//                driverService.updateLicenseType(driver, LicenseType.MEDIUM));
//    }
//
//
//
//    //DriverService – updateAddress
//
//    @Test
//    void SetAddress_Valid_succeeds() {
//        Driver driver = new Driver("34ab!!cdAB", "John", 5, LicenseType.LIGHT,
//                "12|Pork Parade|Echuca|Victoria|Australia", "01-01-1990");
//        driverService.updateAddress(driver, "99|New Road|Melbourne|Victoria|Australia");
//        assertEquals("99|New Road|Melbourne|Victoria|Australia", driver.getAddress());
//    }
//
//    @Test
//    void SetAddress_Invalid_throws_IllegalArgument() {
//        Driver driver = new Driver("34ab!!cdAB", "John", 5, LicenseType.LIGHT,
//                "12|Pork Parade|Echuca|Victoria|Australia", "01-01-1990");
//        assertThrows(IllegalArgumentException.class,
//                () -> driverService.updateAddress(driver, "Bad Address"));
//    }
//
//    // Driver - Immutable fields
//
//    @Test
//    void DriverID_Immutable_returns_OriginalValue() {
//        Driver driver = new Driver(
//                "34ab!!cdAB",
//                "Dave",
//                3,
//                LicenseType.LIGHT,
//                "1|A St|Melbourne|VIC|Australia",
//                "01-01-2000"
//        );
//
//        assertEquals("34ab!!cdAB",
//                driver.getDriverID());
//    }
//
//    @Test
//    void Name_Immutable_returns_OriginalValue() {
//        Driver driver = new Driver(
//                "34ab!!cdAB",
//                "Eve",
//                3,
//                LicenseType.LIGHT,
//                "1|A St|Melbourne|VIC|Australia",
//                "01-01-2000"
//        );
//
//        assertEquals("Eve",
//                driver.getName());
//    }
//
//    @Test
//    void Driver_NoSetDriverID_or_SetName_methods() {
//        boolean hasMutableSetter = false;
//        for (java.lang.reflect.Method m : Driver.class.getMethods()) {
//            if (m.getName().equals("setDriverID") ||
//                    m.getName().equals("setName")) {
//                hasMutableSetter = true;
//                break;
//            }
//        }
//
//        assertFalse(hasMutableSetter);
//    }
//
//
//    //BusService – isValidBusID
//
//    @Test
//    void Valid_BusID_returns_True() {
//        assertTrue(busService.isValidBusID("12345678"));
//    }
//
//    @Test
//    void TooShort_BusID_returns_False() {
//        assertFalse(busService.isValidBusID("1234567"));
//    }
//
//    @Test
//    void TooLong_BusID_returns_False() {
//        assertFalse(busService.isValidBusID("123456789"));
//    }
//
//    @Test
//    void BusID_ContainsLetter_returns_False() {
//        assertFalse(busService.isValidBusID("1234567A"));
//    }
//
//    //BusService – isValidCapacity
//    @Test
//    void Capacity_LowerBoundary_returns_True() {
//        assertTrue(busService.isValidCapacity(1));
//    }
//
//    @Test
//    void Capacity_UpperBoundary_returns_True() {
//        // Upper bound is 70 per BusService implementation
//        assertTrue(busService.isValidCapacity(70));
//    }
//
//    @Test
//    void Capacity_Zero_returns_False() {
//        assertFalse(busService.isValidCapacity(0));
//    }
//
//    @Test
//    void Capacity_OverLimit_returns_False() {
//        assertFalse(busService.isValidCapacity(71));
//    }
//
//    //BusService – isValidFuelLevel
//
//    @Test
//    void FuelLevel_Zero_returns_True() {
//        assertTrue(busService.isValidFuelLevel(0.0));
//    }
//
//    @Test
//    void FuelLevel_OneHundred_returns_True() {
//        assertTrue(busService.isValidFuelLevel(100.0));
//    }
//
//    @Test
//    void FuelLevel_Negative_returns_False() {
//        assertFalse(busService.isValidFuelLevel(-1.0));
//    }
//
//    @Test
//    void FuelLevel_OverHundred_returns_False() {
//        assertFalse(busService.isValidFuelLevel(100.1));
//    }
//
//    //BusService – updateCapacity
//
//    @Test
//    void SetCapacity_Decrease_succeeds() {
//        Bus bus = new Bus("12345678", 60, 50.0, FuelType.DIESEL);
//        busService.updateCapacity(bus, 50);
//        assertEquals(50, bus.getCapacity());
//    }
//
//    @Test
//    void SetCapacity_Increase_throws_IllegalState() {
//        Bus bus = new Bus("12345678", 50, 50.0, FuelType.DIESEL);
//        assertThrows(IllegalStateException.class, () -> busService.updateCapacity(bus, 60));
//    }
//
//    @Test
//    void SetCapacity_Zero_throws_IllegalArgument() {
//        Bus bus = new Bus("12345678", 50, 50.0, FuelType.DIESEL);
//        assertThrows(IllegalArgumentException.class, () -> busService.updateCapacity(bus, 0));
//    }
//
//    //BusService – updateFuelLevel
//
//    @Test
//    void SetFuelLevel_Valid_succeeds() {
//        Bus bus = new Bus("12345678", 50, 50.0, FuelType.DIESEL);
//        busService.updateFuelLevel(bus, 75.0);
//        assertEquals(75.0, bus.getFuelLevel());
//    }
//
//    @Test
//    void SetFuelLevel_Negative_throws_IllegalArgument() {
//        Bus bus = new Bus("12345678", 50, 50.0, FuelType.DIESEL);
//        assertThrows(IllegalArgumentException.class, () -> busService.updateFuelLevel(bus, -5.0));
//    }
//
//    //BusService – updateFuelType
//
//    @Test
//    void SetFuelType_Valid_succeeds() {
//        Bus bus = new Bus("12345678", 50, 50.0, FuelType.DIESEL);
//        busService.updateFuelType(bus, FuelType.HYBRID);
//        assertEquals(FuelType.HYBRID, bus.getFuelType());
//    }
//
//    //BusRepository
//
//    @Test
//    void BusRepo_Add_Then_Retrieve_succeeds() {
//        BusRepository repo = new BusRepository();
//        Bus bus = new Bus("99999991", 40, 60.0, FuelType.DIESEL);
//        repo.add(bus);
//        assertNotNull(repo.retrieve("99999991"));
//    }
//
//    @Test
//    void BusRepo_Add_Duplicate_throws_IllegalArgument() {
//        BusRepository repo = new BusRepository();
//        Bus bus = new Bus("99999992", 40, 60.0, FuelType.DIESEL);
//        repo.add(bus);
//        assertThrows(IllegalArgumentException.class,
//                () -> repo.add(new Bus("99999992", 30, 40.0, FuelType.HYBRID)));
//    }
//
//    @Test
//    void BusRepo_Retrieve_UnknownID_returns_Null() {
//        BusRepository repo = new BusRepository();
//        assertNull(repo.retrieve("00000000"));
//    }
//
//    @Test
//    void BusRepo_Count_increases_after_add() {
//        BusRepository repo = new BusRepository();
//        int before = repo.count();
//        repo.add(new Bus("99999993", 40, 60.0, FuelType.DIESEL));
//        assertEquals(before + 1, repo.count());
//    }
//
//    //BusDriverRelationService – isDriverEligible
//
//    @Test
//    void Eligibility_DriverOver50_LargeBus_returns_False() {
//        BusDriverRelationService service =
//                new BusDriverRelationService(new BusDriverRelationRepository());
//        Driver driver = new Driver("34ab!!cdAB", "Alice", 15, LicenseType.HEAVY,
//                "1|St|City|ST|USA", "01-01-1970");
//        Bus bus = new Bus("11111111", 50, 80.0, FuelType.DIESEL);
//        assertFalse(service.isDriverEligible(driver, bus));
//    }
//
//    @Test
//    void Eligibility_DriverOver50_SmallBus_returns_True() {
//        BusDriverRelationService service =
//                new BusDriverRelationService(new BusDriverRelationRepository());
//        Driver driver = new Driver("34ab!!cdAB", "Alice", 15, LicenseType.HEAVY,
//                "1|St|City|ST|USA", "01-01-1970");
//        Bus bus = new Bus("11111112", 30, 80.0, FuelType.DIESEL);
//        assertTrue(service.isDriverEligible(driver, bus));
//    }
//
//    @Test
//    void Eligibility_DriverExactlyAge50_CapacityFiftyBus_returns_True() {
//        java.time.LocalDate dob =
//                java.time.LocalDate.now().minusYears(50);
//
//        String birthdate = String.format(
//                "%02d-%02d-%04d",
//                dob.getDayOfMonth(),
//                dob.getMonthValue(),
//                dob.getYear()
//        );
//
//        Driver driver = new Driver(
//                "34ab!!cdAB",
//                "Alice",
//                15,
//                LicenseType.HEAVY,
//                "1|St|City|ST|Australia",
//                birthdate
//        );
//
//        Bus bus = new Bus("11111118", 50, 80.0, FuelType.DIESEL);
//
//        assertTrue(relationService.isDriverEligible(driver, bus));
//    }
//
//    @Test
//    void Eligibility_ElectricBus_UnderExperience_returns_False() {
//        BusDriverRelationService service =
//                new BusDriverRelationService(new BusDriverRelationRepository());
//        Driver driver = new Driver("34ab!!cdAB", "Bob", 3, LicenseType.HEAVY,
//                "1|St|City|ST|USA", "01-01-1995");
//        Bus bus = new Bus("11111113", 30, 80.0, FuelType.ELECTRICITY);
//        assertFalse(service.isDriverEligible(driver, bus));
//    }
//
//    @Test
//    void Eligibility_ElectricBus_HeavyLicense_SufficientExp_returns_True() {
//        BusDriverRelationService service =
//                new BusDriverRelationService(new BusDriverRelationRepository());
//        Driver driver = new Driver("34ab!!cdAB", "Dave", 6, LicenseType.HEAVY,
//                "1|St|City|ST|USA", "01-01-1995");
//        Bus bus = new Bus("11111115", 30, 80.0, FuelType.ELECTRICITY);
//        assertTrue(service.isDriverEligible(driver, bus));
//    }
//
//      @Test
//    void Eligibility_ElectricBus_ExactlyFiveYears_returns_True() {
//        Driver driver = new Driver(
//                "34ab!!cdAB",
//                "Bob",
//                5,
//                LicenseType.HEAVY,
//                "1|St|City|ST|Australia",
//                "01-01-1995"
//        );
//
//        Bus bus = new Bus("11111119", 30, 80.0, FuelType.ELECTRICITY);
//
//        assertTrue(relationService.isDriverEligible(driver, bus));
//    }
//
//    @Test
//    void Eligibility_HybridBus_LightLicense_returns_False() {
//        BusDriverRelationService service =
//                new BusDriverRelationService(new BusDriverRelationRepository());
//        Driver driver = new Driver("34ab!!cdAB", "Eve", 10, LicenseType.LIGHT,
//                "1|St|City|ST|USA", "01-01-1995");
//        Bus bus = new Bus("11111116", 30, 80.0, FuelType.HYBRID);
//        assertFalse(service.isDriverEligible(driver, bus));
//    }
//
//    @Test
//    void Eligibility_HybridBus_MediumLicense_returns_False() {
//        Driver driver = new Driver(
//                "34ab!!cdAB",
//                "Carol",
//                8,
//                LicenseType.MEDIUM,
//                "1|St|City|ST|Australia",
//                "01-01-1990"
//        );
//
//        Bus bus = new Bus("11111120", 30, 80.0, FuelType.HYBRID);
//
//        assertFalse(relationService.isDriverEligible(driver, bus));
//    }
//
//    @Test
//    void Eligibility_DieselBus_LightLicense_returns_True() {
//        BusDriverRelationService service =
//                new BusDriverRelationService(new BusDriverRelationRepository());
//        Driver driver = new Driver("34ab!!cdAB", "Frank", 2, LicenseType.LIGHT,
//                "1|St|City|ST|USA", "01-01-1995");
//        Bus bus = new Bus("11111117", 30, 80.0, FuelType.DIESEL);
//        assertTrue(service.isDriverEligible(driver, bus));
//    }
//
//    //DriverRepository
//
//    @Test
//    void DriverRepo_Add_Then_Retrieve_succeeds() {
//        DriverRepository repo = new DriverRepository();
//        Driver driver = new Driver("56xy!!stQR", "Jane", 5, LicenseType.MEDIUM,
//                "1|St|City|ST|USA", "01-01-1995");
//        repo.add(driver);
//        assertNotNull(repo.retrieve("56xy!!stQR"));
//    }
//
//    @Test
//    void DriverRepo_Add_Duplicate_throws_IllegalArgument() {
//        DriverRepository repo = new DriverRepository();
//        Driver driver = new Driver("57xy!!stQR", "Jake", 5, LicenseType.MEDIUM,
//                "1|St|City|ST|USA", "01-01-1995");
//        repo.add(driver);
//        assertThrows(IllegalArgumentException.class,
//                () -> repo.add(new Driver("57xy!!stQR", "Jake2", 3, LicenseType.LIGHT,
//                        "1|St|City|ST|USA", "01-01-1995")));
//    }
//
//    @Test
//    void DriverRepo_Count_increases_after_add() {
//        DriverRepository repo = new DriverRepository();
//        int before = repo.count();
//        repo.add(new Driver("58xy!!stQR", "Jill", 4, LicenseType.LIGHT,
//                "1|St|City|ST|USA", "01-01-1995"));
//        assertEquals(before + 1, repo.count());
//    }
//
//    //Integration Tests – DriverRepository
//
//    @Test
//    void DriverRepo_ValidDriver_StoredAndRetrievedCorrectly() {
//        DriverRepository repo =
//                new DriverRepository();
//
//        Driver driver = new Driver(
//                "34ab!!cdAB",
//                "Alice Smith",
//                5,
//                LicenseType.HEAVY,
//                "10|Park Rd|Melbourne|VIC|Australia",
//                "15-03-1990"
//        );
//
//        repo.add(driver);
//
//        Driver retrieved =
//                repo.retrieve("34ab!!cdAB");
//
//        assertNotNull(retrieved);
//
//        assertEquals("Alice Smith",
//                retrieved.getName());
//    }
//
//    @Test
//    void DriverRepo_Update_PersistedCorrectlyAfterReload() {
//        DriverRepository repo =
//                new DriverRepository();
//
//        int before = repo.count();
//
//        repo.add(new Driver(
//                "56cd!!efGH",
//                "Carol White",
//                4,
//                LicenseType.MEDIUM,
//                "1|Old St|Brisbane|QLD|Australia",
//                "20-11-1992"
//        ));
//
//        repo.update(
//                "56cd!!efGH",
//                4,
//                LicenseType.MEDIUM,
//                "99|New Blvd|Perth|WA|Australia"
//        );
//
//        DriverRepository reloaded =
//                new DriverRepository();
//
//        assertEquals(before + 1,
//                reloaded.count());
//
//        assertEquals(
//                "99|New Blvd|Perth|WA|Australia",
//                reloaded.retrieve("56cd!!efGH").getAddress()
//        );
//    }
//
//    //Integration Tests – BusRepository
//
//    @Test
//    void BusRepo_ValidBus_StoredAndRetrievedCorrectly() {
//        BusRepository repo =
//                new BusRepository();
//
//        Bus bus = new Bus(
//                "77777771",
//                45,
//                75.0,
//                FuelType.DIESEL
//        );
//
//        repo.add(bus);
//
//        Bus retrieved =
//                repo.retrieve("77777771");
//
//        assertNotNull(retrieved);
//
//        assertEquals(45,
//                retrieved.getCapacity());
//    }
//
//    @Test
//    void BusRepo_Update_PersistedCorrectlyAfterReload() {
//        BusRepository repo =
//                new BusRepository();
//
//        int before = repo.count();
//
//        repo.add(new Bus(
//                "77777772",
//                60,
//                90.0,
//                FuelType.DIESEL
//        ));
//
//        repo.update(
//                "77777772",
//                50,
//                70.0,
//                FuelType.DIESEL
//        );
//
//        BusRepository reloaded =
//                new BusRepository();
//
//        assertEquals(before + 1,
//                reloaded.count());
//
//        assertEquals(50,
//                reloaded.retrieve("77777772").getCapacity());
//    }
//
//}