public class Driver{
    private String driverID;
    private String name;
    private int experienceYears;
    private LicenseType licenseType;
    private String address;
    private String birthdate;

    public Driver(String driverID, String name, int experienceYears, LicenseType licenseType, String address, String birthdate){
        this.driverID = driverID;
        this.name = name;
        this.experienceYears = experienceYears;
        this.licenseType = licenseType;
        this.address = address;
        this.birthdate = birthdate;

    }

    public String getDriverID() {
        return driverID;
    }

    public String getName() {
        return name;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public LicenseType getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(LicenseType licenseType) {
        this.licenseType = licenseType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    @Override
    public String toString() {
        return String.format("Driver[ID=%s, Name=%s, Experience=%d years, License=%s, Address=%s, Birthdate=%s]",
            driverID, name, experienceYears, licenseType, address, birthdate);
    }
}