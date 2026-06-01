public class BusService {

    private final BusRepository busRepository;

    public BusService(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    public void createBus(String busID, int capacity, double fuelLevel, FuelType fuelType) {
        if (!isValidBusID(busID))
            throw new IllegalArgumentException("Invalid bus ID: " + busID);
        if (!isValidCapacity(capacity))
            throw new IllegalArgumentException("Invalid capacity: " + capacity);
        if (!isValidFuelLevel(fuelLevel))
            throw new IllegalArgumentException("Invalid fuel level: " + fuelLevel);

        busRepository.add(new Bus(busID, capacity, fuelLevel, fuelType));
    }

    public boolean isValidBusID(String busID) {
        if (busID == null || busID.length() != 8)
            return false;
        for (char c : busID.toCharArray()) {
            if (!Character.isDigit(c))
                return false;
        }
        return true;
    }

    public boolean isValidCapacity(int capacity) {
        return capacity > 0 && capacity <= 70;
    }

    public boolean isValidFuelLevel(double fuelLevel) {
        return fuelLevel >= 0.0 && fuelLevel <= 100.0;
    }

    public void updateCapacity(Bus bus, int capacity) {
        if (!isValidCapacity(capacity))
            throw new IllegalArgumentException("Invalid capacity: " + capacity);
        if (capacity > bus.getCapacity())
            throw new IllegalStateException("Cannot increase capacity");
        bus.setCapacity(capacity);
    }

    public void updateFuelLevel(Bus bus, double fuelLevel) {
        if (!isValidFuelLevel(fuelLevel))
            throw new IllegalArgumentException("Invalid fuel level: " + fuelLevel);
        bus.setFuelLevel(fuelLevel);
    }

    public void updateFuelType(Bus bus, FuelType fuelType) {
        bus.setFuelType(fuelType);
    }
}
