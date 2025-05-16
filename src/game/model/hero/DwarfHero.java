package game.model.hero;

import game.api.Position;
import game.model.building.onmap.Castle;

public class DwarfHero extends PurchasableHero {
    private static final String COLOR = "\u001B[38;5;94m";

    public DwarfHero(Position pos, String color, Castle castle, int power, int points, int gold, Hero owner) {
        super(pos, color, castle, power, points, gold, owner);
    }

    public DwarfHero(Position pos, Castle castle, int power, int points, int gold, Hero owner) {
        super(pos, COLOR, castle, power, points, gold, owner);
    }


    @Override
    public String getColor() {
        return COLOR;
    }

}