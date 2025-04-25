package game.model.unit;

import game.model.player.Player;

public abstract class Unit {
    protected static int cost;
    protected float powerModify;
    protected float distanceModify;

    public Unit() {
    }

    public int getCost() {
        return cost;
    }
}
