package game.model.unit;

import game.model.player.Player;

public class Crossbowman extends Unit {
    private final double distanceModify = 1.3;
    private final double powerModify = 1.2;
    private static final int cost = 20;

    public Crossbowman() { }

    public int getCost() {
        return cost;
    }
}
