import java.io.*;
import java.util.*;

public class BusDriverRelationRepository {

    private static final String FILE_PATH = "bus_driver_relations.txt";
    private static final String HEADER = "busID,driverID";
    private List<BusDriverRelation> relations = new ArrayList<>();

    public BusDriverRelationRepository() {
        loadFromFile();
    }

    public void add(BusDriverRelation relation) {
        relations.add(relation);
        saveToFile();
    }

    public List<BusDriverRelation> retrieveByBus(String busID) {
        List<BusDriverRelation> result = new ArrayList<>();
        for (BusDriverRelation r : relations) {
            if (r.getBusID().equals(busID)) result.add(r);
        }
        return result;
    }

    public List<BusDriverRelation> retrieveByDriver(String driverID) {
        List<BusDriverRelation> result = new ArrayList<>();
        for (BusDriverRelation r : relations) {
            if (r.getDriverID().equals(driverID)) result.add(r);
        }
        return result;
    }

    public List<BusDriverRelation> retrieveAll() {
        return new ArrayList<>(relations);
    }

    public void remove(String busID, String driverID) {
        boolean removed = relations.removeIf(r -> r.getBusID().equals(busID) && r.getDriverID().equals(driverID));
        if (!removed)
            throw new IllegalArgumentException("Relation not found for bus: " + busID + " and driver: " + driverID);
        saveToFile();
    }

    public int count() {
        return relations.size();
    }

    public void clear() {
        relations.clear();
        saveToFile();
    }

    private void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            bw.write(HEADER);
            bw.newLine();
            for (BusDriverRelation r : relations) {
                bw.write(String.join(",", r.getBusID(), r.getDriverID()));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save relations: " + e.getMessage());
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
                String[] parts = line.split(",", 2);
                if (parts.length < 2) continue;
                relations.add(new BusDriverRelation(parts[0].trim(), parts[1].trim()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load relations: " + e.getMessage());
        }
    }
}
