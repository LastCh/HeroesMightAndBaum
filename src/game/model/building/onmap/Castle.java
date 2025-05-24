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
    private final String colorCode;

    public Castle(Position position, int maxHe, Field field, String colorCode) {
        super(position, "♜", 2);
        this.health = maxHe;
        this.field = field;
        this.colorCode = colorCode;
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

    public Castle(Position position, String colorCode, int maxHe, Field field) {
        super(position, "\u001B[" + colorCode + "m♜♜\u001B[0m", 2);
        this.colorCode = colorCode; // Сохраняем только код цвета (напр., "34;47")
        this.health = maxHe;
        this.field = field;
    }

    @Override
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append(position.x()).append(",").append(position.y())
                .append(";").append(health)
                .append(";").append(colorCode.replace(";", "-")); // Заменяем ';' на '-' внутри colorCode

        if (!buildings.isEmpty()) {
            sb.append(";");
            for (int i = 0; i < buildings.size(); i++) {
                sb.append(buildings.get(i).getClass().getSimpleName());
                if (i < buildings.size() - 1) sb.append(",");
            }
        }
        return sb.toString();
    }


    public static Castle deserialize(String data, Field field) {
        try {
            int firstSemicolon = data.indexOf(';');
            int secondSemicolon = data.indexOf(';', firstSemicolon + 1);
            int thirdSemicolon = data.indexOf(';', secondSemicolon + 1);

            if (firstSemicolon == -1 || secondSemicolon == -1) {
                System.out.println("❌ Неверный формат данных замка: " + data);
                return null;
            }

            String[] coords = data.substring(0, firstSemicolon).split(",");
            if (coords.length != 2) {
                System.out.println("❌ Ошибка в координатах замка: " + data);
                return null;
            }
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);

            int health = Integer.parseInt(data.substring(firstSemicolon + 1, secondSemicolon));

            String colorCode;
            String buildingsData = null;

            if (thirdSemicolon != -1) {
                colorCode = data.substring(secondSemicolon + 1, thirdSemicolon).replace("-", ";");
                buildingsData = data.substring(thirdSemicolon + 1);
            } else {
                colorCode = data.substring(secondSemicolon + 1).replace("-", ";");
            }

            Castle castle = new Castle(new Position(x, y), colorCode, health, field);

            if (buildingsData != null && !buildingsData.isEmpty()) {
                String[] buildings = buildingsData.split(",");
                for (String name : buildings) {
                    try {
                        Class<?> cls = Class.forName("game.model.building.incastle." + name);
                        BuildingCastle building = (BuildingCastle) cls.getDeclaredConstructor().newInstance();
                        castle.addBuilding(building);
                    } catch (Exception ex) {
                        System.out.println("❌ Не удалось загрузить здание: " + name);
                    }
                }
            }

            return castle;
        } catch (Exception e) {
            System.out.println("❌ Критическая ошибка при загрузке замка: " + data);
            e.printStackTrace();
            return null;
        }
    }


    public boolean isPlayerCastle() {
        return colorCode.equals("34;47");
    }

    public boolean isComputerCastle() {
        return colorCode.equals("31;47");
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

}
