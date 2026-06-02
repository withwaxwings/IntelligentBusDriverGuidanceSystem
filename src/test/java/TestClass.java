import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class TestClass {
    private final DriverService driverService = new DriverService(new DriverRepository());
    private final BusService busService = new BusService(new BusRepository());
    @Test
    void TestFunction(){
        boolean iAmTrue = true;
        boolean iAmFalse = false;

        assertTrue(iAmTrue);
        assertFalse(iAmFalse);
    }

    //Driver-isValidDriverID

    @Test
    void TooLong_DriverID_returns_False(){
        assertFalse(Driver.isValidDriverID("12345678910"));
    }

    @Test
    void TooShort_DriverID_returns_False(){
        assertFalse(Driver.isValidDriverID("34ab!!AB"));
    }

    @Test
    void Valid_DriverID_returns_True(){
        assertTrue(Driver.isValidDriverID("34ab!!cdAB"));
    }

    @Test
    void DriverID_FirstDigitOne_returns_False(){
        assertFalse(Driver.isValidDriverID("14ab!!cdAB"));
    }

    @Test
    void DriverID_LastCharsLowercase_returns_False(){
        assertFalse(Driver.isValidDriverID("34ab!!cdab"));
    }

    @Test
    void DriverID_OneSpecialInMiddle_returns_False(){
        assertFalse(Driver.isValidDriverID("34abc!efAB"));
    }

    @Test
    void DriverID_ExactlyTwoSpecials_returns_True(){
        assertTrue(Driver.isValidDriverID("34a!!bcdAB"));
    }

    //Driver–isValidAddress

    @Test
    void Incorrect_Address_Format_returnsFalse(){
        assertFalse(Driver.isValidAddress("12 Pork Street"));
    }

    @Test
    void Correct_Address_Format_returns_True(){
        assertTrue(Driver.isValidAddress("12|Pork Parade|Echuca|Victoria|Australia"));
    }


    @Test
    void Address_EmptyPart_returns_False(){
        assertFalse(Driver.isValidAddress("12||Echuca|Victoria|Australia"));
    }

    @Test
    void Address_SixParts_returns_False(){
        assertFalse(Driver.isValidAddress("12|Pork Parade|Echuca|Victoria|Australia|Extra"));
    }

    //Driver – isValidLicenseType

    @Test
    void Incorrect_LicenseType_returns_False(){
        assertFalse(Driver.isValidLicenseType("Median"));
    }

    @Test
    void Correct_LicenseType_Heavy_returns_True(){
        assertTrue(Driver.isValidLicenseType("Heavy"));
    }

    @Test
    void Correct_LicenseType_PublicTransport_returns_True(){
        assertTrue(Driver.isValidLicenseType("PublicTransport"));
    }

    @Test
    void LicenseType_Lowercase_returns_False(){
        assertFalse(Driver.isValidLicenseType("heavy"));
    }

    //Driver–isValidBirthdate

    @Test
    void Correct_BirthDate_returns_True(){
        assertTrue(Driver.isValidBirthdate("11-09-2005"));
    }

    @Test
    void Incorrect_BirthDate_returns_False(){
        assertFalse(Driver.isValidBirthdate("11/09/2005"));
    }

    @Test
    void BirthDate_InvalidDay_returns_False(){
        assertFalse(Driver.isValidBirthdate("32-01-2000"));
    }

    @Test
    void BirthDate_InvalidMonth_returns_False(){
        assertFalse(Driver.isValidBirthdate("01-13-2000"));
    }

    //Driver–setLicenseType

    @Test
    void Invalid_Driver_License_Change_throws_IllegalState(){
        Driver driver = new Driver("34ab!!cdAB", "John", 11, "Heavy", "12|Pork Parade|Echuca|Victoria|Australia", "01-01-1990");
        assertThrows(IllegalStateException.class, () -> driver.setLicenseType("Medium"));
    }

    @Test
    void Valid_Driver_License_Change_succeeds(){
        Driver driver = new Driver("34ab!!cdAB", "John", 5, "Light", "12|Pork Parade|Echuca|Victoria|Australia", "01-01-1990");
        driver.setLicenseType("Heavy");
        assertEquals("Heavy", driver.getLicenseType());
    }

    @Test
    void SetLicenseType_InvalidValue_throws_IllegalArgument(){
        Driver driver = new Driver("34ab!!cdAB", "John", 5, "Light", "12|Pork Parade|Echuca|Victoria|Australia", "01-01-1990");
        assertThrows(IllegalArgumentException.class, () -> driver.setLicenseType("Motorbike"));
    }

    //Driver–setAddress

    @Test
    void SetAddress_Valid_succeeds(){
        Driver driver = new Driver("34ab!!cdAB", "John", 5, "Light", "12|Pork Parade|Echuca|Victoria|Australia", "01-01-1990");
        driver.setAddress("99|New Road|Melbourne|Victoria|Australia");
        assertEquals("99|New Road|Melbourne|Victoria|Australia", driver.getAddress());
    }

    @Test
    void SetAddress_Invalid_throws_IllegalArgument(){
        Driver driver = new Driver("34ab!!cdAB", "John", 5, "Light", "12|Pork Parade|Echuca|Victoria|Australia", "01-01-1990");
        assertThrows(IllegalArgumentException.class, () -> driver.setAddress("Bad Address"));
    }

    //Bus–isValidBusID

    @Test
    void Valid_BusID_returns_True(){
        assertTrue(Bus.isValidBusID("12345678"));
    }


    @Test
    void TooShort_BusID_returns_False(){
        assertFalse(Bus.isValidBusID("1234567"));
    }

    @Test
    void TooLong_BusID_returns_False(){
        assertFalse(Bus.isValidBusID("123456789"));
    }

    @Test
    void BusID_ContainsLetter_returns_False(){
        assertFalse(Bus.isValidBusID("1234567A"));
    }

    //Bus–isValidFuelType
    @Test
    void FuelType_Electricity_returns_True(){
        assertTrue(Bus.isValidFuelType("Electricity"));
    }

    @Test
    void FuelType_Invalid_returns_False(){
        assertFalse(Bus.isValidFuelType("Petrol"));
    }


    //Bus–isValidCapacity
    @Test
    void Capacity_LowerBoundary_returns_True(){
        assertTrue(Bus.isValidCapacity(1));
    }

    @Test
    void Capacity_UpperBoundary_returns_True(){
        assertTrue(Bus.isValidCapacity(100));
    }

    @Test
    void Capacity_Zero_returns_False(){
        assertFalse(Bus.isValidCapacity(0));
    }

    @Test
    void Capacity_OverHundred_returns_False(){
        assertFalse(Bus.isValidCapacity(101));
    }
    //Bus–isValidFuelLevel

    @Test
    void FuelLevel_Zero_returns_True(){
        assertTrue(Bus.isValidFuelLevel(0.0));
    }

    @Test
    void FuelLevel_OneHundred_returns_True(){
        assertTrue(Bus.isValidFuelLevel(100.0));
    }

    @Test
    void FuelLevel_Negative_returns_False(){
        assertFalse(Bus.isValidFuelLevel(-1.0));
    }

    @Test
    void FuelLevel_OverHundred_returns_False(){
        assertFalse(Bus.isValidFuelLevel(100.1));
    }

    //Bus–setCapacity

    @Test
    void SetCapacity_Decrease_succeeds(){
        Bus bus = new Bus("12345678", 80, 50.0, "Diesel");
        bus.setCapacity(60);
        assertEquals(60, bus.getCapacity());
    }

    @Test
    void SetCapacity_Increase_throws_IllegalState(){
        Bus bus = new Bus("12345678", 50, 50.0, "Diesel");
        assertThrows(IllegalStateException.class, () -> bus.setCapacity(60));
    }

    @Test
    void SetCapacity_Zero_throws_IllegalArgument(){
        Bus bus = new Bus("12345678", 50, 50.0, "Diesel");
        assertThrows(IllegalArgumentException.class, () -> bus.setCapacity(0));
    }

    //Bus–setFuelLevel

    @Test
    void SetFuelLevel_Valid_succeeds(){
        Bus bus = new Bus("12345678", 50, 50.0, "Diesel");
        bus.setFuelLevel(75.0);
        assertEquals(75.0, bus.getFuelLevel());
    }

    @Test
    void SetFuelLevel_Negative_throws_IllegalArgument(){
        Bus bus = new Bus("12345678", 50, 50.0, "Diesel");
        assertThrows(IllegalArgumentException.class, () -> bus.setFuelLevel(-5.0));
    }

    //Bus–setFuelType

    @Test
    void SetFuelType_Valid_succeeds(){
        Bus bus = new Bus("12345678", 50, 50.0, "Diesel");
        bus.setFuelType("Hybrid");
        assertEquals("Hybrid", bus.getFuelType());
    }

    @Test
    void SetFuelType_Invalid_throws_IllegalArgument(){
        Bus bus = new Bus("12345678", 50, 50.0, "Diesel");
        assertThrows(IllegalArgumentException.class, () -> bus.setFuelType("Petrol"));
    }

    //BusRepository

    @Test
    void BusRepo_Add_Then_Retrieve_succeeds(){
        BusRepository repo = new BusRepository();
        Bus bus = new Bus("99999991", 40, 60.0, "Diesel");
        repo.add(bus);
        assertNotNull(repo.retrieve("99999991"));
    }

    @Test
    void BusRepo_Add_Duplicate_throws_IllegalArgument(){
        BusRepository repo = new BusRepository();
        Bus bus = new Bus("99999992", 40, 60.0, "Diesel");
        repo.add(bus);
        assertThrows(IllegalArgumentException.class, () -> repo.add(new Bus("99999992", 30, 40.0, "Hybrid")));
    }

    @Test
    void BusRepo_Retrieve_UnknownID_returns_Null(){
        BusRepository repo = new BusRepository();
        assertNull(repo.retrieve("00000000"));
    }

    @Test
    void BusRepo_Count_increases_after_add(){
        BusRepository repo = new BusRepository();
        int before = repo.count();
        repo.add(new Bus("99999993", 40, 60.0, "Diesel"));
        assertEquals(before + 1, repo.count());
    }

    //BusRepository–isDriverEligible

    @Test
    void Eligibility_DriverOver50_LargeBus_returns_False(){
        BusRepository repo = new BusRepository();
        Driver driver = new Driver("34ab!!cdAB", "Alice", 15, "Heavy", "1|St|City|ST|USA", "01-01-1970");
        Bus bus = new Bus("11111111", 50, 80.0, "Diesel");
        assertFalse(repo.isDriverEligible(driver, bus));
    }

    @Test
    void Eligibility_DriverOver50_SmallBus_returns_True(){
        BusRepository repo = new BusRepository();
        Driver driver = new Driver("34ab!!cdAB", "Alice", 15, "Heavy", "1|St|City|ST|USA", "01-01-1970");
        Bus bus = new Bus("11111112", 30, 80.0, "Diesel");
        assertTrue(repo.isDriverEligible(driver, bus));
    }

    @Test
    void Eligibility_ElectricBus_UnderExperience_returns_False(){
        BusRepository repo = new BusRepository();
        Driver driver = new Driver("34ab!!cdAB", "Bob", 3, "Heavy", "1|St|City|ST|USA", "01-01-1995");
        Bus bus = new Bus("11111113", 30, 80.0, "Electricity");
        assertFalse(repo.isDriverEligible(driver, bus));
    }

    @Test
    void Eligibility_ElectricBus_HeavyLicense_SufficientExp_returns_True(){
        BusRepository repo = new BusRepository();
        Driver driver = new Driver("34ab!!cdAB", "Dave", 6, "Heavy", "1|St|City|ST|USA", "01-01-1995");
        Bus bus = new Bus("11111115", 30, 80.0, "Electricity");
        assertTrue(repo.isDriverEligible(driver, bus));
    }

    @Test
    void Eligibility_HybridBus_LightLicense_returns_False(){
        BusRepository repo = new BusRepository();
        Driver driver = new Driver("34ab!!cdAB", "Eve", 10, "Light", "1|St|City|ST|USA", "01-01-1995");
        Bus bus = new Bus("11111116", 30, 80.0, "Hybrid");
        assertFalse(repo.isDriverEligible(driver, bus));
    }

    @Test
    void Eligibility_DieselBus_LightLicense_returns_True(){
        BusRepository repo = new BusRepository();
        Driver driver = new Driver("34ab!!cdAB", "Frank", 2, "Light", "1|St|City|ST|USA", "01-01-1995");
        Bus bus = new Bus("11111117", 30, 80.0, "Diesel");
        assertTrue(repo.isDriverEligible(driver, bus));
    }

    //DriverRepository

    @Test
    void DriverRepo_Add_Then_Retrieve_succeeds(){
        DriverRepository repo = new DriverRepository();
        Driver driver = new Driver("56xy!!stQR", "Jane", 5, "Medium", "1|St|City|ST|USA", "01-01-1995");
        repo.add(driver);
        assertNotNull(repo.retrieve("56xy!!stQR"));
    }

    @Test
    void DriverRepo_Add_Duplicate_throws_IllegalArgument(){
        DriverRepository repo = new DriverRepository();
        Driver driver = new Driver("57xy!!stQR", "Jake", 5, "Medium", "1|St|City|ST|USA", "01-01-1995");
        repo.add(driver);
        assertThrows(IllegalArgumentException.class, () -> repo.add(new Driver("57xy!!stQR", "Jake2", 3, "Light", "1|St|City|ST|USA", "01-01-1995")));
    }


    @Test
    void DriverRepo_Count_increases_after_add(){
        DriverRepository repo = new DriverRepository();
        int before = repo.count();
        repo.add(new Driver("58xy!!stQR", "Jill", 4, "Light", "1|St|City|ST|USA", "01-01-1995"));
        assertEquals(before + 1, repo.count());
    }
}
