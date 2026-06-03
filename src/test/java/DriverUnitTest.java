import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class DriverUnitTest {

    private final DriverService driverService = new DriverService(new DriverRepository());

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

    // Test Case 7 – Null and Empty Input
    @Test
    void GivenEmpty_ShouldReject() {
        assertFalse(driverService.isValidDriverID(null));
        assertFalse(driverService.isValidDriverID(""));
    }
}
