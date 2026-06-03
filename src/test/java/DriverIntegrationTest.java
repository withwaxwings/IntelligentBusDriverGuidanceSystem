import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DriverIntegrationTest {

    private DriverService driverService;
    private DriverRepository driverRepository;

    @BeforeEach
    void setUp() {
        driverRepository = new DriverRepository();
        driverRepository.clear();
        driverService = new DriverService(driverRepository);
    }

    @Nested
    class DriverRepositoryTests {
        String address = "124 | La Trobe St | Melbourne | Victoria | Australia";
        String birthDate = "01-01-2001";

        // Test Case 1 – Add multiple drivers and retrieve each by ID
        @Test
        void GivenMultipleDriversAdded_ShouldRetrieveById() {
            String driver1ID = "34ab!!cdAB";
            String driver2ID = "34ab!!cdAC";
            String driver3ID = "34ab!!cdAD";

            driverService.createDriver(driver1ID, "Aaron", 5, LicenseType.HEAVY, address, birthDate);
            driverService.createDriver(driver2ID, "Alex", 3, LicenseType.LIGHT, address, birthDate);
            driverService.createDriver(driver3ID, "Alice", 7, LicenseType.MEDIUM, address, birthDate);

            assertNotNull(driverRepository.retrieve(driver1ID));
            assertEquals("Aaron", driverRepository.retrieve(driver1ID).getName());

            assertNotNull(driverRepository.retrieve(driver2ID));
            assertEquals("Alex", driverRepository.retrieve(driver2ID).getName());

            assertNotNull(driverRepository.retrieve(driver3ID));
            assertEquals("Alice", driverRepository.retrieve(driver3ID).getName());
        }

        // Test Case 2 – Unique DriverID constraint
        @Test
        void GivenDuplicateDriverID_ShouldThrow() {
            String duplicateID = "34ab!!cdAB";

            driverService.createDriver(duplicateID, "Aaron", 5, LicenseType.HEAVY, address, birthDate);
            assertThrows(IllegalArgumentException.class, () -> driverService.createDriver(duplicateID, "Alex", 3, LicenseType.LIGHT, address, birthDate));
        }

        // Test Case 3 – Driver updates are persistent
        @Test
        void GivenDriverUpdated_ShouldReturnUpdatedDetails() {
            String driverID = "34ab!!cdAB";
            driverService.createDriver(driverID, "Alex", 5, LicenseType.HEAVY, address, birthDate);
            Driver driver = driverRepository.retrieve(driverID);

            String newAddress = "1341|Dandenong Rd|Malvern East|Victoria|Australia";
            String newBirthdate = "02-02-2002";
            int newExperience = 9;

            driverService.updateBirthdate(driver, newBirthdate);
            driverService.updateAddress(driver, newAddress);
            driverService.updateLicenseType(driver, LicenseType.PUBLIC_TRANSPORT);
            driverService.updateExperienceYears(driver, newExperience);
            driverService.save();

            DriverRepository reloaded = new DriverRepository();
            Driver updated = reloaded.retrieve(driverID);

            assertEquals(newBirthdate, updated.getBirthdate());
            assertEquals(newAddress, updated.getAddress());
            assertEquals(LicenseType.PUBLIC_TRANSPORT, updated.getLicenseType());
            assertEquals(newExperience, updated.getExperienceYears());
        }

        // Test Case 4 – Repository count updates after add
        @Test
        void GivenDriverAdded_ShouldIncreaseCount() {
            int before = driverRepository.count();

            driverService.createDriver("34ab!!cdAB", "Aaron", 4, LicenseType.HEAVY, address, birthDate);

            int after = driverRepository.count();

            assertEquals(before + 1, after);
        }
    }
}
