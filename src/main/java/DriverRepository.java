import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DriverRepository {
    private static final String FILE_PATH = "drivers.txt";
    private static final String HEADER = "driverID,name,experienceYears,licenseType,address,birthdate";
    private List<Driver> drivers = new ArrayList<>();

    public DriverRepository(){
        loadFromFile();
    }

    public void add(Driver newDriver){
        for (Driver d: drivers){
            if (d.getDriverID().equals(newDriver.getDriverID()))
               throw new IllegalArgumentException("Duplicate ID:" + newDriver.getDriverID());
        }
        drivers.add(newDriver);
        saveToFile();
    }

    public Driver retrieve(String driverID) {
        for (Driver d: drivers){
            if (d.getDriverID().equals(driverID))
                return d;
        }
        return null;
    }

    public List<Driver> retrieveAll() {
        return new ArrayList<>(drivers);
    }

    public void update(String driverID, int experienceYears, LicenseType licenseType, String address) {
        Driver d = retrieve(driverID);
        if (d == null){
            throw new IllegalArgumentException("Driver not found:" + driverID);
        }
        d.setExperienceYears(experienceYears);
        if (licenseType != null) d.setLicenseType(licenseType);
        if (address != null)     d.setAddress(address);
        saveToFile();
    }

    public int count() {
        return drivers.size();
    }

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

    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // skips header line

            String line;
            drivers = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                // Split on comma, but only 6 parts
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