package game.model.hero;

import game.api.Direction;
import game.api.Position;
import game.map.Field;
import game.model.building.incastle.GameBuildings;
import game.model.building.onmap.Castle;
import game.model.unit.GameUnits;
import game.model.unit.Unit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComputerHero extends Hero {
    private static final String COLOR = "\u001B[31m";
    private final Position targetCastle;
    private boolean preferX = true;
    private boolean stableBought = false;
    private boolean firstUnitBought = false;

    public ComputerHero(Position startPosition, Position targetCastle, int points, Castle castle, int gold) {
        super(startPosition, Direction.UP, COLOR, castle, 10, points, gold);
        this.targetCastle = targetCastle;
        this.power = 0; // Начальная сила
    }


    public void makeMove(Field field) {
        if (units.isEmpty() && noHaveMoney(GameBuildings.STABLE.getCost() + GameUnits.SPEARMAN.getCost())) {
            return;
        } else if(!noHaveMoney(GameBuildings.STABLE.getCost() + GameUnits.SPEARMAN.getCost())){
            autoBuy(field);
        }

        // Проверка на вражеского героя в радиусе 2 клеток
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                Position newPos = new Position(position.x() + dx, position.y() + dy);
                HumanHero player = field.getHumanHeroAt(newPos); // Проверяем каждую позицию в радиусе

                if (player != null && movementPoints > 0) {
                    this.attack(player); // Атака игрока, если он найден в пределах 2 клеток
                    spendMovementPoints(1);
                    return; // После атаки сразу выходим, так как бот не будет двигаться дальше в этом ходу
                }
            }
        }

        // Атака замка, если на его позиции
        Castle enemyCastle = field.getCastleAt(targetCastle);
        if (enemyCastle != null) {
            Position castlePos = enemyCastle.getPosition();
            int dx = Math.abs(position.x() - castlePos.x());
            int dy = Math.abs(position.y() - castlePos.y());

            if (dx <= 1 && dy <= 1) {
                enemyCastle.takeDamage(power);
                System.out.println("Компьютер атакует ваш замок с позиции " + position + ", у него осталось " + enemyCastle.getHealth() + " hp!");
            }
        }

        moveTowardsCastle(field); // Передвижение к замку

    }

    private void autoBuy(Field field) {
        if (!stableBought && !noHaveMoney(GameBuildings.STABLE.getCost())) {
            myCastle.addBuilding(GameBuildings.STABLE);
            spendMoney(GameBuildings.STABLE.getCost());
            stableBought = true;
            System.out.println("Компьютер построил конюшню.");
        }

        if (!firstUnitBought && !noHaveMoney(GameUnits.SPEARMAN.getCost())) {
            Unit u = GameUnits.SPEARMAN.clone();
            spendMoney(u.getCost());
            addUnits(u);
            firstUnitBought = true;
            System.out.println("Компьютер купил первого юнита.");
        }

        List<Unit> unitPool = new ArrayList<>(List.of(
                GameUnits.PALADIN, GameUnits.CAVALRYMAN,
                GameUnits.SWORDSMAN, GameUnits.CROSSBOWMAN,
                GameUnits.SPEARMAN
        ));

        Random rand = new Random();

        while (!unitPool.isEmpty()) {
            Unit base = unitPool.get(rand.nextInt(unitPool.size())); // случайный юнит
            if (noHaveMoney(base.getCost())) {
                unitPool.remove(base); // Если не можем купить — удаляем из пула
                continue;
            }

            Unit copy = base.clone();
            spendMoney(copy.getCost());
            addUnits(copy);
            System.out.println("Компьютер купил " + copy.getClass().getSimpleName() +
                    ". Сила героя: " + this.getPower());
        }
    }

    private void moveTowardsCastle(Field field) {
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


    private boolean tryMove(int dx, int dy, Field field) {
        int d = (dx != 0 && dy != 0) ? 1 : 0;

        Position before = getPosition();
        int beforePoints = getMovementPoints();

        move(dx, dy, field, d);

        // Если позиция не изменилась — значит, перемещение не удалось
        boolean moved = !getPosition().equals(before);

        if (moved) {
            System.out.println("Компьютер передвинулся на: " + getPosition());
        }

        return moved;
    }

    @Override
    public void interact(Hero player) {
    }
}
