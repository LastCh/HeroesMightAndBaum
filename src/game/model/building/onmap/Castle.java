package game.model.building.onmap;

import game.api.Immovable;
import game.api.Position;
import game.api.FieldObject;
import game.map.Field;
import game.model.building.incastle.BuildingCastle;

import java.util.ArrayList;

public class Castle extends FieldObject implements Immovable {
    private final ArrayList<BuildingCastle> buildings = new ArrayList<>(8);
    private int health;
    private final Field field;

    public Castle(Position position, int maxHe, Field field) {
        super(position, "♜", 2);
        this.health = maxHe;
        this.field = field;
    }

    public boolean contains(BuildingCastle obj) {
        return buildings.contains(obj);
    }

    public void addBuilding(BuildingCastle build) {
        buildings.add(build);
    }

    public Castle(Position position, String color, int maxHe, Field field) {
        super(position, color + "♜♜" + "\u001B[0m", 2);
        this.health = maxHe;
        this.field = field;
    }

    @Override
    public String serialize() {
        return position.x() + "," + position.y() + ";" + health + ";" + coloredSymbol;
    }

    public static Castle deserialize(String data, Field field) {
        String[] parts = data.split(";");
        String[] coords = parts[0].split(",");
        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);
        int health = Integer.parseInt(parts[1]);
        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < parts.length; i++) {
            if (i > 2) sb.append(";");
            sb.append(parts[i]);
        }
        String colStr = sb.toString();

        Castle c = new Castle(new Position(x, y), health, field);
        c.setColoredSymbol(colStr);
        return c;
    }

    @Override
    public String getClassName() {
        return "Castle";
    }


    public void takeDamage(int damage) {
        health = Math.max(health - damage, 0);
    }

    public int getHealth() {
        return health;
    }

    public boolean isDestroyed() {
        return health <= 0;
    }

    public Field getField() {
        return field;
    }

    public void setColoredSymbol(String symb) {
        coloredSymbol = symb;
    }
}
