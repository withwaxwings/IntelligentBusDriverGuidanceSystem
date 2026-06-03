public class BusDriverRelation {
    private String busID;
    private String driverID;

    /**
     * Constructs a new BusDriverRelation.
     * @param busID the ID of the assigned bus
     * @param driverID the ID of the assigned driver
     */
    public BusDriverRelation(String busID, String driverID) {
        this.busID = busID;
        this.driverID = driverID;
    }

    public String getBusID() {
        return busID;
    }

    public String getDriverID() {
        return driverID;
    }

    @Override
    public String toString() {
        return String.format("BusDriverRelation[Bus=%s, Driver=%s]", busID, driverID);
    }
}
