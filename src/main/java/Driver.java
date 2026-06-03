import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Driver{
    private String driverID;
    private String name;
    private int experienceYears;
    private LicenceType licenceType;
    private String address;
    private String birthdate;

    /**
     * Constructs a new Driver.
     * @param driverID unique 10-character driver id
     * @param name name of the driver
     * @param experienceYears years of driving experience
     * @param licenceType the driver's licence type
     * @param address pipe-delimited address
     * @param birthdate date of birth in dd-MM-yyyy format
     */
    public Driver(String driverID, String name, int experienceYears, LicenceType licenceType, String address, String birthdate){
        this.driverID = driverID;
        this.name = name;
        this.experienceYears = experienceYears;
        this.licenceType = licenceType;
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

    public LicenceType getLicenceType() {
        return licenceType;
    }

    public void setLicenceType(LicenceType licenceType) {
        this.licenceType = licenceType;
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

    /**
     * Calculates the driver's current age based on their birthdate.
     * @return age in years
     */
    public int getAgeInYears() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dateOfBirth = LocalDate.parse(this.birthdate, format);
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return String.format("Driver[ID=%s, Name=%s, Experience=%d years, Licence=%s, Address=%s, Birthdate=%s]",
            driverID, name, experienceYears, licenceType, address, birthdate);
    }
}
