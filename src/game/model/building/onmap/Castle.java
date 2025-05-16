package game.model.building.onmap;

import game.api.Immovable;
import game.api.Position;
import game.api.FieldObject;
import game.map.Field;
import game.model.building.incastle.BuildingCastle;

import java.util.ArrayList;
import java.util.Scanner;

public class Castle extends FieldObject implements Immovable {
    private final ArrayList<BuildingCastle> buildings = new ArrayList<>(8);
    private final Scanner scanner;
    private int health;
    private final int maxHealth;
    private final Field field;

    public Castle(Position position, Scanner scanner, int maxHe, Field field) {
        super(position, "♜", 2); // Символ замка и его приоритет
        this.scanner = scanner;
        this.health = maxHe;
        this.maxHealth = maxHe;
        this.field = field;
    }

    public boolean contains(BuildingCastle obj) {
        return buildings.contains(obj);
    }

    public void addBuilding(BuildingCastle build) {
        buildings.add(build);
    }

    public Castle(Position position, Scanner scanner, String color, int maxHe, Field field) {
        super(position, color + "♜" + color + "♜" + "\u001B[0m", 2); // Символ замка и его приоритет
        this.scanner = scanner;
        this.health = maxHe;
        this.maxHealth = maxHe;
        this.field = field;
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
