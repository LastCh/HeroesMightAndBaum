package game.model.hero;

import game.api.Direction;
import game.api.FieldObject;
import game.api.Position;
import game.map.Field;
import game.model.building.incastle.GameBuildings;
import game.model.building.onmap.Castle;
import game.model.building.onmap.GoldCave;
import game.model.item.MagicalArtifact;
import game.model.monster.Zombie;

import java.util.List;

public class HumanHero extends Hero {
    private static final String COLOR = "\u001B[34m";
    boolean speedStableBonus = false;
    private MagicalArtifact magicalArtifact;

    public HumanHero(Position startPosition, int points, Castle castle, int gold) {
        super(startPosition, Direction.UP, COLOR, castle, 10, points, gold);
    }

    @Override
    public void interact(Hero player) {
        System.out.println("Игрок взаимодействует с другим игроком.");
    }

    public void move(int dx, int dy, Field field, int d) {

        Position newPos = new Position(position.x() + dx, position.y() + dy);
        int newDiag = diag + d;
        int cost = movementPoints;
        double newAccumulatedMovementCoef = accumulatedMovementCoef;

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

        // Проверка стоимости
        if(cost < 0){
            System.out.println("Недостаточно очков для перемещения!");
            return;
        }

        accumulatedMovementCoef = newAccumulatedMovementCoef - (int)newAccumulatedMovementCoef;
        int finalCost = movementPoints - cost;
        spendMovementPoints(finalCost);
        if(dx != 0 || dy != 0) {
            System.out.println("Вы переместились на: " + newPos);
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
            System.out.println("Вы нашли Золотую пещеру и заходите внутрь!");
            cave.interact(this); // Взаимодействие с пещерой
            field.removeCave(cave);
        }
    }

    public boolean targetCheck(Field field, Position newPos) {
        ComputerHero target = field.getComputerHeroAt(newPos);
        if (target != null) {
            this.attack(target);
            spendMovementPoints(1);
            return true;
        } else {
            return false;
        }
    }

    public boolean castleCheck(Field field, Position newPos) {
        Castle castle = field.getCastleAt(newPos);
        if (castle != null) {
            if (castle != myCastle) { // Атакуем только ЧУЖИЕ замки
                castle.takeDamage(power);
                System.out.println("Вы атакуете вражеский замок!");
            } else {
                System.out.println("Вы находитесь в своём замке!");
                if (castle.contains(GameBuildings.STABLE) && !speedStableBonus) {
                    System.out.println("В вашем замке есть конюшня, ваши характеристики передвижения улучшены!");
                    speedStableBonus = true;
                }
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public void makeMove(Field field) {
    }

    public boolean isValidPosition(Position newPos) {
        return newPos.x() >= 0 && newPos.x() < 10 && newPos.y() >= 0 && newPos.y() < 10;
    }

    public boolean isValidPoints(int newDiag, int oldDiag) {
        return (((movementPoints > 0) && (newDiag % 2 == 1)) ||
                ((movementPoints > 1) && (newDiag % 2 == 0) && (oldDiag != 0)) ||
                ((movementPoints > 1) && (oldDiag == 0)));
    }

    public void receiveArtifact() {
        if(magicalArtifact == null) {
            this.magicalArtifact = new MagicalArtifact(1,this);
        }else{
            magicalArtifact.addArtifact(1);
        }
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

        // Проверяем, есть ли живой зомби в клетке
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

        // Перемещаем героя
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

}
