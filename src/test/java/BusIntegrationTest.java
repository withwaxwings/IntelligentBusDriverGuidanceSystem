import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class BusIntegrationTest {

    // Integration Test 1 - Valid Buses Are Stored Correctly
    @Nested
    class ValidBusStorage {
        @Test
        void BusRepo_ValidBus_StoredAndRetrievedCorrectly() {
            BusRepository repo = new BusRepository();

            Bus bus1 = new Bus("77777771", 45, 75.0, FuelType.DIESEL);
            Bus bus2 = new Bus("77777775", 30, 60.0, FuelType.HYBRID);
            Bus bus3 = new Bus("77777776", 20, 50.0, FuelType.ELECTRICITY);

            repo.add(bus1);
            repo.add(bus2);
            repo.add(bus3);

            BusRepository reloaded = new BusRepository();

            assertNotNull(reloaded.retrieve("77777771"));
            assertEquals(45, reloaded.retrieve("77777771").getCapacity());

            assertNotNull(reloaded.retrieve("77777775"));
            assertEquals(30, reloaded.retrieve("77777775").getCapacity());

            assertNotNull(reloaded.retrieve("77777776"));
            assertEquals(20, reloaded.retrieve("77777776").getCapacity());
        }
    }

    // Integration Test 2 - Invalid Buses Are Rejected
    @Nested
    class InvalidBusRejection {
        @Test
        void BusRepo_InvalidBus_Rejected() {
            BusRepository repo = new BusRepository();
            BusService busService = new BusService(repo);

            // Test Case 1 - Invalid BusID due to letter
            assertThrows(IllegalArgumentException.class,
                    () -> {
                        if (!busService.isValidBusID("1234567A"))
                            throw new IllegalArgumentException("Invalid BusID");
                    });

            // Test Case 2 - Invalid Capacity
            assertThrows(IllegalArgumentException.class,
                    () -> {
                        if (!busService.isValidCapacity(0))
                            throw new IllegalArgumentException("Invalid Capacity");
                    });

            // Test Case 3 - Invalid Fuel Level
            assertThrows(IllegalArgumentException.class,
                    () -> {
                        if (!busService.isValidFuelLevel(-1.0))
                            throw new IllegalArgumentException("Invalid FuelLevel");
                    });
        }
    }

    // Integration Test 3 - Updates Are Persisted Correctly
    @Nested
    class UpdatePersistence {
        @Test
        void BusRepo_Update_PersistedCorrectlyAfterReload() {
            BusRepository repo = new BusRepository();
            int before = repo.count();

            repo.add(new Bus("77777772", 60, 90.0, FuelType.DIESEL));
            repo.add(new Bus("77777773", 55, 85.0, FuelType.HYBRID));
            repo.add(new Bus("77777774", 50, 80.0, FuelType.ELECTRICITY));

            repo.update("77777772", 50, 70.0, FuelType.DIESEL);
            repo.update("77777773", 45, 65.0, FuelType.HYBRID);
            repo.update("77777774", 40, 60.0, FuelType.ELECTRICITY);

            BusRepository reloaded = new BusRepository();

            assertEquals(50, reloaded.retrieve("77777772").getCapacity());
            assertEquals(45, reloaded.retrieve("77777773").getCapacity());
            assertEquals(40, reloaded.retrieve("77777774").getCapacity());
        }
    }

    // Integration Test 4 - Record Counts Are Updated Correctly
    @Nested
    class RecordCount {
        @Test
        void BusRepo_Count_UpdatedCorrectlyAfterReload() {
            BusRepository repo = new BusRepository();
            int before = repo.count();

            repo.add(new Bus("66666661", 40, 60.0, FuelType.DIESEL));
            repo.add(new Bus("66666662", 50, 70.0, FuelType.HYBRID));
            repo.add(new Bus("66666663", 30, 80.0, FuelType.ELECTRICITY));

            BusRepository reloaded = new BusRepository();

            assertEquals(before + 3, reloaded.count());

            assertThrows(IllegalArgumentException.class,
                    () -> reloaded.add(new Bus("66666661", 40, 60.0, FuelType.DIESEL)));

            assertEquals(before + 3, reloaded.count());
        }
    }
}