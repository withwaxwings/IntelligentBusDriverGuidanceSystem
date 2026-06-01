import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class BusRepository {
// Add (), Update (), Retrieve (), Count () functions
private static final String FILE_PATH = "busrepo.txt";
private static final String HEADER = "busID,capacity,fuelLevel,fuelType";
private List<Bus> buses = new ArrayList<>();

public BusRepository(){
    loadFromFile();
}

public void add(Bus bus){
    for(Bus b: buses){
        if(b.getBusID().equals(bus.getBusID()))
         throw new IllegalArgumentException("Duplicate ID:" + bus.getBusID());
    }
    buses.add(bus);
    saveToFile();
}

public Bus retrieve(String busID){
    for(Bus b: buses){
        if(b.getBusID().equals(busID)) return b;
    }
    return null;
    }

public List<Bus> retrieveAll(){
    return new ArrayList<>(buses);
}

public void update(String busID, int capacity, double fuelLevel, String fuelType){
    Bus b=retrieve(busID);
    if(b==null){
        throw new IllegalArgumentException("Bus not found:" + busID);
    }
    if(Bus.isValidCapacity(capacity)) { b.setCapacity(capacity);}
    if(Bus.isValidFuelLevel(fuelLevel)) { b.setFuelLevel(fuelLevel);}
    if(Bus.isValidFuelType(fuelType)) { b.setFuelType(fuelType);}
    saveToFile();
}

public int count(){
    return buses.size();
}

public boolean isDriverEligible(Driver driver, Bus bus) {
    //Driver older than 50 cannot drive buses with capacity >= 50
    if (driver.getAgeInYears() > 50 && bus.getCapacity() >= 50)
        return false;

    //Electric bus requires at least 5 years experience
    if ("Electricity".equals(bus.getFuelType()) && driver.getExperienceYears() < 5)
        return false;

    //Electric/Hybrid requires Heavy or PublicTransport licence
    if (("Electricity".equals(bus.getFuelType()) || "Hybrid".equals(bus.getFuelType())) &&
        !driver.getLicenseType().equals("Heavy") &&
        !driver.getLicenseType().equals("PublicTransport"))
        return false;

    return true;
}

private void saveToFile(){
    try(BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))){
        bw.write(HEADER);
        bw.newLine();
        for(Bus b: buses){
            bw.write(String.join(",",
            b.getBusID(),
            String.valueOf(b.getCapacity()),
            String.valueOf(b.getFuelLevel()),
            b.getFuelType()
            ));
            bw.newLine();
        } 
        }catch(IOException e){
            throw new RuntimeException("Failed to save buses: " + e.getMessage());
        }
    }


private void loadFromFile() {
    File file = new File(FILE_PATH);
    if (!file.exists()) return;

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        boolean firstLine = true;
        while ((line = br.readLine()) != null) {
            if (firstLine) { firstLine = false; continue; }
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split(",", 4);
            if (parts.length < 4) continue;

            Bus b = new Bus(
                parts[0].trim(),
                Integer.parseInt(parts[1].trim()),
                Double.parseDouble(parts[2].trim()),
                parts[3].trim()
            );
            buses.add(b);
        }
    } catch (IOException e) {
        throw new RuntimeException("Failed to load buses: " + e.getMessage());
    }
}
}

