public class Bus {
    private String busID;
    private int capacity;
    private double fuelLevel;
    private String fuelType; // Diesel, Hybrid, Electricity

    public Bus(String busID, int capacity, double fuelLevel, String fuelType){
        this.busID=busID;
        this.capacity=capacity;
        this.fuelLevel=fuelLevel;
        this.fuelType=fuelType;
    }

    public static boolean isValidBusID(String busID) {
        if (busID == null || busID.length() != 8) return false;
        for (char c : busID.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    public static boolean isValidFuelType(String fuelType) {
        if (fuelType == null) return false;
        return fuelType.equals("Diesel") ||
               fuelType.equals("Hybrid") ||
               fuelType.equals("Electricity");
}

    public static boolean isValidCapacity(int capacity) {
        return capacity > 0 && capacity <= 100;
    }

    public static boolean isValidFuelLevel(double fuelLevel) {
        return fuelLevel >= 0.0 && fuelLevel <= 100.0;
    }

    public String getBusID() {
        return busID;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getFuelLevel() {
        return fuelLevel;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setCapacity(int capacity) {
        if (!isValidCapacity(capacity)) {
            throw new IllegalArgumentException("Invalid capacity: " + capacity);
        }
        if(capacity > this.capacity){
            throw new IllegalStateException("Cannot increase capacity");
        }
        this.capacity = capacity;
    }

    public void setFuelLevel(double fuelLevel) {
        if (!isValidFuelLevel(fuelLevel)) {
            throw new IllegalArgumentException("Invalid fuel level: " + fuelLevel);
        }
        this.fuelLevel = fuelLevel;
    }

    public void setFuelType(String fuelType) {
        if(!isValidFuelType(fuelType)){
            throw new IllegalArgumentException("Invalid fuel type: " + fuelType);
        }
        this.fuelType = fuelType;
    }

    @Override
    public String toString(){
        return String.format("BusID: %s, Capacity: %d, FuelLevel: %.2f%%, FuelType: %s",
                busID, capacity, fuelLevel, fuelType);
    }

}