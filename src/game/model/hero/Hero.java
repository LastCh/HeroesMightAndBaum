package game.model.hero;

import game.api.Position;
import game.api.FieldObject;
import game.map.Field;
import game.model.building.incastle.GameBuildings;
import game.model.building.onmap.Castle;
import game.model.unit.*;

import java.util.ArrayList;

public abstract class Hero extends FieldObject{
    protected ArrayList<Unit> units = new ArrayList<>();
    protected Castle myCastle;
    protected int maxMovementPoints;
    protected int movementPoints;
    protected int diag = 0;
    protected int health;
    protected int power;
    protected int gold;
    protected double accumulatedMovementCoef = 0.0;
    protected boolean speedStableBonus = false;
    protected Castle castle;
    protected boolean deathCooldown = false;

    public Hero(Position startPosition, String colorCode, Castle castle,
                int priority, int points, int gold) {
        super(startPosition, colorCode + "☻" + " " + "\u001B[0m", priority);
        this.maxMovementPoints = points;
        this.movementPoints = 0;
        this.health = 100;
        this.power = 0;
        this.coloredSymbol = colorCode + "☻" + " " + "\u001B[0m";
        this.myCastle = castle;
        this.gold = gold;
    }

    public boolean isInRange(Hero other, int range) {
        Position otherPos = other.getPosition();
        return Math.abs(position.x() - otherPos.x()) <= range
                && Math.abs(position.y() - otherPos.y()) <= range;
    }

    public void attack(Hero target) {
        target.takeDamage(this.power);
        if(target.getHealth() < 0) {
            target.setHealth(0);
        }
        System.out.printf("%s атаковал %s! Осталось здоровья: %d\n",
                this.getClass().getSimpleName(),
                target.getClass().getSimpleName(),
                target.getHealth()
        );
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) health = 0;
        if (health <= 0) {
            System.out.println(this.getClass().getSimpleName() + " погиб!");
        }
    }

    public void move(int dx, int dy, Field field, int d) {
        Position newPos = new Position(position.x() + dx, position.y() + dy);
        int newDiag = diag + d;
        int cost = movementPoints;
        double newAccumulatedMovementCoef = accumulatedMovementCoef;

        if (units.isEmpty()) return;
        if (isNotValidPosition(newPos)) return;
        if (isNotValidPoints(newDiag, d)) return;

        newAccumulatedMovementCoef += (1.0 - field.getCell(newPos.x(), newPos.y()).getTerrainType().getModifier());

        if (castleCheck(field, newPos)) return;
        if (targetCheck(field, newPos)) return;

        if (newDiag % 2 == 0 && d != 0) cost--;
        cost--;
        cost -= (int)newAccumulatedMovementCoef;

        if (cost < 0) return;

        accumulatedMovementCoef = newAccumulatedMovementCoef - (int)newAccumulatedMovementCoef;
        int finalCost = movementPoints - cost;
        spendMovementPoints(finalCost);

        diag = newDiag;
        field.moveObject(this, this.position.x(), this.position.y(), newPos.x(), newPos.y());
        this.position = newPos;
    }

    public boolean targetCheck(Field field, Position newPos) {
        Hero target = field.getHeroAt(newPos);
        if (target != null && target != this && target.getMyCastle() != this.myCastle) {
            this.attack(target);
            spendMovementPoints(1);
            return true;
        }
        return false;
    }

    public boolean castleCheck(Field field, Position newPos) {
        Castle castle = field.getCastleAt(newPos);
        if (castle != null) {
            if (castle != myCastle) {
                castle.takeDamage(power);
                return true;
            } else {
                if (castle.contains(GameBuildings.STABLE) && !speedStableBonus) {
                    speedStableBonus = true;
                }
                return false;
            }
        }
        return false;
    }

    public boolean isNotValidPosition(Position newPos) {
        return newPos.x() < 0 || newPos.x() >= 10 || newPos.y() < 0 || newPos.y() >= 10;
    }

    public boolean isNotValidPoints(int newDiag, int oldDiag) {
        return (((movementPoints <= 0) || (newDiag % 2 != 1)) &&
                ((movementPoints <= 1) || (newDiag % 2 != 0) || (oldDiag == 0)) &&
                ((movementPoints <= 1) || (oldDiag != 0)));
    }

    public void addUnits(Unit unit) {
        this.units.add(unit);
        this.power += unit.getPower(); // Обновляем силу
    }

    protected String serializeUnits() {
        StringBuilder sb = new StringBuilder();
        for (Unit unit : units) {
            sb.append(unit.getClass().getSimpleName()).append(";");
        }
        return sb.toString();
    }

    protected void deserializeUnits(String data) {
        units.clear();
        if (data == null || data.isEmpty()) return;

        String[] parts = data.split(";");
        for (String unitName : parts) {
            Unit unit = switch (unitName) {
                case "Swordsman" -> new Swordsman();
                case "Spearman" -> new Spearman();
                case "Cavalryman" -> new Cavalryman();
                case "Crossbowman" -> new Crossbowman();
                case "Paladin" -> new Paladin();
                default -> null;
            };
            if (unit != null) {
                this.addUnits(unit);
            }
        }
    }


    public void setGold(int count) { this.gold = count; }

    public int getGold() { return this.gold; }

    public Castle getMyCastle() { return this.myCastle; }

    public boolean isAlive() { return health > 0; }

    public void move(int dx, int dy, Field field) {
        updatePosition(position.x() + dx, position.y() + dy);
    }

    public void updatePosition(int newX, int newY) { this.position = new Position(newX, newY); }

    @Override
    public String getColoredSymbol() { return String.format("%-3s", coloredSymbol); }

    public void setHealth(int newHealth) { health = newHealth; }

    public int getHealth() { return health; }

    public void setPower(int newPower) { power = newPower; }

    public int getPower() { return power; }

    public int getMovementPoints() { return movementPoints; }

    public void spendMovementPoints(int points) { movementPoints -= points; }

    public void resetMovementPoints() {
        movementPoints = maxMovementPoints;
        diag = 0;
        accumulatedMovementCoef = 0.0;
    }

    public Hero getOwner() { return this; }

    public ArrayList<Unit> getUnits() { return units; }

    public void setUnits(ArrayList<Unit> uni) { this.units = uni; }

    public boolean noHaveMoney(int cost) { return !(this.gold >= cost); }

    public void addGold(int money) { gold += money; }

    public void spendMoney(int cost) { gold -= cost; }

    public Field getField() { return castle.getField(); }

    public boolean isInDeathCooldown() { return deathCooldown; }

    public void setDeathCooldown(boolean state) { this.deathCooldown = state; }
}