package game.model.hero;

import game.api.FieldObject;
import game.api.Position;
import game.map.Field;
import game.model.building.incastle.GameBuildings;
import game.model.building.onmap.*;
import game.model.item.MagicalArtifact;
import game.model.monster.Zombie;
import game.model.unit.Unit;
import game.service.ServiceType;

import java.util.ArrayList;
import java.util.List;

public class HumanHero extends Hero implements ServiceVisitor {
    private static final String COLOR = "\u001B[34m";
    boolean speedStableBonus = false;
    private MagicalArtifact magicalArtifact;
    private String name;
    private double karma = 0;
    private boolean fastCastleCapture = false;
    private int maxHP;

    public HumanHero(Position startPosition, int points, Castle castle, int gold) {
        super(startPosition, COLOR, castle, 10, points, gold);
        this.movementPoints = points;
        this.maxHP = health;
    }

    public void move(int dx, int dy, Field field, int d) {
        Position newPos = new Position(position.x() + dx, position.y() + dy);
        int newDiag = diag + d;
        int cost = movementPoints;
        double newAccumulatedMovementCoef = accumulatedMovementCoef;

        if(!this.isAlive()){
            field.moveObject(this, this.position.x(), this.position.y(), 0, 0);
            this.position = new Position(0, 0);
            ArrayList<Unit> newUni = new ArrayList<>();
            this.setUnits(newUni);
            return;
        }

        // Проверка на наличие отряда
        if (units.isEmpty()) {
            System.out.println("У вашего героя нет юнитов, купите их!");
            return;
        }

        // Проверка возможность перемещения по позиции
        if (isNotValidPosition(newPos)) {
            System.out.println("Невозможно переместиться в эту позицию!");
            return;
        }

        // Проверка возможность перемещения по очкам диагонали
        if (isNotValidPoints(newDiag, d)) {
            System.out.println("Недостаточно очков для перемещения!");
            return;
        }

        // Накопление коэффициента
        newAccumulatedMovementCoef += (1.0 - field.getCell(newPos.x(), newPos.y()).getTerrainType().getModifier());

        // Проверка на атаку замка
        if (interactWithBuildings(field, newPos)) {
            return;
        }

        // Проверка на атаку противника
        if (targetCheck(field, newPos)) {
            return;
        }

        // Трата второго очка передвижения после второй диагонали
        if (newDiag % 2 == 0 && d != 0) {
            cost--;
        }

        // Трата очков за каждый ход и очков коэффициента поля
        cost--;
        cost -= (int)newAccumulatedMovementCoef;

        if(cost < 0){
            System.out.println("Недостаточно очков для перемещения!");
            return;
        }

        accumulatedMovementCoef = newAccumulatedMovementCoef - (int)newAccumulatedMovementCoef;
        int finalCost = movementPoints - cost;
        spendMovementPoints(finalCost);
        if(dx != 0 || dy != 0) {
            System.out.println("Вы переместились на клетку: (" + newPos.x() + ";" + newPos.y() + ")");
        }
        diag = newDiag;
        field.moveObject(this, this.position.x(), this.position.y(), newPos.x(), newPos.y());
        this.position = newPos;

        // Взаимодействие с пещерой после того, как персонаж переместился
        interactWithGoldCave(field, newPos);
    }

    private void interactWithGoldCave(Field field, Position newPos) {
        // Проверяем только текущую клетку, есть ли в ней Золотая пещера
        GoldCave cave = (GoldCave) field.getCell(newPos.x(), newPos.y()).getObjects().stream()
                .filter(obj -> obj instanceof GoldCave)
                .findFirst().orElse(null);

        if (cave != null && !cave.isInCave()) {
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");
            cave.interact(this); // Взаимодействие с пещерой
            field.removeCave(cave);
        }
    }

    public boolean targetCheck(Field field, Position newPos) {
        // Проверяем героя-компьютера
        ComputerHero computerHero = field.getComputerHeroAt(newPos);
        if (computerHero != null) {
            this.attack(computerHero);
            if (!computerHero.isAlive()) {
                karma += 0.1;
            }
            spendMovementPoints(1);
            return true;
        }

        // Проверяем наёмников компьютера (PurchasableHero)
        Hero enemy = field.getHeroAt(newPos);
        if (enemy instanceof PurchasableHero purchasable && purchasable.getOwner() instanceof ComputerHero) {
            this.attack(enemy);
            if (!enemy.isAlive()) {
                karma += 0.05;
                field.removePlayer(enemy);
            }
            spendMovementPoints(1);
            return true;
        }

        return false;
    }

