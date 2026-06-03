public class BusDriverRelationService {

    private final BusDriverRelationRepository relationRepository;

    /**
     * @param relationRepository the repository used to persist bus-driver relations
     */
    public BusDriverRelationService(BusDriverRelationRepository relationRepository) {
        this.relationRepository = relationRepository;
    }

    /**
     * Determines whether a driver is eligible to operate a given bus:
     * Drivers over 50 cannot drive buses with a capacity of 50 or more.
     * Electric buses require at least 5 years of experience.
     * Electric and hybrid buses require a HEAVY or PUBLIC_TRANSPORT license.
     * @param driver the driver to check
     * @param bus the bus to check against
     * @return true if the driver is eligible, false otherwise
     */
    public boolean isDriverEligible(Driver driver, Bus bus) {
        if (driver.getAgeInYears() > 50 && bus.getCapacity() >= 50)
            return false;

        if (bus.getFuelType() == FuelType.ELECTRICITY && driver.getExperienceYears() < 5)
            return false;

        boolean isAdvancedBus = bus.getFuelType() == FuelType.ELECTRICITY || bus.getFuelType() == FuelType.HYBRID;
        boolean hasRequiredLicense = driver.getLicenseType() == LicenseType.HEAVY || driver.getLicenseType() == LicenseType.PUBLIC_TRANSPORT;
        if (isAdvancedBus && !hasRequiredLicense)
            return false;

        return true;
    }

    /**
     * Assigns an eligible driver to a bus and persists the relation.
     * @param driver the driver to assign
     * @param bus the bus to assign the driver to
     * @return the created BusDriverRelation
     * @throws IllegalStateException if the driver is not eligible for the bus
     */
    public BusDriverRelation assign(Driver driver, Bus bus) {
        if (!isDriverEligible(driver, bus))
            throw new IllegalStateException(
                "Driver " + driver.getDriverID() + " is not eligible for bus " + bus.getBusID());

        BusDriverRelation relation = new BusDriverRelation(bus.getBusID(), driver.getDriverID());
        relationRepository.add(relation);
        return relation;
    }
}
