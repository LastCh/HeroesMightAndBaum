package game.model.hero;

import game.api.FieldObject;
import game.api.Position;
import game.map.Field;
import game.model.building.incastle.GameBuildings;
import game.model.building.onmap.Castle;
import game.model.building.onmap.GoldCave;
import game.model.item.MagicalArtifact;
import game.model.monster.Zombie;
import game.model.unit.Unit;

import java.util.ArrayList;
import java.util.List;

public class HumanHero extends Hero {
    private static final String COLOR = "\u001B[34m";
    boolean speedStableBonus = false;
    private MagicalArtifact magicalArtifact;
    private String name;
    private double karma = 0;

    public HumanHero(Position startPosition, int points, Castle castle, int gold) {
        super(startPosition, COLOR, castle, 10, points, gold);
        this.movementPoints = points;
    }

    public void move(int dx, int dy, Field field, int d) {
        Position newPos = new Position(position.x() + dx, position.y() + dy);
        int newDiag = diag + d;
        int cost = movementPoints;
        double newAccumulatedMovementCoef = accumulatedMovementCoef;

        if(!this.isAlive()){
            field.moveObject(this, this.position.x(), this.position.y(), 0, 0);
            Position defPos = new Position(0, 0);
            this.position = defPos;
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
        if (!isValidPosition(newPos)) {
            System.out.println("Невозможно переместиться в эту позицию!");
            return;
        }

        // Проверка возможность перемещения по очкам диагонали
        if (!isValidPoints(newDiag, d)) {
            System.out.println("Недостаточно очков для перемещения!");
            return;
        }

        // Накопление коэффициента
        newAccumulatedMovementCoef += (1.0 - field.getCell(newPos.x(), newPos.y()).getTerrainType().getModifier());

        // Проверка на атаку замка
        if (castleCheck(field, newPos)) {
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
        ComputerHero target = field.getComputerHeroAt(newPos);
        if (target != null) {
            this.attack(target);
            if(!target.isAlive()){
                karma+=0.1;
            }
            spendMovementPoints(1);
            return true;
        } else {
            return false;
        }
    }

    public boolean castleCheck(Field field, Position newPos) {
        Castle castle = field.getCastleAt(newPos);
        if (castle != null) {
            if (castle != myCastle) {
                castle.takeDamage(power);
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
        System.out.println("💥 Артефакт активирован! Враг уничтожен.");
        karma += 0.1;
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
        if (magicalArtifact == null || magicalArtifact.getAmount() <= 0) {
            return false;
        }else {
            return true;
        }
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
        String unitData = parts.length > 6 ? parts[6] : "";

        Position position = new Position(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]));
        HumanHero hero = new HumanHero(position, 10, myCastle, gold);
        hero.name = name;
        hero.health = health;
        hero.speedStableBonus = speedBonus;

        if (artifacts > 0) {
            hero.receiveArtifact(artifacts);
        }

        hero.deserializeUnits(unitData);
        field.getCell(position.x(), position.y()).addObject(hero);
        return hero;
    }

    public double getKarma() { return karma;}

    public void resetKarma() { karma = 0; }

    public void addKarma(double karm) { karma += karm; }
}