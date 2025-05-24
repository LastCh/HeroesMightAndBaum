package game.model.building.onmap;

import game.api.Immovable;
import game.api.Position;
import game.api.FieldObject;
import game.map.Field;
import game.model.building.incastle.BuildingCastle;

import java.util.ArrayList;
import java.util.Objects;

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

    public boolean containsName(BuildingCastle obj) {
        boolean cont = false;
        for (BuildingCastle building : buildings) {
            if (Objects.equals(obj.getNameNotStat(), building.getNameNotStat())) {
                cont = true;
                break;
            }
        }
        return cont;
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
        StringBuilder sb = new StringBuilder();
        sb.append(position.x()).append(",").append(position.y()).append(";")
                .append(health).append(";")
                .append(coloredSymbol).append(";");

        for (int i = 0; i < buildings.size(); i++) {
            sb.append(buildings.get(i).getClass().getSimpleName());
            if (i < buildings.size() - 1) sb.append(",");
        }

        return sb.toString();
    }


    public static Castle deserialize(String data, Field field) {
        String[] parts = data.split(";", -1); // <- важно: -1, чтобы не терялись пустые части

        String[] coords = parts[0].split(",");
        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);
        int health = Integer.parseInt(parts[1]);
        String color = parts[2]; // цветовая строка
        Castle c = new Castle(new Position(x, y), health, field);
        c.setColoredSymbol(color);

        if (parts.length > 3 && !parts[4].isEmpty()) {
            String[] buildingNames = parts[4].split(",");
            for (String name : buildingNames) {
                try {
                    Class<?> clazz = Class.forName("game.model.building.incastle." + name);
                    Object instance = clazz.getDeclaredConstructor().newInstance();
                    if (instance instanceof BuildingCastle building) {
                        c.addBuilding(building);
                    }
                } catch (Exception e) {
                    System.out.println("❌ Не удалось загрузить здание: " + name);
                }
            }
        }

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
