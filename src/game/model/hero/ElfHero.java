package game.model.hero;

import game.api.Position;
import game.model.building.onmap.Castle;

public class ElfHero extends PurchasableHero {
    private static final String COLOR = "\u001B[38;5;22m";

    public ElfHero(Position pos, String color, Castle castle, int power, int points, int gold, Hero owner) {
        super(pos, color, castle, power, points, gold, owner);
    }

    public ElfHero(Position pos, Castle castle, int power, int points, int gold, Hero owner) {
        super(pos, COLOR, castle, power, points, gold, owner);
    }

    @Override
    public String getColor() {
        return COLOR;
    }

}