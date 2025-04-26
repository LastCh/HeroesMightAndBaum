package game.model.hero;

import game.api.Direction;
import game.api.Position;
import game.map.Field;
import game.model.building.onmap.Castle;

public class ElfHero extends PurchasableHero {
    private static final String COLOR = "\u001B[32m";

    public ElfHero(Position pos, Direction dir, String color, Castle castle, int power, int points, int gold, Hero owner) {
        super(pos, dir, color, castle, power, points, gold, owner);
    }

}
