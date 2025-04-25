package game.model.unit;

import game.model.player.Player;

public class Spearman extends Unit {
    private final double distanceModify = 1.1;
    private final double powerModify = 1.2;
    private static final int cost = 10;

    public Spearman() { }

    public int getCost() {
        return cost;
    }
}
