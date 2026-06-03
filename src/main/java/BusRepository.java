import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BusRepository {
    private static final String FILE_PATH = "bus_repository.txt";
    private static final String HEADER = "busID,capacity,fuelLevel,fuelType";
    private List<Bus> buses = new ArrayList<>();

    public BusRepository() {
        loadFromFile();
    }

    /**
     * Adds a bus to the repository.
     * @param newBus the bus to add
     * @throws IllegalArgumentException if a bus with the same ID already exists
     */
    public void add(Bus newBus) {
        for (Bus b : buses) {
            if (b.getBusID().equals(newBus.getBusID()))
                throw new IllegalArgumentException("Duplicate ID: " + newBus.getBusID());
        }
        buses.add(newBus);
        saveToFile();
    }

    /**
     * Retrieves a bus by its ID.
     * @param busID the ID to search for
     * @return the matching Bus, or null if not found
     */
    public Bus retrieve(String busID) {
        for (Bus b : buses) {
            if (b.getBusID().equals(busID)) return b;
        }
        return null;
    }

    /**
     * @return a copy of all buses in the repository
     */
    public List<Bus> retrieveAll() {
        return new ArrayList<>(buses);
    }

    /**
     * Updates the fields of an existing bus and persists the change.
     * @param busID the ID of the bus to update
     * @param capacity updated passenger capacity
     * @param fuelLevel updated fuel level as a percentage
     * @param fuelType updated fuel type
     * @throws IllegalArgumentException if no bus with the given ID exists
     */
    public void update(String busID, int capacity, double fuelLevel, FuelType fuelType) {
        Bus b = retrieve(busID);
        if (b == null)
            throw new IllegalArgumentException("Bus not found: " + busID);
        b.setCapacity(capacity);
        b.setFuelLevel(fuelLevel);
        b.setFuelType(fuelType);
        saveToFile();
    }

    /**
     * @return the number of buses currently in the repository
     */
    public int count() {
        return buses.size();
    }

    /**
     * Saves the current in-memory state to file.
     */
    public void save() {
        saveToFile();
    }

    /**
     * Removes all buses from the repository and clears the file.
     */
    public void clear() {
        buses.clear();
        saveToFile();
    }

    /**
     * Saves all buses from the repository into the file
     */
    private void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            bw.write(HEADER);
            bw.newLine();
            for (Bus b : buses) {
                bw.write(String.join(",",
                    b.getBusID(),
                    String.valueOf(b.getCapacity()),
                    String.valueOf(b.getFuelLevel()),
                    b.getFuelType().name()
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save buses: " + e.getMessage());
        }
    }

    /**
     * Loads all buses from the file into the repository
     */
    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",", 4);
                if (parts.length < 4) continue;

                buses.add(new Bus(
                    parts[0].trim(),
                    Integer.parseInt(parts[1].trim()),
                    Double.parseDouble(parts[2].trim()),
                    FuelType.valueOf(parts[3].trim())
                ));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load buses: " + e.getMessage());
        }
    }
}
