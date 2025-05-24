package game.model.hero;

import game.api.Position;
import game.map.Field;
import game.model.building.incastle.BuildingCastle;
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
    private String name;
    private static final double HERO_PURCHASE_CHANCE = 1.0 / 6;
    private static final int MIN_GOLD_FOR_HERO = 200;
    private int turnCounter = 0;
    private int radiusAttack = 1;
    private double karma = 0;

    public ComputerHero() {
        super(new Position(0, 0), COLOR, null, 10, 0, 0); // Можно использовать дефолтные значения или передать их позже
        this.targetCastle = new Position(0, 0);
    }

    public ComputerHero(Position startPosition, Position targetCastle, int points, Castle castle, int gold) {
        super(startPosition, COLOR, castle, 10, points, gold);
        this.targetCastle = targetCastle;
        this.power = (int)(10 * karma);
        this.movementPoints = points;
        this.addGold((int)(100 * karma));
    }

    public void makeMove(Field field) {
        if (!this.isAlive()) {
            handleRevive(field);
            return;
        }

        turnCounter++;

        // Проверяем возможность покупки героя
        if (shouldBuyHero()) {
            buyRandomHero(field);
        }

        // Если нет ни одного юнита и нет золота — ничего не делаем
        if (units.isEmpty() && noHaveMoney(GameBuildings.STABLE.getCost() + GameUnits.SPEARMAN.getCost())) {
            return;
        }

        // Только раз в n ходов покупаем здание и юнитов
        if (!noHaveMoney(GameBuildings.STABLE.getCost() + GameUnits.SPEARMAN.getCost()) && turnCounter % 2 == 0) {
            autoBuy(field);
        }

        // Атака замка
        Castle enemyCastle = field.getCastleAt(targetCastle);
        if (enemyCastle != null) {
            Position castlePos = enemyCastle.getPosition();
            int dx = Math.abs(position.x() - castlePos.x());
            int dy = Math.abs(position.y() - castlePos.y());

            if (dx <= 1 && dy <= 1) {
                enemyCastle.takeDamage(power);
                log("Компьютер атакует ваш замок с позиции " + position +
                        ", у него осталось " + enemyCastle.getHealth() + " hp!");
            }
        }

        if (radiusAttack != 2){ enhanceRadius(units); }

        // Проверка на вражеского героя в радиусе n клеток
        if (attackNearbyPlayerIfExists(field, radiusAttack)) return;

        moveTowardsCastle(field); // Передвижение к замку

    }

    private void handleRevive(Field field) {
        Position respawn = new Position(9, 9);
        field.moveObject(this, this.position.x(), this.position.y(), respawn.x(), respawn.y());
        this.position = respawn;
        this.setUnits(new ArrayList<>());
        log("Герой компьютера возрожден на позиции (9,9)");
    }

    private boolean attackNearbyPlayerIfExists(Field field, int radius) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                Position newPos = new Position(position.x() + dx, position.y() + dy);
                HumanHero player = field.getHumanHeroAt(newPos);
                if (player != null && movementPoints > 0) {
                    this.attack(player);
                    spendMovementPoints(1);
                    log("Компьютер атаковал игрока на позиции " + newPos);
                    return true;
                }
            }
        }
        return false;
    }

    private void autoBuy(Field field) {
        buildStableIfNeeded();
        buyFirstSpearmanIfNeeded();
        buildPreferredOrRandomBuilding();
        buyAvailableUnits();
    }

    private void buildStableIfNeeded() {
        if (!stableBought && !noHaveMoney(GameBuildings.STABLE.getCost())) {
            myCastle.addBuilding(GameBuildings.STABLE);
            spendMoney(GameBuildings.STABLE.getCost());
            stableBought = true;
            log("Компьютер построил конюшню.");
        }
    }

    private void buyFirstSpearmanIfNeeded() {
        if (!firstUnitBought && !noHaveMoney(GameUnits.SPEARMAN.getCost())) {
            Unit u = GameUnits.SPEARMAN.cloneUnit();
            spendMoney(u.getCost());
            addUnits(u);
            firstUnitBought = true;
            log("Компьютер купил первого копейщика.");
        }
    }

    private void buildPreferredOrRandomBuilding() {
        Random rand = new Random();

        if (!myCastle.contains(GameBuildings.TAVERN) && !noHaveMoney(GameBuildings.TAVERN.getCost())) {
            myCastle.addBuilding(GameBuildings.TAVERN);
            spendMoney(GameBuildings.TAVERN.getCost());
            log("Компьютер построил таверну.");
            return;
        }

        List<BuildingCastle> buildingPool = new ArrayList<>(List.of(
                GameBuildings.GUARD_POST,
                GameBuildings.ARMORY,
                GameBuildings.ARENA,
                GameBuildings.CATHEDRAL,
                GameBuildings.CROSSBOWMENS_TOWER
        ));
        buildingPool.removeIf(b -> myCastle.contains(b) || noHaveMoney(b.getCost()));

        if (!buildingPool.isEmpty()) {
            BuildingCastle chosen = buildingPool.get(rand.nextInt(buildingPool.size()));
            myCastle.addBuilding(chosen);
            spendMoney(chosen.getCost());
            System.out.println("Компьютер построил здание: " + chosen.getClass().getSimpleName());
        }
    }

    private void buyAvailableUnits() {
        Random rand = new Random();

        List<Unit> unitPool = new ArrayList<>();

        if (myCastle.contains(GameBuildings.CATHEDRAL)) {
            unitPool.add(GameUnits.PALADIN);
        }
        if (myCastle.contains(GameBuildings.ARENA)) {
            unitPool.add(GameUnits.CAVALRYMAN);
        }
        if (myCastle.contains(GameBuildings.ARMORY)) {
            unitPool.add(GameUnits.SWORDSMAN);
        }
        if (myCastle.contains(GameBuildings.CROSSBOWMENS_TOWER)) {
            unitPool.add(GameUnits.CROSSBOWMAN);
        }
        if (myCastle.contains(GameBuildings.GUARD_POST)) {
            unitPool.add(GameUnits.SPEARMAN);
        }

        while (!unitPool.isEmpty()) {
            Unit base = unitPool.get(rand.nextInt(unitPool.size()));
            if (noHaveMoney(base.getCost())) {
                unitPool.remove(base);
                continue;
            }

            Unit copy = base.cloneUnit();
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
    public String serialize() {
        return name + ";" +
                health + ";" +
                gold + ";" +
                stableBought + ";" +
                firstUnitBought + ";" +
                turnCounter + ";" +
                radiusAttack + ";" +
                preferX + ";" +
                position.x() + "," + position.y() + ";" +
                targetCastle.x() + "," + targetCastle.y() + ";" +
                serializeUnits();
    }

    @Override
    public String getClassName() {
        return "ComputerHero";
    }

    public static ComputerHero deserialize(String data, Field field, Castle myCastle) {
        String[] parts = data.split(";");
        String name = parts[0];
        int health = Integer.parseInt(parts[1]);
        int gold = Integer.parseInt(parts[2]);
        boolean stableBought = Boolean.parseBoolean(parts[3]);
        boolean firstUnitBought = Boolean.parseBoolean(parts[4]);
        int turnCounter = Integer.parseInt(parts[5]);
        int radiusAttack = Integer.parseInt(parts[6]);
        boolean preferX = Boolean.parseBoolean(parts[7]);

        String[] pos = parts[8].split(",");
        String[] targetPos = parts[9].split(",");
        StringBuilder sb = new StringBuilder();
        for (int i = 6; i < parts.length; i++) {
            sb.append(parts[i]);
            if (i < parts.length - 1) sb.append(";");
        }
        String unitData = sb.toString();

        Position position = new Position(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]));
        Position target = new Position(Integer.parseInt(targetPos[0]), Integer.parseInt(targetPos[1]));

        ComputerHero hero = new ComputerHero(position, target, 2, myCastle, gold);
        hero.name = name;
        hero.health = health;
        hero.stableBought = stableBought;
        hero.firstUnitBought = firstUnitBought;
        hero.turnCounter = turnCounter;
        hero.radiusAttack = radiusAttack;
        hero.preferX = preferX;

        hero.deserializeUnits(unitData);
        int totalPower = hero.getUnits().stream()
                .mapToInt(Unit::getPower)
                .sum();
        hero.setPower(totalPower);
        field.getCell(position.x(), position.y()).addObject(hero);
        return hero;
    }


    private boolean shouldBuyHero() {
        // Проверяем, есть ли таверна
        if (!myCastle.contains(GameBuildings.TAVERN)) {
            return false;
        }

        // Проверяем, достаточно ли золота
        if (gold < MIN_GOLD_FOR_HERO) {
            return false;
        }

        // Проверяем шанс покупки
        return Math.random() < HERO_PURCHASE_CHANCE;
    }

    private void buyRandomHero(Field field) {
        Position castlePos = myCastle.getPosition();
        Position spawnPos = field.findFreeAdjacent(castlePos);

        if (spawnPos == null) {
            return;
        }

        // Выбираем случайного героя
        PurchasableHero newHero = null;
        int heroType = (int) (Math.random() * 3);

        switch (heroType) {
            case 0: // Эльф
                if (gold >= 120) {
                    newHero = new ElfHero(spawnPos, "\u001B[95m", myCastle, 3, 1, 120, this);
                    spendMoney(120);
                }
                break;
            case 1: // Орк
                if (gold >= 100) {
                    newHero = new OrcHero(spawnPos, "\u001B[95m", myCastle, 2, 1, 100, this);
                    spendMoney(100);
                }
                break;
            case 2: // Гном
                if (gold >= 90) {
                    newHero = new DwarfHero(spawnPos, "\u001B[95m", myCastle, 1, 2, 90, this);
                    spendMoney(90);
                }
                break;
        }

        if (newHero != null) {
            field.getCell(spawnPos.x(), spawnPos.y()).addObject(newHero);
            field.addHeroToAll(newHero);
            log("Компьютер нанял " + newHero.getClass().getSimpleName() + "!");
        }
    }

    private void log(String msg) {
        System.out.println("[AI] " + msg);
    }

    public void enhanceRadius(ArrayList<Unit> nowUnits) {
        int rangedCount = 0;

        for (Unit u : nowUnits) {
            if (u.getClass().getSimpleName().equals("Crossbowman")) {
                rangedCount++;
            }
        }

        if (rangedCount >= 3) {
            setRadius(2);
        } else {
            setRadius(1);
        }
    }

    public void addBenefit(double karm) {
        karma = Math.max(karm, 0);
        this.power += (int)(100 * karma);
        this.addGold((int)(1000 * karma));
    }

    public int getRadius() { return radiusAttack; }

    public void setRadius(int radius) { this.radiusAttack = radius; }
}