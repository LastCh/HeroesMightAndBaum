package game.model.hero;

import game.api.Position;
import game.map.Field;
import game.model.building.onmap.Castle;
import game.model.unit.GameUnits;
import game.model.unit.Unit;

public abstract class PurchasableHero extends Hero {
    protected final Hero owner;
    protected boolean preferX = true;

    public PurchasableHero(Position position, String color, Castle castle,
                           int movement, int points, int gold, Hero owner) {
        super(position, color, castle, movement, points, gold);
        this.owner = owner;
    }

    public void makeMove(Field field) {
        Unit u = GameUnits.SWORDSMAN.cloneUnit();
        addUnits(u);

        // Проверка на вражеского героя в радиусе 1
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                Position newPos = new Position(position.x() + dx, position.y() + dy);
                if((owner.getPosition().x() != newPos.x()) && (owner.getPosition().y() != newPos.y())) {
                    Hero player = field.getEnemyHeroAt(newPos);
                    if (player != null && movementPoints > 0 && !field.isAlly(this, player)) {
                        System.out.println(this.getClass().getSimpleName() + " (" + this.getOwner().getClass().getSimpleName() +
                                ") атакует " + player.getClass().getSimpleName() +
                                " (" + player.getOwner() + ")");
                        this.attack(player);
                        spendMovementPoints(1);
                        return;
                    }
                }
            }
        }

        // Атака замка, если на его позиции
        Hero targetHero = field.getNearestEnemyHero(this);
        if (targetHero == null) return;

        Castle enemyCastle = field.getCastleAt(targetHero.getMyCastle().getPosition());
        if (enemyCastle != null) {
            Position castlePos = enemyCastle.getPosition();
            int dx = Math.abs(position.x() - castlePos.x());
            int dy = Math.abs(position.y() - castlePos.y());

            if (dx <= 1 && dy <= 1) {
                enemyCastle.takeDamage(power);
                System.out.println("Elf атакует замок с позиции " + position + ", у замка осталось " + enemyCastle.getHealth() + " hp!");
            }
        }

        moveTowardsCastle(field, targetHero.getMyCastle().getPosition()); // Передвижение к замку

    }

    protected void moveTowardsCastle(Field field, Position targetCastle) {
        if (getPosition().equals(targetCastle)) {
            return;
        }
        while (movementPoints > 0) {
            Position current = getPosition();
            int dx = Integer.compare(targetCastle.x(), current.x());
            int dy = Integer.compare(targetCastle.y(), current.y());

            // Попытка двигаться по диагонали (если возможно)
            if (dx != 0 && dy != 0 && tryMove(dx, dy, field)) {
                preferX = !preferX; // Рандомизация следующего приоритета
                continue;
            }

            boolean moved = false;

            if (preferX) {
                moved = tryMove(dx, 0, field);
                if (!moved) moved = tryMove(0, dy, field);
            } else {
                moved = tryMove(0, dy, field);
                if (!moved) moved = tryMove(dx, 0, field);
            }
            if (!moved) break;
        }
    }

    protected boolean tryMove(int dx, int dy, Field field) {
        int d = (dx != 0 && dy != 0) ? 1 : 0;

        Position before = getPosition();
        int beforePoints = getMovementPoints();

        move(dx, dy, field, d); // Используем твой метод с полной логикой

        // Если позиция не изменилась — значит, перемещение не удалось
        boolean moved = !getPosition().equals(before);

        if (moved) {
            System.out.println("Компьютер передвинулся на: " + getPosition());
        }

        return moved;
    }

    @Override
    public abstract String serialize();

    @Override
    public abstract String getClassName();

    public int getMaxMovementPoints() {
        return maxMovementPoints;
    }

    public void setMaxMovementPoints(int points) {
        maxMovementPoints = points;
    }

    @Override
    public Hero getOwner() { return owner; }

}