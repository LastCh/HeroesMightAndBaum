package game.model.unit;

import game.model.player.Player;

public class Cavalryman extends Unit {
    private final double distanceModify = 1.1;
    private final double powerModify = 1.4;
    private static final int cost = 40;

    public Cavalryman() { }

    public int getCost() {
        return cost;
    }
}
