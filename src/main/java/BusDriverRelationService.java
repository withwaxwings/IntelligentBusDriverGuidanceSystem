public class BusDriverRelationService {

    private final BusDriverRelationRepository relationRepository;

    public BusDriverRelationService(BusDriverRelationRepository relationRepository) {
        this.relationRepository = relationRepository;
    }

    public static boolean isDriverEligible(Driver driver, Bus bus) {
        // drivers over 50 cannot drive bus with capacity of 50 or more
        if (driver.getAgeInYears() > 50 && bus.getCapacity() >= 50)
            return false;

        // driver must have at least 5 years of experience to drive electric bus
        if (bus.getFuelType() == FuelType.ELECTRICITY && driver.getExperienceYears() < 5)
            return false;

        // driver must have heavy/public transport to drive eletric/hybrid bus
        boolean isAdvancedBus = bus.getFuelType() == FuelType.ELECTRICITY || bus.getFuelType() == FuelType.HYBRID;
        boolean hasRequiredLicense = driver.getLicenseType() == LicenseType.HEAVY || driver.getLicenseType() == LicenseType.PUBLIC_TRANSPORT;
        if (isAdvancedBus && !hasRequiredLicense)
            return false;

        return true;
    }

    public BusDriverRelation assign(Driver driver, Bus bus) {
        if (!isDriverEligible(driver, bus))
            throw new IllegalStateException(
                "Driver " + driver.getDriverID() + " is not eligible for bus " + bus.getBusID());

        BusDriverRelation relation = new BusDriverRelation(bus.getBusID(), driver.getDriverID());
        relationRepository.add(relation);
        return relation;
    }
}
