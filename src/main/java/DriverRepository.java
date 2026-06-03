import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DriverRepository {
    private static final String FILE_PATH = "driver_repository.txt";
    private static final String HEADER = "driverID,name,experienceYears,licenseType,address,birthdate";
    private List<Driver> drivers = new ArrayList<>();

    public DriverRepository(){
        loadFromFile();
    }

    /**
     * Adds a driver to the repository.
     * @param newDriver the driver to add
     * @throws IllegalArgumentException if a driver with the same ID already exists
     */
    public void add(Driver newDriver){
        for (Driver d: drivers){
            if (d.getDriverID().equals(newDriver.getDriverID()))
               throw new IllegalArgumentException("Duplicate ID:" + newDriver.getDriverID());
        }
        drivers.add(newDriver);
        saveToFile();
    }

    /**
     * Retrieves a driver by their ID.
     * @param driverID the ID to search for
     * @return the matching Driver, or null if not found
     */
    public Driver retrieve(String driverID) {
        for (Driver d: drivers){
            if (d.getDriverID().equals(driverID))
                return d;
        }
        return null;
    }

    /**
     * @return a copy of all drivers in the repository
     */
    public List<Driver> retrieveAll() {
        return new ArrayList<>(drivers);
    }

    /**
     * Updates the fields of an existing driver and persists the change.
     * @param driverID the ID of the driver to update
     * @param experienceYears updated years of experience (ignored if null)
     * @param licenseType     updated license type (ignored if null)
     * @param address         updated address (ignored if null)
     * @param birthdate       updated birthdate in dd-MM-yyyy format (ignored if null)
     * @throws IllegalArgumentException if no driver with the given ID exists
     */
    public void update(String driverID, Integer experienceYears, LicenseType licenseType, String address, String birthdate) {
        Driver d = retrieve(driverID);
        if (d == null){
            throw new IllegalArgumentException("Driver not found:" + driverID);
        }
        if (experienceYears != null) d.setExperienceYears(experienceYears);
        if (licenseType != null) d.setLicenseType(licenseType);
        if (address != null) d.setAddress(address);
        if (birthdate != null) d.setBirthdate(birthdate);
    }

    /**
     * Saves the current in-memory state to file.
     */
    public void save() {
        saveToFile();
    }

    /**
     * @return the number of drivers currently in the repository
     */
    public int count() {
        return drivers.size();
    }

    /**
     * Removes all drivers from the repository and clears the file.
     */
    public void clear() {
        drivers.clear();
        saveToFile();
    }

    /**
     * Saves all drivers from the repository into the file
     */
    private void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            bw.write(HEADER);
            bw.newLine();
            for (Driver d : drivers) {
                bw.write(String.join(",",
                    d.getDriverID(),
                    d.getName(),
                    String.valueOf(d.getExperienceYears()),
                    d.getLicenseType().name(),
                    d.getAddress(),
                    d.getBirthdate()
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save drivers: " + e.getMessage());
        }
    }

    /**
     * Loads all drivers from the file into the repository
     */
    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();

            String line;
            drivers = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",", 6);
                if (parts.length < 6) continue;

                Driver d = new Driver(
                    parts[0].trim(),
                    parts[1].trim(),
                    Integer.parseInt(parts[2].trim()),
                    LicenseType.valueOf(parts[3].trim()),
                    parts[4].trim(),
                    parts[5].trim()
                );
                drivers.add(d);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load drivers: " + e.getMessage());
        }
    }
}
