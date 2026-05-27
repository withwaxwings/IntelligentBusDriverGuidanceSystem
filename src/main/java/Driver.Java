public class Driver{
    private String driverID;
    private String name;
    private int experienceYears;
    private String licenseType; // Light, Medium, Heavy, PublicTransport
    private String address;
    private String birthdate;

    public Driver(String driverID, String name, int experienceYears, String licenseType, String address, String birthdate){
        this.driverID = driverID;
        this.name = name;
        this.experienceYears=experienceYears;
        this.licenseType=licenseType;
        this.birthdate=birthdate;
    }
}