package game.model.unit;

public abstract class Unit {
    protected static int cost;
    protected float powerModify;
    protected float distanceModify;

    public Unit() { }

    public int getCost() {
        return cost;
    }

    public int getPower() {
        return (int)(10*powerModify);
    }
}