    public boolean interactWithBuildings(Field field, Position newPos) {
        Castle castle = field.getCastleAt(newPos);
        if (castle != null) {
            if (castle != myCastle) {
                if(fastCastleCapture){
                    castle.takeDamage(2*power);
                }else{
                    castle.takeDamage(power);
                }
                System.out.println("Вы атакуете вражеский замок!");
            } else {
                System.out.println("Вы находитесь в своём замке!");
                if (castle.contains(GameBuildings.STABLE) && !speedStableBonus) {
                    System.out.println("В вашем замке есть конюшня, ваши характеристики передвижения улучшены!");
                    speedStableBonus = true;
                    maxMovementPoints++;
                }
                return false;
            }
            return true;
        }

        for (FieldObject obj : field.getCell(newPos.x(), newPos.y()).getObjects()) {
            if (obj instanceof Enterprise enterprise) {
                System.out.println("🏪 Вы вошли в здание: " + enterprise.getClassName());

                int choice;
                do {
                    choice = enterprise.displayMenu();

                    switch (enterprise.getClassName()) {
                        case "Barbershop" -> {
                            if (choice == 1) enterprise.enter(this, Barbershop.SIMPLE_CUT);
                            else if (choice == 2) enterprise.enter(this, Barbershop.FASHION_CUT);
                        }
                        case "Hotel" -> {
                            if (choice == 1) enterprise.enter(this, Hotel.SHORT_REST);
                            else if (choice == 2) enterprise.enter(this, Hotel.LONG_REST);
                        }
                        case "Restaurant" -> {
                            if (choice == 1) enterprise.enter(this, Restaurant.SNACK);
                            else if (choice == 2) enterprise.enter(this, Restaurant.LUNCH);
                        }
                    }

                    if (choice == 3) {         // «Выйти» в вашем меню
                        enterprise.shutdown();
                        break;
                    }
                } while (true);

                return false;
            }
        }

        return false;
    }

    public void receiveArtifact(int count) {
        if (magicalArtifact == null) {
            this.magicalArtifact = new MagicalArtifact(count, this);
        } else {
            magicalArtifact.addArtifact(count);
        }
        System.out.println("Вы получили " + count + " артефакта(ов)!");
    }

    public boolean useArtifact(Hero target) {
        if (magicalArtifact == null || magicalArtifact.getAmount() <= 0) {
            System.out.println("У вас нет артефакта!");
            return false;
        }
        magicalArtifact.spendArtifact();
        if(magicalArtifact.getAmount() == 0) {
            magicalArtifact = null;
        }
        target.takeDamage(target.getHealth());
        System.out.println("💥 Артефакт активирован! Вы испепелили всё живое на клетке.");
        karma += 0.3;
        return true;
    }

    public void moveInCave(int dx, int dy, Field caveField) {
        Position current = this.getPosition();
        int newX = current.x() + dx;
        int newY = current.y() + dy;

        if (newX < 0 || newY < 0 || newX >= caveField.getWidth() || newY >= caveField.getHeight()) {
            System.out.println("Вы не можете пройти в эту сторону – стена.");
            return;
        }

        Position newPos = new Position(newX, newY);
        List<FieldObject> objectsAtNewPos = caveField.getCell(newX, newY).getObjects();

        Zombie zombie = null;
        for (FieldObject obj : objectsAtNewPos) {
            if (obj instanceof Zombie z && !z.isDead()) {
                zombie = z;
                break;
            }
        }

        if (zombie != null) {
            System.out.println("Вы наступаете на зомби! Он погибает в агонии...");
            zombie.takeDamage(zombie.getHealth()); // Убиваем зомби
            if (zombie.isDead()) {
                caveField.getCell(newX, newY).removeObject(zombie);
                System.out.println("Зомби уничтожен!");
            }
        }

        caveField.getCell(current.x(), current.y()).removeObject(this);
        this.setPosition(newPos);
        caveField.getCell(newX, newY).addObject(this);

        System.out.println("Вы переместились в клетку: " + newPos);
    }

    public boolean hasArtifact() {
        return magicalArtifact != null && magicalArtifact.getAmount() > 0;
    }

    @Override
    public String serialize() {
        return name + ";" +
                health + ";" +
                gold + ";" +
                speedStableBonus + ";" +
                (magicalArtifact != null ? magicalArtifact.getAmount() : 0) + ";" +
                position.x() + "," + position.y() + ";" +
                serializeUnits();
    }

    @Override
    public String getClassName() {
        return "HumanHero";
    }

    public static HumanHero deserialize(String data, Field field, Castle myCastle) {
        String[] parts = data.split(";");
        String name = parts[0];
        int health = Integer.parseInt(parts[1]);
        int gold = Integer.parseInt(parts[2]);
        boolean speedBonus = Boolean.parseBoolean(parts[3]);
        int artifacts = Integer.parseInt(parts[4]);
        String[] pos = parts[5].split(",");
        StringBuilder sb = new StringBuilder();
        for (int i = 6; i < parts.length; i++) {
            sb.append(parts[i]);
            if (i < parts.length - 1) sb.append(";");
        }
        String unitData = sb.toString();

        Position position = new Position(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]));
        HumanHero hero = new HumanHero(position, 10, myCastle, gold);
        hero.name = name;
        hero.health = health;
        hero.speedStableBonus = speedBonus;

        if (artifacts > 0) {
            hero.receiveArtifact(artifacts);
        }

        hero.deserializeUnits(unitData);
        int totalPower = hero.getUnits().stream()
                .mapToInt(Unit::getPower)
                .sum();
        hero.setPower(totalPower);
        field.getCell(position.x(), position.y()).addObject(hero);
        return hero;
    }

    public double getKarma() { return karma;}

    public void resetKarma() { karma = 0; }

    public void addKarma(double karm) { karma += karm; }

    public String getName() {
        return "Герой";
    }

    public void applyBonus(ServiceType service) {

    }

    public void healUnits(int heal) {
        this.maxHP += heal;
        this.health = maxHP;
        System.out.println("🛌 Отряд отдохнул " + heal + " HP. Текущее: " + this.health);
    }

    public void boostMovement(int points) {
        this.movementPoints += points;
        System.out.println("💨 Герой получил +" + points + " к очкам передвижения. Текущие: " + this.movementPoints);
    }

    public void reduceCastleCaptureTime() {
        fastCastleCapture = true;
        System.out.println("🏰 Теперь вы быстрее захватите замок!");
    }
}