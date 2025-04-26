package game.model.hero;

import game.api.Direction;
import game.api.Position;
import game.model.building.onmap.Castle;

public class OrcHero extends PurchasableHero {
    private static final String COLOR = "\u001B[31m";

    public OrcHero(Position pos, Direction dir, String color, Castle castle, int power, int points, int gold, Hero owner) {
        super(pos, dir, color, castle, power, points, gold, owner);
    }

}
