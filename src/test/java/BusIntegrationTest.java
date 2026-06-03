import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BusIntegrationTest {

    private BusService busService;
    private BusRepository busRepository;

    @BeforeEach
    void setUp() {
        busRepository = new BusRepository();
        busRepository.clear();
        busService = new BusService(busRepository);
    }

    @Nested
    class BusRepositoryTests {

        // Test Case 1 – Valid buses are stored correctly
        @Test
        void GivenMultipleBusesAdded_ShouldRetrieveById() {
            String busID1 = "77777771";
            String busID2 = "77777775";
            String busID3 = "77777776";

            busService.createBus(busID1, 45, 75.0, FuelType.DIESEL);
            busService.createBus(busID2, 30, 60.0, FuelType.HYBRID);
            busService.createBus(busID3, 20, 50.0, FuelType.ELECTRICITY);

            assertNotNull(busRepository.retrieve(busID1));
            assertEquals(45, busRepository.retrieve(busID1).getCapacity());

            assertNotNull(busRepository.retrieve(busID2));
            assertEquals(30, busRepository.retrieve(busID2).getCapacity());

            assertNotNull(busRepository.retrieve(busID3));
            assertEquals(20, busRepository.retrieve(busID3).getCapacity());
        }

        // Test Case 2 – Invalid buses are rejected
        @Test
        void GivenDuplicateBusID_ShouldThrow() {
            String duplicateID = "77777772";

            busService.createBus(duplicateID, 40, 60.0, FuelType.DIESEL);
            assertThrows(IllegalArgumentException.class, () -> busService.createBus(duplicateID, 30, 40.0, FuelType.HYBRID));
        }

        // Test Case 3 – Updates are persisted correctly
        @Test
        void GivenBusUpdated_ShouldReturnUpdatedDetails() {
            String busID = "77777773";
            busService.createBus(busID, 60, 90.0, FuelType.DIESEL);
            Bus bus = busRepository.retrieve(busID);

            int newCapacity = 50;
            double newFuelLevel = 70.0;
            FuelType newFuelType = FuelType.HYBRID;

            busService.updateCapacity(bus, newCapacity);
            busService.updateFuelLevel(bus, newFuelLevel);
            busService.updateFuelType(bus, newFuelType);
            busService.save();

            BusRepository reloaded = new BusRepository();
            Bus updated = reloaded.retrieve(busID);

            assertEquals(newCapacity, updated.getCapacity());
            assertEquals(newFuelLevel, updated.getFuelLevel());
            assertEquals(newFuelType, updated.getFuelType());
        }

        // Test Case 4 – Record counts are updated correctly
        @Test
        void GivenBusAdded_ShouldIncreaseCount() {
            int before = busRepository.count();

            busService.createBus("77777774", 40, 60.0, FuelType.DIESEL);

            int after = busRepository.count();

            assertEquals(before + 1, after);
        }
    }
}
