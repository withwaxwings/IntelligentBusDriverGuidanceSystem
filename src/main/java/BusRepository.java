import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BusRepository {
    private static final String FILE_PATH = "busrepo.txt";
    private static final String HEADER = "busID,capacity,fuelLevel,fuelType";
    private List<Bus> buses = new ArrayList<>();

    public BusRepository() {
        loadFromFile();
    }

    public void add(Bus newBus) {
        for (Bus b : buses) {
            if (b.getBusID().equals(newBus.getBusID()))
                throw new IllegalArgumentException("Duplicate ID: " + newBus.getBusID());
        }
        buses.add(newBus);
        saveToFile();
    }

    public Bus retrieve(String busID) {
        for (Bus b : buses) {
            if (b.getBusID().equals(busID)) return b;
        }
        return null;
    }

    public List<Bus> retrieveAll() {
        return new ArrayList<>(buses);
    }

    public void update(String busID, int capacity, double fuelLevel, String fuelType) {
        Bus b = retrieve(busID);
        if (b == null)
            throw new IllegalArgumentException("Bus not found: " + busID);
        b.setCapacity(capacity);
        b.setFuelLevel(fuelLevel);
        b.setFuelType(fuelType);
        saveToFile();
    }

    public int count() {
        return buses.size();
    }

    private void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            bw.write(HEADER);
            bw.newLine();
            for (Bus b : buses) {
                bw.write(String.join(",",
                    b.getBusID(),
                    String.valueOf(b.getCapacity()),
                    String.valueOf(b.getFuelLevel()),
                    b.getFuelType()
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save buses: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",", 4);
                if (parts.length < 4) continue;

                buses.add(new Bus(
                    parts[0].trim(),
                    Integer.parseInt(parts[1].trim()),
                    Double.parseDouble(parts[2].trim()),
                    parts[3].trim()
                ));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load buses: " + e.getMessage());
        }
    }
}
