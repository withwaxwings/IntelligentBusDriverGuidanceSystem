import static org.junit.jupiter.api.Assertions.assertFalse;
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
}
