package game.model.hero;

import game.api.Position;
import game.map.Field;
import game.model.building.onmap.Castle;

import java.util.Objects;

public class DwarfHero extends PurchasableHero {
    private static final String COLOR = "\u001B[38;5;94m";

    public DwarfHero(Position pos, String color, Castle castle, int power, int points, int gold, Hero owner) {
        super(pos, color, castle, power, points, gold, owner);
    }

    public DwarfHero(Position pos, Castle castle, int power, int points, int gold, Hero owner) {
        super(pos, COLOR, castle, power, points, gold, owner);
    }

    @Override
    public String getClassName() {
        return "DwarfHero";
    }

    @Override
    public String serialize() {
        return position.x() + "," + position.y() + ";" +
                power + ";" + movementPoints + ";" + gold + ";" +
                owner.getClassName() + ";" +
                serializeUnits();
    }

    public static DwarfHero deserialize(String data, Field field, Castle playerCastle, Castle compCastle , Hero player, Hero Comp) {
        String[] parts = data.split(";");
        String[] pos = parts[0].split(",");
        int x = Integer.parseInt(pos[0]);
        int y = Integer.parseInt(pos[1]);
        int power = Integer.parseInt(parts[1]);
        int points = Integer.parseInt(parts[2]);
        int gold = Integer.parseInt(parts[3]);
        String name = parts[4];
        String unitData = parts.length > 5 ? parts[5] : "";

        DwarfHero hero1 = new DwarfHero(new Position(x, y), playerCastle, power, points, gold, player);

        if(Objects.equals(name, "HumanHero")){
            DwarfHero hero = new DwarfHero(new Position(x, y), playerCastle, power, points, gold, player);
            field.getCell(x, y).addObject(hero);
            hero.deserializeUnits(unitData);
            return hero;
        }
        if(Objects.equals(name, "ComputerHero")){
            DwarfHero hero = new DwarfHero(new Position(x, y), compCastle, power, points, gold, Comp);
            field.getCell(x, y).addObject(hero);
            hero.deserializeUnits(unitData);
            return hero;
        }
        return hero1;
    }


}