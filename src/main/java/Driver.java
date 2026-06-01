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
        this.address=address;
        this.birthdate=birthdate;
        
    }
    public static boolean isValidAddress(String address) {
    if (address == null) return false;
    String[] parts = address.split("\\|");
    // Must have exactly 5 parts
    if (parts.length != 5) return false;
    for (String part : parts) {
        if (part.trim().isEmpty()) return false;
    }
    return true;
}

    public static boolean isValidLicenseType(String licenseType) {
    if (licenseType == null) return false;
    return licenseType.equals("Light") ||
           licenseType.equals("Medium") ||
           licenseType.equals("Heavy") ||
           licenseType.equals("PublicTransport");
    }

    public static boolean isValidDriverID(String id) {
    if (id == null || id.length() != 10) return false;
        for (char c: id.toCharArray()) {
            if(!Character.isDigit(c)) return false;
        }
        return true;
    }

public static boolean isValidBirthdate(String birthdate) {
    if (birthdate == null) return false;
    if (!birthdate.matches("\\d{2}-\\d{2}-\\d{4}")) return false;
    try {
        java.time.format.DateTimeFormatter fmt =
            java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy");
        java.time.LocalDate.parse(birthdate, fmt);
        return true;
    } catch (java.time.format.DateTimeParseException e) {
        return false;
    }
}

    public String getDriverID()     { return driverID; }
    public String getName()         { return name; }
    public int getExperienceYears() { return experienceYears; }
    public String getLicenseType()  { return licenseType; }
    public String getAddress()      { return address; }
    public String getBirthdate()    { return birthdate; }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }
    public void setLicenseType(String licenseType) {
    if (this.experienceYears > 10)
        throw new IllegalStateException("Cannot change license type");
    if (!isValidLicenseType(licenseType))
        throw new IllegalArgumentException("Invalid license type:" + licenseType);
    this.licenseType = licenseType;
}

    public void setAddress(String address) {
        if (!isValidAddress(address))
            throw new IllegalArgumentException("Invalid address format");
        this.address = address;
    }
    public int getAgeInYears() {
    java.time.format.DateTimeFormatter fmt =
        java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy");
    java.time.LocalDate dob = java.time.LocalDate.parse(this.birthdate, fmt);
    return java.time.Period.between(dob, java.time.LocalDate.now()).getYears();
}

@Override
public String toString() {
    return String.format("Driver[ID=%s, Name=%s, Experience=%d years, License=%s, Address=%s, Birthdate=%s]",
        driverID, name, experienceYears, licenseType, address, birthdate); 
        }

}