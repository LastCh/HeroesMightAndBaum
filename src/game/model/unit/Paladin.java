package game.model.unit;

public class Paladin extends Unit {
    private final double distanceModify = 1;
    private final double powerModify = 1.3;
    private static final int cost = 50;

    public Paladin() { }

    public int getCost() {
        return cost;
    }

    public int getPower() {
        return (int)(10*powerModify);
    }
}
