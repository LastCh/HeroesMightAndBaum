package game.model.building.onmap;

import game.api.FieldObject;
import game.api.Immovable;
import game.api.Position;
import game.map.Field;
import game.model.hero.RandomVisitor;
import game.model.hero.ServiceVisitor;
import game.service.ServiceTask;
import game.service.ServiceType;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

import static game.api.LogConfig.NPC_LOGGER;

public abstract class Enterprise extends FieldObject implements Immovable {
    protected final Field field;
    protected  int capacity;

    protected final List<ServiceVisitor> currentVisitors = new ArrayList<>();
    private final Queue<QueueEntry> waitingQueue = new ArrayDeque<>();

    private ExecutorService pool;
    private ScheduledExecutorService npcSpawner;
    private boolean simulationStarted = false;

    private final int npcBatchMin;
    private final int npcBatchMax;
    private final int npcTickSec;
    private final double npcWishProbability;

    protected Enterprise(
            Position pos, String symbol, int capacity, Field field,
            int npcBatchMin, int npcBatchMax, int npcTickSec, double npcProb) {
        super(pos, symbol, 2);
        this.field = field;
        this.capacity = capacity;
        this.npcBatchMin = npcBatchMin;
        this.npcBatchMax = npcBatchMax;
        this.npcTickSec = npcTickSec;
        this.npcWishProbability = npcProb;
    }

    public synchronized void enter(ServiceVisitor visitor, ServiceType service) {
        if (!simulationStarted) startSimulation();

        waitingQueue.add(new QueueEntry(visitor, service));

        if (isPlayer(visitor)) {
            System.out.printf("🚶 %s встал в очередь (%d ждут, %d обслуживаются)%n",
                    visitor.getName(), waitingQueue.size(), currentVisitors.size());
        } else {
            NPC_LOGGER.info(String.format("[NPC] %s queued (%d wait, %d serve)",
                    visitor.getName(), waitingQueue.size(), currentVisitors.size()));
        }
        tryStartNext();
    }

    private void startSimulation() {
        pool = Executors.newFixedThreadPool(capacity);
        npcSpawner = Executors.newSingleThreadScheduledExecutor();
        simulationStarted = true;

        spawnRandomVisitors(ThreadLocalRandom.current()
                .nextInt(npcBatchMin, npcBatchMax + 1));

        npcSpawner.scheduleAtFixedRate(this::npcTick,
                npcTickSec,
                npcTickSec,
                TimeUnit.SECONDS);

    }

    private void tryStartNext() {
        while (currentVisitors.size() < capacity && !waitingQueue.isEmpty()) {
            QueueEntry next = waitingQueue.poll();
            currentVisitors.add(next.visitor);

            pool.submit(new ServiceTask(next.visitor, next.service, this));
            if (isPlayer(next.visitor)) {
                System.out.printf("🔄 %s начал %s (%d ждут, %d обслуживаются)%n",
                        next.visitor.getName(), next.service.getName(),
                        waitingQueue.size(), currentVisitors.size());
            } else {
                NPC_LOGGER.info(String.format("[NPC] %s started %s",
                        next.visitor.getName(), next.service.getName()));
            }
        }
    }

    public synchronized void onServiceFinished(ServiceVisitor v) {
        currentVisitors.remove(v);
        if (isPlayer(v)) {
            System.out.printf("✔ %s освободил место (%d ждут, %d обслуживаются)%n",
                    v.getName(), waitingQueue.size(), currentVisitors.size());
        } else {
            NPC_LOGGER.info(String.format("[NPC] %s finished", v.getName()));
        }
        tryStartNext();
    }

    private void npcTick() {
        if (waitingQueue.isEmpty() && currentVisitors.isEmpty()) {
            int count = ThreadLocalRandom.current().nextInt(npcBatchMin, npcBatchMax + 1);
            NPC_LOGGER.info(String.format("[NPC] No activity — spawning %d new NPCs", count));
            spawnRandomVisitors(count);
        }

        List<ServiceVisitor> snapshot = new ArrayList<>(currentVisitors);
        snapshot.addAll(waitingQueue.stream().map(q -> q.visitor).toList());

        for (ServiceVisitor vis : snapshot) {
            if (!(vis instanceof RandomVisitor)) continue;
            if (ThreadLocalRandom.current().nextDouble() > npcWishProbability) continue;

            long delay = ThreadLocalRandom.current().nextLong(npcTickSec);
            npcSpawner.schedule(() -> {
                enter(vis, randomService());
            }, delay, TimeUnit.SECONDS);
        }
    }


    private static boolean isPlayer(ServiceVisitor v) {
        return !(v instanceof RandomVisitor);
    }

    private void spawnRandomVisitors(int count) {
        for (int i = 0; i < count; i++) {
            ServiceVisitor npc = new RandomVisitor("NPC-" + i, 500);
            NPC_LOGGER.info(String.format("[NPC] %s spawned", npc.getName()));
            ServiceType rnd = randomService();
            enter(npc, rnd);
        }
    }

    protected abstract ServiceType randomService();

    public List<ServiceVisitor> getCurrentVisitors() {
        return currentVisitors;
    }

    @Override
    public String serialize() {
        return position.x() + "," + position.y() + ";" + coloredSymbol + ";";
    }

    public int displayMenu(){
        return 3;
    }

    public synchronized void changeCapacity(int newCap) {
        if (newCap == capacity) return;

        if (simulationStarted) pool.shutdownNow();

        this.capacity = newCap;

        pool = Executors.newFixedThreadPool(newCap);
        tryStartNext();
    }

    public synchronized void shutdown() {
        if (!simulationStarted) return;

        npcSpawner.shutdownNow();
        pool.shutdownNow();
        waitingQueue.clear();
        currentVisitors.clear();
        simulationStarted = false;
    }

    private record QueueEntry(ServiceVisitor visitor, ServiceType service) {}

}
