import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DriverUnitTest {

    private DriverService driverService;

    @BeforeEach
    void setUp(){
        driverService = new DriverService(new DriverRepository());
    }

    // D1 - Driver ID Rules
    @Nested
    class ValidDriverID{
        // Test Case 1 – Valid DriverID
        @Test
        void GivenValidDriverID_ShouldAccept() {
            assertTrue(driverService.isValidDriverID("34ab!!cdAB"));
            assertTrue(driverService.isValidDriverID("23!@#bcdEF"));
            assertTrue(driverService.isValidDriverID("89*12!QwQW"));
        }

        // Test Case 2 – Too Long
        @Test
        void GivenTooLongID_ShouldReject() {
            assertFalse(driverService.isValidDriverID("34ab!!cdABC"));
            assertFalse(driverService.isValidDriverID("54&&asdfGHJK"));
            assertFalse(driverService.isValidDriverID("64^^qwerTYUIOP"));
        }

        // Test Case 3 – Too Short
        @Test
        void GivenTooShortID_ShouldReject() {
            assertFalse(driverService.isValidDriverID("34ab!!AB"));
            assertFalse(driverService.isValidDriverID("67zxcv()B"));
            assertFalse(driverService.isValidDriverID("89**qw"));
        }

        // Test Case 4 – Incorrect Prefix
        @Test
        void Given_InvalidPrefix_ShouldReject() {
            assertFalse(driverService.isValidDriverID("14ab!!cdAB"));
            assertFalse(driverService.isValidDriverID("01dfgh%%JK"));
            assertFalse(driverService.isValidDriverID("qwQWER$$QW"));
        }

        // Test Case 5 – Incorrect Suffix
        @Test
        void Given_InvalidSuffix_ShouldReject() {
            assertFalse(driverService.isValidDriverID("34ab!!cdab"));
            assertFalse(driverService.isValidDriverID("56##qwertY"));
            assertFalse(driverService.isValidDriverID("78asdf$$90"));
        }

        // Test Case 6 – Insufficient Special Characters
        @Test
        void GivenInsufficientSpecialChars_ShouldReject() {
            assertFalse(driverService.isValidDriverID("34abc!efAB"));
            assertFalse(driverService.isValidDriverID("23ABCDEFGH"));
            assertFalse(driverService.isValidDriverID("45a1b2c#DF"));
        }

        // Test Case 7 – Empty and Null Input
        @Test
        void GivenEmptyOrNull_ShouldReject() {
            assertFalse(driverService.isValidDriverID(""));
            assertFalse(driverService.isValidDriverID(null));
        }
    }

    // D2 - Address Format
    @Nested
    class ValidAddress {
        // Test Case 1 – Valid Address
        @Test
        void GivenValidAddress_ShouldAccept() {
            assertTrue(driverService.isValidAddress("124 | La Trobe St | Melbourne | Victoria | Australia"));
            assertTrue(driverService.isValidAddress("9      |      Nicholson St | Carlton | Victoria|Australia"));
            assertTrue(driverService.isValidAddress("1341|Dandenong Rd|Malvern East|Victoria|Australia"));
        }

        // Test Case 2 – Incorrect Structure
        @Test
        void GivenIncorrectStructure_ShouldReject() {
            assertFalse(driverService.isValidAddress("124 La Trobe St Melbourne Victoria Australia"));
            assertFalse(driverService.isValidAddress("322-326, Coventry St, South Melbourne, Victoria, Australia"));
            assertFalse(driverService.isValidAddress("8 Whiteman St | Southbank | Victoria | Australia"));
        }

        // Test Case 3 – Missing Section
        @Test
        void GivenMissingSection_ShouldReject() {
            assertFalse(driverService.isValidAddress("124 | La Trobe St | | Victoria | Australia"));
            assertFalse(driverService.isValidAddress("740 | Bourke St | Docklands | |"));
            assertFalse(driverService.isValidAddress("Queen St | Melbourne | Victoria"));
        }

        // Test Case 4 – Extra Section
        @Test
        void GivenExtraSection_ShouldReject() {
            assertFalse(driverService.isValidAddress("124 | La Trobe St | Melbourne | Victoria | Australia | Earth"));
            assertFalse(driverService.isValidAddress("100 | Bulla Rd | Essendon Fields | Victoria | 3041 | Australia"));
            assertFalse(driverService.isValidAddress("2 | Booker St | Spotswood | Yarra River | Victoria | Australia"));
        }

        // Test Case 5 – Empty and Null Input
        @Test
        void GivenEmptyOrNull_ShouldReject() {
            assertFalse(driverService.isValidAddress(""));
            assertFalse(driverService.isValidAddress(null));
        }
    }

    // D3 - Birthdate Format
    @Nested
    class ValidBirthDate {
        // Test Case 1 – Valid Birthdate
        @Test
        void GivenValidBirthdate_ShouldAccept() {
            assertTrue(driverService.isValidBirthdate("11-09-2005"));
            assertTrue(driverService.isValidBirthdate("31-12-1999"));
            assertTrue(driverService.isValidBirthdate("29-02-2000"));
        }

        // Test Case 2 – Incorrect Format
        @Test
        void GivenIncorrectFormat_ShouldReject() {
            assertFalse(driverService.isValidBirthdate("11/09/2005"));
            assertFalse(driverService.isValidBirthdate("02,02,2002"));
            assertFalse(driverService.isValidBirthdate("2003-02-01"));
        }

        // Test Case 3 – Invalid Day
        @Test
        void GivenInvalidDay_ShouldReject() {
            assertFalse(driverService.isValidBirthdate("32-01-2000"));
            assertFalse(driverService.isValidBirthdate("00-05-2005"));
            assertFalse(driverService.isValidBirthdate("First-03-2003"));
        }

        // Test Case 4 – Invalid Month
        @Test
        void GivenInvalidMonth_ShouldReject() {
            assertFalse(driverService.isValidBirthdate("01-13-2000"));
            assertFalse(driverService.isValidBirthdate("02-00-2002"));
            assertFalse(driverService.isValidBirthdate("03-Mar-2004"));
        }

        // Test Case 5 – Empty and Null Input
        @Test
        void GivenEmptyOrNull_ShouldReject() {
            assertFalse(driverService.isValidBirthdate(""));
            assertFalse(driverService.isValidBirthdate(null));
        }
    }

    // D4 - Licence Update Restriction
    @Nested
    class UpdateLicence {
        // static variables
        String address = "124 | La Trobe St | Melbourne | Victoria | Australia";
        String birthDate = "01-01-2001";

        // Test Case 1 – Over 10 years experience
        @Test
        void GivenExperienceOver10_ShouldThrow() {
            Driver driver1 = new Driver("34ab!!cdAB", "Aaron", 11, LicenceType.HEAVY, address, birthDate);
            assertThrows(IllegalArgumentException.class, () -> driverService.updateLicenceType(driver1, LicenceType.MEDIUM));

            Driver driver2 = new Driver("34ab!!cdAC", "Alex", 20, LicenceType.LIGHT, address, birthDate);
            assertThrows(IllegalArgumentException.class, () -> driverService.updateLicenceType(driver2, LicenceType.PUBLIC_TRANSPORT));
        }

        // Test Case 2 – Under 10 years experience
        @Test
        void GivenExperienceUnder10_ShouldUpdate() {
            Driver driver1 = new Driver("34ab!!cdBB", "Bob", 9, LicenceType.MEDIUM, address, birthDate);
            driverService.updateLicenceType(driver1, LicenceType.LIGHT);
            assertEquals(LicenceType.LIGHT, driver1.getLicenceType());

            Driver driver2 = new Driver("34ab!!cdBC", "Barry", 5, LicenceType.PUBLIC_TRANSPORT, address, birthDate);
            driverService.updateLicenceType(driver2, LicenceType.HEAVY);
            assertEquals(LicenceType.HEAVY, driver2.getLicenceType());
        }

        // Test Case 3 – Exactly 10 years experience
        @Test
        void GivenExperienceExactly10_ShouldUpdate() {
            Driver driver1 = new Driver("34ab!!cdCB", "Cindy", 10, LicenceType.HEAVY, address, birthDate);
            driverService.updateLicenceType(driver1, LicenceType.LIGHT);
            assertEquals(LicenceType.LIGHT, driver1.getLicenceType());

            Driver driver2 = new Driver("34ab!!cdCD", "Caine", 10, LicenceType.MEDIUM, address, birthDate);
            driverService.updateLicenceType(driver2, LicenceType.HEAVY);
            assertEquals(LicenceType.HEAVY, driver2.getLicenceType());
        }
    }

    // D5 - Immutable Fields
    @Nested
    class ImmutableFields {
        // static variables
        String address = "124 | La Trobe St | Melbourne | Victoria | Australia";
        String birthDate = "01-01-2001";

        // Test Case 1 – driverID unchanged after construction
        @Test
        void GivenDriverID_ShouldReturnSameValue() {
            String driver1ID = "34ab!!cdAB";
            Driver driver1 = new Driver(driver1ID, "Aaron", 5, LicenceType.LIGHT, address, birthDate);
            assertEquals(driver1ID, driver1.getDriverID());

            String driver2ID = "34ab!!cdAC";
            Driver driver2 = new Driver(driver2ID, "Alex", 5, LicenceType.LIGHT, address, birthDate);
            assertEquals(driver2ID, driver2.getDriverID());
        }

        // Test Case 2 – name unchanged after construction
        @Test
        void GivenName_ShouldReturnSameValue() {
            String driver1Name = "Aaron";
            Driver driver1 = new Driver("34ab!!cdAB", driver1Name, 5, LicenceType.LIGHT, address, birthDate);
            assertEquals(driver1Name, driver1.getName());

            String driver2Name = "Alice";
            Driver driver2 = new Driver("34ab!!cdAC", driver2Name, 5, LicenceType.LIGHT, address, birthDate);
            assertEquals(driver2Name, driver2.getName());
        }

        // Test Case 3 - Verify not setDriverID and setName mutators exists
        @Test
        void ShouldNotHave_DriverID_and_Name_Mutators(){
            boolean hasMutableSetter = false;
            for (Method m : Driver.class.getMethods()) {
                if (m.getName().equals("setDriverID") || m.getName().equals("setName")) {
                    hasMutableSetter = true;
                    break;
                }
            }
            assertFalse(hasMutableSetter);
        }
    }
}
