package game.service;

public class ServiceType {
    private final String name;
    private final long durationMillis;
    private final int cost;
    private final Effect effect;

    public ServiceType(String name, long durationMillis, int cost, Effect effect) {
        this.name = name;
        this.durationMillis = durationMillis;
        this.cost = cost;
        this.effect = effect;
    }

    @Override
    public String toString() {
        return name + " (" + durationMillis + " ms, " + cost + " gold)";
    }

    public String getName() { return name; }
    public long getDurationMillis() { return durationMillis; }
    public int getCost() { return cost; }
    public Effect getEffect() { return effect; }
}
