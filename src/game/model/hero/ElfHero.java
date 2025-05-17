package game.model.hero;

import game.api.Position;
import game.map.Field;
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

    @Override
    public String getClassName() {
        return "ElfHero";
    }

    @Override
    public String serialize() {
        return position.x() + "," + position.y() + ";" +
                power + ";" + movementPoints + ";" + gold + ";" +
                owner.getClassName() + ";" +
                serializeUnits(); // Упрощённо: имя класса владельца
    }

    public static ElfHero deserialize(String data, Field field, Castle castle, Hero owner) {
        String[] parts = data.split(";");
        String[] pos = parts[0].split(",");
        int x = Integer.parseInt(pos[0]);
        int y = Integer.parseInt(pos[1]);
        int power = Integer.parseInt(parts[1]);
        int points = Integer.parseInt(parts[2]);
        int gold = Integer.parseInt(parts[3]);
        String unitData = parts.length > 4 ? parts[4] : "";

        ElfHero hero = new ElfHero(new Position(x, y), castle, power, points, gold, owner);
        hero.deserializeUnits(unitData);
        field.getCell(x, y).addObject(hero);
        return hero;
    }


}