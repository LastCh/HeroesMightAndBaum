package game.model.hero;

import game.api.Position;
import game.map.Field;
import game.model.building.onmap.Castle;

import java.util.Objects;

public class OrcHero extends PurchasableHero {
    private static final String COLOR = "\u001B[90m";

    public OrcHero(Position pos, String color, Castle castle, int power, int points, int gold, Hero owner) {
        super(pos, color, castle, power, points, gold, owner);
    }

    public OrcHero(Position pos, Castle castle, int power, int points, int gold, Hero owner) {
        super(pos, COLOR, castle, power, points, gold, owner);
        this.maxMovementPoints = points;
    }

    @Override
    public String getClassName() {
        return "OrcHero";
    }

    @Override
    public String serialize() {
        return position.x() + "," + position.y() + ";" +
                power + ";" + movementPoints + ";" + gold + ";" +
                owner.getClassName() + ";" +
                serializeUnits();
    }

    public static OrcHero deserialize(String data, Field field,
                                        Castle playerCastle, Castle compCastle,
                                        Hero player, Hero comp) {
        String[] parts = data.split(";");
        String[] pos = parts[0].split(",");
        int x = Integer.parseInt(pos[0]);
        int y = Integer.parseInt(pos[1]);
        int power = Integer.parseInt(parts[1]);
        int points = Integer.parseInt(parts[2]);
        int gold = Integer.parseInt(parts[3]);
        String ownerName = parts[4];
        String unitData = parts.length > 5 ? parts[5] : "";

        OrcHero hero;
        if (Objects.equals(ownerName, "HumanHero")) {
            hero = new OrcHero(new Position(x, y), playerCastle, power, points, gold, player);
        } else {
            hero = new OrcHero(new Position(x, y), compCastle, power, points, gold, comp);
        }
        hero.deserializeUnits(unitData);
        return hero;
    }

}