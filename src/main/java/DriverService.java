import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    /**
     * Validates and creates a new Driver, then adds it to the repository.
     * @param driverID unique driver id
     * @param name name of the driver
     * @param experienceYears years of driving experience
     * @param licenseType the driver's license type
     * @param address pipe-delimited address
     * @param birthdate date of birth in dd-MM-yyyy format
     * @throws IllegalArgumentException if any field fails validation
     */
    public void createDriver(String driverID, String name, int experienceYears, LicenseType licenseType, String address, String birthdate) {
        if (!isValidDriverID(driverID))
            throw new IllegalArgumentException("Invalid driver ID: " + driverID);
        if (!isValidAddress(address))
            throw new IllegalArgumentException("Invalid address format");
        if (!isValidBirthdate(birthdate))
            throw new IllegalArgumentException("Invalid birthdate format");

        driverRepository.add(new Driver(driverID, name, experienceYears, licenseType, address, birthdate));
    }

    /**
     * Validates a driver ID against the required format:
     * exactly 10 characters,
     * first 2 digits in range [2-9],
     * last 2 uppercase letters, and
     * at least 2 special characters in positions 3–8.
     * @param id the driver ID to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidDriverID(String id) {
        if (id == null || id.length() != 10)
            return false;
        if (!id.substring(0, 2).matches("[2-9]{2}"))
            return false;
        if (!id.substring(8, 10).matches("[A-Z]{2}"))
            return false;

        String specialSection = id.substring(2, 8);
        int specialCount = 0;
        for (char c : specialSection.toCharArray()) {
            if (!Character.isLetterOrDigit(c))
                specialCount++;
        }
        return specialCount >= 2;
    }

    /**
     * Validates an address against a pipe-delimited format with 5 non-empty parts.
     * @param address the address string to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidAddress(String address) {
        if (address == null)
            return false;

        String[] parts = address.split("\\|");
        if (parts.length != 5)
            return false;
        for (String part : parts) {
            if (part.trim().isEmpty())
                return false;
        }
        return true;
    }

    /**
     * Validates a birthdate string against the dd-MM-yyyy format and the date validity
     * @param birthdate the birthdate string to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidBirthdate(String birthdate) {
        if (birthdate == null) return false;
        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate.parse(birthdate, format);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Updates the experience years of a driver.
     * @param driver the driver to update
     * @param experienceYears new years of experience
     */
    public void updateExperienceYears(Driver driver, int experienceYears) {
        driver.setExperienceYears(experienceYears);
        driverRepository.update(driver.getDriverID(), experienceYears, null, null, null);
    }

    /**
     * Updates the license type of a driver.
     * Drivers with more than 10 years of experience cannot change their license type.
     * @param driver the driver to update
     * @param licenseType the new license type
     * @throws IllegalArgumentException if the driver has more than 10 years of experience
     */
    public void updateLicenseType(Driver driver, LicenseType licenseType) {
        if (driver.getExperienceYears() > 10)
            throw new IllegalArgumentException("Driver with more than 10 years of experience cannot change license type");
        driver.setLicenseType(licenseType);
        driverRepository.update(driver.getDriverID(), null, licenseType, null, null);
    }

    /**
     * Updates the address of a driver after validation.
     * @param driver  the driver to update
     * @param address the new pipe-delimited address
     * @throws IllegalArgumentException if the address format is invalid
     */
    public void updateAddress(Driver driver, String address) {
        if (!isValidAddress(address))
            throw new IllegalArgumentException("Invalid address format");
        driver.setAddress(address);
        driverRepository.update(driver.getDriverID(), null, null, address, null);
    }

    /**
     * Updates the birthdate of a driver after validation.
     * @param driver the driver to update
     * @param birthdate the new birthdate in dd-MM-yyyy format
     * @throws IllegalArgumentException if the birthdate format is invalid
     */
    public void updateBirthdate(Driver driver, String birthdate) {
        if (!isValidBirthdate(birthdate))
            throw new IllegalArgumentException("Invalid birthdate format");
        driver.setBirthdate(birthdate);
        driverRepository.update(driver.getDriverID(), null, null, null, birthdate);
    }
}
