import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class DriverUnitTest {

   
    //   - ID must be exactly 10 characters:
    //   - First two chars: digits 2-9
    //   - Chars 3-8: at least 2 special characters
    //   - Last two chars: uppercase letters A-Z

    @Test
    void shouldAcceptValidDriverID() {

        // Valid ID: digits 2-9 prefix, two special chars in middle,
        // uppercase suffix

        boolean result =
                Driver.isValidDriverID("23!@abcdAB");

        assertTrue(result);
    }

    @Test
    void shouldAcceptValidDriverIDWithDifferentSpecials() {

        // Valid ID: using hash symbols as the two special characters

        boolean result =
                Driver.isValidDriverID("56##xyz1ZZ");

        assertTrue(result);
    }

    @Test
    void shouldAcceptIDWithExactlyTwoSpecialChars() {

        // Edge case: exactly the minimum of 2 special chars in positions 3-8

        boolean result =
                Driver.isValidDriverID("34!#abcdMN");

        assertTrue(result);
    }

    @Test
    void shouldRejectIDWithFirstDigitOutOfRange() {

        // Invalid: first character is '1', must be between 2 and 9

        boolean result =
                Driver.isValidDriverID("13!@abcdAB");

        assertFalse(result);
    }

    @Test
    void shouldRejectIDWithLowercaseEnding() {

        // Invalid: last two characters must be uppercase A-Z

        boolean result =
                Driver.isValidDriverID("23!@abcdab");

        assertFalse(result);
    }

    @Test
    void shouldRejectIDWithOnlyOneSpecialChar() {

        // Invalid: only one special character in the middle section,
        // at least two are required

        boolean result =
                Driver.isValidDriverID("23!aabcdAB");

        assertFalse(result);
    }

    @Test
    void shouldRejectIDThatIsTooShort() {

        // Invalid: ID is 9 characters, must be exactly 10

        boolean result =
                Driver.isValidDriverID("23!@abcAB");

        assertFalse(result);
    }

    @Test
    void shouldRejectNullDriverID() {

        // Edge case: null input

        boolean result =
                Driver.isValidDriverID(null);

        assertFalse(result);
    }

    // Must follow: Street Number|Street Name|City|State|Country
    // All 5 parts must be not be empty
    

    @Test
    void shouldAcceptValidAddress() {

        // Valid address with all 5 non-empty pipe-separated parts

        boolean result =
                Driver.isValidAddress("12|Main Street|Melbourne|VIC|Australia");

        assertTrue(result);
    }

    @Test
    void shouldRejectAddressWithFourParts() {

        // Invalid: missing country, only 4 parts

        boolean result =
                Driver.isValidAddress("12|Main Street|Melbourne|VIC");

        assertFalse(result);
    }

    @Test
    void shouldRejectAddressWithEmptySegment() {

        // Invalid: city segment is empty

        boolean result =
                Driver.isValidAddress("12|Main Street||VIC|Australia");

        assertFalse(result);
    }

    @Test
    void shouldRejectNullAddress() {

        // Edge case: null input

        boolean result =
                Driver.isValidAddress(null);

        assertFalse(result);
    }

    // Must follow DD-MM-YYYY and be a real calendar date

    @Test
    void shouldAcceptValidBirthdate() {

        // Valid date in correct DD-MM-YYYY format

        boolean result =
                Driver.isValidBirthdate("15-06-1990");

        assertTrue(result);
    }

    @Test
    void shouldRejectBirthdateInWrongFormat() {

        // Invalid: date is in YYYY-MM-DD format instead of DD-MM-YYYY

        boolean result =
                Driver.isValidBirthdate("1990-06-15");

        assertFalse(result);
    }

    @Test
    void shouldRejectNonExistentDate() {

        // Invalid: 31st February does not exist

        boolean result =
                Driver.isValidBirthdate("31-02-2000");

        assertFalse(result);
    }

    @Test
    void shouldRejectNullBirthdate() {

        // Edge case: null input

        boolean result =
                Driver.isValidBirthdate(null);

        assertFalse(result);
    }

   
    // Drivers with more than 10 years experience cannot change licenseType
    
    @Test
    void shouldAllowLicenseChangeUnder10Years() {

        // Driver with 5 years experience should be allowed to change license

        Driver driver = new Driver(
                "23!@abcdAB",
                "Alice",
                5,
                "Light",
                "1|A St|Melbourne|VIC|Australia",
                "01-01-1990"
        );

        assertDoesNotThrow(() ->
                driver.setLicenseType("Heavy"));
    }

    @Test
    void shouldAllowLicenseChangeAtExactly10Years() {

        // Edge case: exactly 10 years experience, change should still be allowed

        Driver driver = new Driver(
                "23!@abcdAB",
                "Bob",
                10,
                "Light",
                "1|A St|Melbourne|VIC|Australia",
                "01-01-1985"
        );

        assertDoesNotThrow(() ->
                driver.setLicenseType("Medium"));
    }

    @Test
    void shouldBlockLicenseChangeOver10Years() {

        // Invalid: driver with 11 years experience cannot change license type

        Driver driver = new Driver(
                "23!@abcdAB",
                "Charlie",
                11,
                "Heavy",
                "1|A St|Melbourne|VIC|Australia",
                "01-01-1980"
        );

        assertThrows(
                IllegalStateException.class,
                () -> driver.setLicenseType("Light")
        );
    }

    
    // driverID and name cannot be modified after construction
   
    @Test
    void shouldReturnOriginalDriverID() {

        // driverID should remain the value set at construction

        Driver driver = new Driver(
                "23!@abcdAB",
                "Dave",
                3,
                "Light",
                "1|A St|Melbourne|VIC|Australia",
                "01-01-2000"
        );

        assertEquals("23!@abcdAB",
                driver.getDriverID());
    }

    @Test
    void shouldReturnOriginalName() {

        // name should remain the value set at construction

        Driver driver = new Driver(
                "23!@abcdAB",
                "Eve",
                3,
                "Light",
                "1|A St|Melbourne|VIC|Australia",
                "01-01-2000"
        );

        assertEquals("Eve",
                driver.getName());
    }

    @Test
    void shouldNotExposeSetDriverIDOrSetName() {

        // Driver class must not expose setDriverID or setName methods

        boolean hasMutableSetter = false;

        for (java.lang.reflect.Method m : Driver.class.getMethods()) {
            if (m.getName().equals("setDriverID") ||
                    m.getName().equals("setName")) {
                hasMutableSetter = true;
                break;
            }
        }

        assertFalse(hasMutableSetter);
    }
}
