public class BusDriverRelation {
    private String busID;
    private String driverID;

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
