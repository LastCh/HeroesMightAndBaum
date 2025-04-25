package game.model.unit;

import game.model.player.Player;

public class Swordsman extends Unit {
    private final double distanceModify = 1;
    private final double powerModify = 1.1;
    private static final int cost = 30;

    public Swordsman() { }

    public int getCost() {
        return cost;
    }
}
