package game.model.unit;

public class Crossbowman extends Unit {
    private final double distanceModify = 1.3;
    private final double powerModify = 1.2;
    private static final int cost = 20;

    public Crossbowman() { }

    public int getCost() {
        return cost;
    }

    public int getPower() {
        return (int)(10*powerModify);
    }
}
