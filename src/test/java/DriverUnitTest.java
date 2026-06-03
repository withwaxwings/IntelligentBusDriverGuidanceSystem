import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class DriverUnitTest {

    private final DriverService driverService = new DriverService(new DriverRepository());

    // D1 - Valid DriverID
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

    // D3 - isValidBirthdate
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

}
