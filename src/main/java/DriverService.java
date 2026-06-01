import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public void createDriver(String driverID, String name, int experienceYears, LicenseType licenseType, String address, String birthdate) {
        if (!isValidDriverID(driverID))
            throw new IllegalArgumentException("Invalid driver ID: " + driverID);
        if (!isValidAddress(address))
            throw new IllegalArgumentException("Invalid address format");
        if (!isValidBirthdate(birthdate))
            throw new IllegalArgumentException("Invalid birthdate format");

        driverRepository.add(new Driver(driverID, name, experienceYears, licenseType, address, birthdate));
    }

    public boolean isValidDriverID(String id) {
        // check for 10 characters
        if (id == null || id.length() != 10)
            return false;
        // check first 2 characters are numbers 2-9
        if (!id.substring(0, 2).matches("[2-9]{2}"))
            return false;
        // check last 2 characters are upper case letters
        if (!id.substring(8, 10).matches("[A-Z]{2}"))
            return false;

        // check characters 3-8 contains 2 special characters
        String specialSection = id.substring(2, 8);
        int specialCount = 0;
        for (char c : specialSection.toCharArray()) {
            if (!Character.isLetterOrDigit(c))
                specialCount++;
        }
        return specialCount >= 2;
    }

    public boolean isValidAddress(String address) {
        if (address == null)
            return false;

        // check if address is in the format of part1|part2|part3|part4|part5
        String[] parts = address.split("\\|");
        if (parts.length != 5)
            return false;
        for (String part : parts) {
            if (part.trim().isEmpty())
                return false;
        }
        return true;
    }

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

    public void updateExperienceYears(Driver driver, int experienceYears) {
        driver.setExperienceYears(experienceYears);
    }

    public void updateLicenseType(Driver driver, LicenseType licenseType) {
        if (driver.getExperienceYears() > 10)
            throw new IllegalArgumentException("Driver with more than 10 years of experience cannot change license type");
        driver.setLicenseType(licenseType);
    }

    public void updateAddress(Driver driver, String address) {
        if (!isValidAddress(address))
            throw new IllegalArgumentException("Invalid address format");
        driver.setAddress(address);
    }

    public void updateBirthdate(Driver driver, String birthdate) {
        if (!isValidBirthdate(birthdate))
            throw new IllegalArgumentException("Invalid birthdate format");
        driver.setBirthdate(birthdate);
    }
}
