import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class DriverRepository {
// Add (), Update (), Retrieve (), Count () functions
private static final String FILE_PATH = "drivers.txt";
// assign header columns
private static final String HEADER = "driverID,name,experienceYears,licenseType,address,birthdate";
private List<Driver> drivers = new ArrayList<>();



public DriverRepository(){
    loadFromFile();
}

//add driver
public void add(Driver driver){
    for(Driver d: drivers){
       if(d.getDriverID().equals(driver.getDriverID()))
        throw new IllegalArgumentException("Duplicate ID:" + driver.getDriverID()); 
    }
    drivers.add(driver);
    saveToFile();
}

public Driver retrieve(String driverID) { 
    for(Driver d: drivers){
        if (d.getDriverID().equals(driverID)) return d;
    }
    return null;
}

public List<Driver> retrieveAll() {
    return new ArrayList<>(drivers);
 }


public void update(String driverID, int experienceYears,String licenseType, String address) { 
    Driver d=retrieve(driverID);
    if(d == null){
        throw new IllegalArgumentException("Driver not found:" + driverID);
    }
    d.setExperienceYears(experienceYears);
    if (licenseType != null) { d.setLicenseType(licenseType); }
    if (address != null)     { d.setAddress(address); }
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
                    d.getLicenseType(),
                    d.getAddress(),       // contains | separators, safe in CSV
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
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; } // skip header
                if (line.trim().isEmpty()) continue;

                // Split on comma, but only 6 parts (address contains | not ,)
                String[] parts = line.split(",", 6);
                if (parts.length < 6) continue;

                Driver d = new Driver(
                    parts[0].trim(), // driverID
                    parts[1].trim(), // name
                    Integer.parseInt(parts[2].trim()), // experienceYears
                    parts[3].trim(), // licenseType
                    parts[4].trim(), // address
                    parts[5].trim()  // birthdate
                );
                drivers.add(d);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load drivers: " + e.getMessage());
        }
    }

}