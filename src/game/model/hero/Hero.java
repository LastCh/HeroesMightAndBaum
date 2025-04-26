package game.model.hero;

import game.api.Direction;
import game.api.Movable;
import game.api.Position;
import game.api.FieldObject;
import game.map.Field;
import game.model.building.incastle.GameBuildings;
import game.model.building.onmap.Castle;
import game.model.unit.Unit;
import java.util.ArrayList;

public abstract class Hero extends FieldObject implements Movable {
    protected ArrayList<Unit> units = new ArrayList<>();
    protected Direction direction;
    protected Castle myCastle;
    protected int maxMovementPoints;
    protected int movementPoints;
    protected int diag = 0;
    protected int health;
    protected int power;
    protected int gold;
    protected double accumulatedMovementCoef = 0.0;
    protected boolean speedStableBonus = false;

    public Hero(Position startPosition, Direction startDirection, String colorCode, Castle castle,
                int priority, int points, int gold) {
        super(startPosition, colorCode + "☻" + " " + "\u001B[0m", priority);
        this.direction = startDirection;
        this.maxMovementPoints = points;
        this.movementPoints = points;
        this.health = 100;  // Добавить
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
        System.out.printf("%s атаковал %s! Осталось здоровья: %d\n",
                this.getClass().getSimpleName(),
                target.getClass().getSimpleName(),
                target.getHealth()
        );
    }

    public void takeDamage(int damage) {
        health -= damage;
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
        if (!isValidPosition(newPos)) return;
        if (!isValidPoints(newDiag, d)) return;

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

    public boolean isValidPosition(Position newPos) {
        return newPos.x() >= 0 && newPos.x() < 10 && newPos.y() >= 0 && newPos.y() < 10;
    }

    public boolean isValidPoints(int newDiag, int oldDiag) {
        return (((movementPoints > 0) && (newDiag % 2 == 1)) ||
                ((movementPoints > 1) && (newDiag % 2 == 0) && (oldDiag != 0)) ||
                ((movementPoints > 1) && (oldDiag == 0)));
    }

    // Новый абстрактный метод — поведение героя
    public abstract void makeMove(Field field);


    public void setGold(int count) {
        this.gold = count;
    }

    public int getGold() {
        return this.gold;
    }

    public Castle getMyCastle() {
        return this.myCastle;
    }

    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public void move(int dx, int dy, Field field) {
        updatePosition(position.x() + dx, position.y() + dy);
    }

    public void updatePosition(int newX, int newY) {
        this.position = new Position(newX, newY);
    }

    @Override
    public void turn(Direction direction) {
        this.direction = direction;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public String getColoredSymbol() {
        return String.format("%-3s", coloredSymbol); // Фиксированная ширина в 3 символа
    }

    public void setHealth(int newHealth) {
        health = newHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setPower(int newPower) {
        power = newPower;
    }

    public int getPower() {
        return power;
    }

    public int getMovementPoints() {
        return movementPoints;
    }

    public void spendMovementPoints(int points) {
        movementPoints -= points;
    }

    public void resetMovementPoints() {
        movementPoints = maxMovementPoints;
        diag = 0;
        accumulatedMovementCoef = 0.0;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<Unit> uni) {
        this.units = uni;
    }

    public void addUnits(Unit unit) {
        this.units.add(unit);
    }

    public boolean noHaveMoney(int cost) {
        return !(this.gold >= cost);
    }

    public void spendMoney(int cost) {
        gold -= cost;
    }
}
