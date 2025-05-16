package game.model.hero;

import game.api.Position;
import game.model.building.onmap.Castle;

public class OrcHero extends PurchasableHero {
    private static final String COLOR = "\u001B[90m";

    public OrcHero(Position pos, String color, Castle castle, int power, int points, int gold, Hero owner) {
        super(pos, color, castle, power, points, gold, owner);
    }

    public OrcHero(Position pos, Castle castle, int power, int points, int gold, Hero owner) {
        super(pos, COLOR, castle, power, points, gold, owner);
    }

    @Override
    public String getColor() {
        return COLOR;
    }
}