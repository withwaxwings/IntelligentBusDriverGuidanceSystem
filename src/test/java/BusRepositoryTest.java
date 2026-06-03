import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BusRepositoryTest {
    @Nested
    class AdditionAndRetrieval{
        //Test Case 1 - Valid Addition And Retrieval
        @Test
        void BusRepo_Add_Then_Retrieve_succeeds() {
            BusRepository repo = new BusRepository();
            Bus bus1 = new Bus("99999991", 40, 60.0, FuelType.DIESEL);
            Bus bus2 = new Bus("99999994", 50, 70.0, FuelType.HYBRID);
            Bus bus3 = new Bus("99999995", 30, 80.0, FuelType.ELECTRICITY);
            repo.add(bus1);
            repo.add(bus2);
            repo.add(bus3);
            assertNotNull(repo.retrieve("99999991"));
            assertNotNull(repo.retrieve("99999994"));
            assertNotNull(repo.retrieve("99999995"));
        }

        //Test Case 2 - Invalid Addition due to Duplicate
        @Test
        void BusRepo_Add_Duplicate_throws_IllegalArgument() {
            BusRepository repo = new BusRepository();
            repo.add(new Bus("99999992", 40, 60.0, FuelType.DIESEL));
            repo.add(new Bus("99999996", 50, 70.0, FuelType.HYBRID));
            repo.add(new Bus("99999997", 30, 80.0, FuelType.ELECTRICITY));
            assertThrows(IllegalArgumentException.class,
                    () -> repo.add(new Bus("99999992", 30, 40.0, FuelType.HYBRID)));
            assertThrows(IllegalArgumentException.class,
                    () -> repo.add(new Bus("99999996", 20, 50.0, FuelType.DIESEL)));
            assertThrows(IllegalArgumentException.class,
                    () -> repo.add(new Bus("99999997", 10, 30.0, FuelType.ELECTRICITY)));
        }

        // Test Case 3 - Unknown ID returns Null
        @Test
        void BusRepo_Retrieve_UnknownID_returns_Null() {
            BusRepository repo = new BusRepository();
            assertNull(repo.retrieve("00000000"));
            assertNull(repo.retrieve("11111111"));
            assertNull(repo.retrieve("22222222"));
        }
    }

    @Nested
    class Count{
        //Test Case 1 - Count Increases After Addition
        @Test
        void BusRepo_Count_increases_after_add() {
            BusRepository repo = new BusRepository();
            int before = repo.count();
            repo.add(new Bus("99999993", 40, 60.0, FuelType.DIESEL));
            assertEquals(before + 1, repo.count());
            repo.add(new Bus("99999998", 50, 70.0, FuelType.HYBRID));
            assertEquals(before + 2, repo.count());
            repo.add(new Bus("99999999", 30, 80.0, FuelType.ELECTRICITY));
            assertEquals(before + 3, repo.count());
        }
    }

    @Nested
    class Update{
        //Test Case 1 - Bus Repository Persists After Updates
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

            assertEquals(before + 3, reloaded.count());
            assertEquals(50, reloaded.retrieve("77777772").getCapacity());
            assertEquals(45, reloaded.retrieve("77777773").getCapacity());
            assertEquals(40, reloaded.retrieve("77777774").getCapacity());
        }
    }
}