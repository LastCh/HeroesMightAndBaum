package game.service;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServiceTaskTest {

    @Test
    void serviceTask_appliesEffect_and_notifiesBuilding() throws Exception {
        DummyVisitor visitor = new DummyVisitor();
        DummyEnterprise enterprise = new DummyEnterprise();

        ServiceType type = new ServiceType(
                "heal", 50, 0,
                v -> v.healUnits(3)
        );

        ServiceTask task = new ServiceTask(visitor, type, enterprise);

        ExecutorService pool = Executors.newSingleThreadExecutor();

        pool.submit(task);
        boolean done = enterprise.finished.await(1, TimeUnit.SECONDS);  // ждём ≤1 с
        pool.shutdownNow();

        assertTrue(done, "здание не получило onServiceFinished()");
        assertEquals(3, visitor.healed, "эффект не применён к посетителю");
    }
}