public class BusService {

    private final BusRepository busRepository;

    public BusService(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    /**
     * Validates and creates a new Bus, then adds it to the repository.
     * @param busID unique bus id
     * @param capacity passenger capacity
     * @param fuelLevel initial fuel level as a percentage
     * @param fuelType the bus's fuel type
     * @throws IllegalArgumentException if any field fails validation
     */
    public void createBus(String busID, int capacity, double fuelLevel, FuelType fuelType) {
        if (!isValidBusID(busID))
            throw new IllegalArgumentException("Invalid bus ID: " + busID);
        if (!isValidCapacity(capacity))
            throw new IllegalArgumentException("Invalid capacity: " + capacity);
        if (!isValidFuelLevel(fuelLevel))
            throw new IllegalArgumentException("Invalid fuel level: " + fuelLevel);

        busRepository.add(new Bus(busID, capacity, fuelLevel, fuelType));
    }

    /**
     * Validates a bus ID — must be exactly 8 numeric digits.
     * @param busID the bus ID to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidBusID(String busID) {
        if (busID == null || busID.length() != 8)
            return false;
        for (char c : busID.toCharArray()) {
            if (!Character.isDigit(c))
                return false;
        }
        return true;
    }

    /**
     * Validates a passenger capacity value — must be between 0 and 70 inclusive.
     * @param capacity the capacity to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidCapacity(int capacity) {
        return capacity >= 0 && capacity <= 70;
    }

    /**
     * Validates a fuel level — must be between 0.0 and 100.0 inclusive.
     * @param fuelLevel the fuel level to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidFuelLevel(double fuelLevel) {
        return fuelLevel >= 0.0 && fuelLevel <= 100.0;
    }

    /**
     * Updates the capacity of a bus. Capacity can only be decreased, not increased.
     * @param bus the bus to update
     * @param capacity the new capacity
     * @throws IllegalArgumentException if the capacity is invalid
     * @throws IllegalStateException if the new capacity is greater than the current capacity
     */
    public void updateCapacity(Bus bus, int capacity) {
        if (!isValidCapacity(capacity))
            throw new IllegalArgumentException("Invalid capacity: " + capacity);
        if (capacity > bus.getCapacity())
            throw new IllegalStateException("Cannot increase capacity");
        bus.setCapacity(capacity);
    }

    /**
     * Updates the fuel level of a bus after validation.
     * @param bus the bus to update
     * @param fuelLevel new fuel level as a percentage
     * @throws IllegalArgumentException if the fuel level is out of range
     */
    public void updateFuelLevel(Bus bus, double fuelLevel) {
        if (!isValidFuelLevel(fuelLevel))
            throw new IllegalArgumentException("Invalid fuel level: " + fuelLevel);
        bus.setFuelLevel(fuelLevel);
    }

    /**
     * Updates the fuel type of a bus.
     * @param bus the bus to update
     * @param fuelType new fuel type to assign
     */
    public void updateFuelType(Bus bus, FuelType fuelType) {
        bus.setFuelType(fuelType);
    }
}
