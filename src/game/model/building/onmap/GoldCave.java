package game.model.building.onmap;

import game.api.FieldObject;
import game.api.Immovable;
import game.api.Position;
import game.map.Field;
import game.model.hero.HumanHero;
import game.model.monster.Zombie;
import game.model.hero.Hero;

import java.util.List;
import java.util.Scanner;

public class GoldCave extends FieldObject implements Immovable {
    private final int goldAmount;
    private boolean completed = false;
    private final Field caveField;
    private boolean inCave = false;
    private final Scanner scanner = new Scanner(System.in);
    private final Position caveEntry = new Position(0, 0);

    public GoldCave(Position position, int goldAmount) {
        super(position, "\u001B[33m" + "\u001B[41m" + "💰" + "\u001B[0m", 4);
        this.goldAmount = goldAmount;

        // Создаем мини-подземелье 5x5
        caveField = new Field(5, 5);
        for (int i = 0; i < 3; i++) {
            caveField.getCell(1 + i, 1).addObject(new Zombie(new Position(1 + i, 1), 100));
        }
    }

    @Override
    public void interact(Hero player) {}

    public void interact(HumanHero player) {
        if (completed) {
            System.out.println("Пещера уже очищена.");
            return;
        }

        System.out.println("Игрок входит в Золотую пещеру!");
        player.setPosition(caveEntry);
        caveField.getCell(caveEntry.x(), caveEntry.y()).addObject(player);
        inCave = true;

        while (true) {
            clearConsole();
            caveField.render();
            System.out.println("Вы находитесь в клетке: " + player.getPosition());
            System.out.println("Выберите действие:");
            System.out.println("[W] Вверх");
            System.out.println("[S] Вниз");
            System.out.println("[A] Влево");
            System.out.println("[D] Вправо");
            System.out.println("[L] Сдаться и выйти (без награды)");

            String input = scanner.nextLine().trim().toUpperCase();;

            switch (input) {
                case "W" -> {
                    player.moveInCave(0, -1, caveField);
                    if (areAllZombiesDead()) {
                        System.out.println("Вы победили всех зомби и получаете артефакт!");
                        player.receiveArtifact();
                        completed = true;
                        removePlayerFromCave(player);
                        return;
                    }
                }
                case "S" ->{
                    player.moveInCave(0, 1, caveField);
                    if (areAllZombiesDead()) {
                        System.out.println("Вы победили всех зомби и получаете артефакт!");
                        player.receiveArtifact();
                        completed = true;
                        removePlayerFromCave(player);
                        return;
                    }
                }
                case "A" -> {
                    player.moveInCave(-1, 0, caveField);
                    if (areAllZombiesDead()) {
                        System.out.println("Вы победили всех зомби и получаете артефакт!");
                        player.receiveArtifact();
                        completed = true;
                        removePlayerFromCave(player);
                        return;
                    }
                }
                case "D" -> {
                    player.moveInCave(1, 0, caveField);
                    if (areAllZombiesDead()) {
                        System.out.println("Вы победили всех зомби и получаете артефакт!");
                        player.receiveArtifact();
                        completed = true;
                        removePlayerFromCave(player);
                        return;
                    }
                }
                case "L" -> {
                    System.out.println("Вы покинули пещеру.");
                    removePlayerFromCave(player);
                    return;
                }
                default -> System.out.println("Неизвестная команда.");
            }

            if (areAllZombiesDead()) {
                System.out.println("Вы победили всех зомби и получаете артефакт!");
                player.receiveArtifact();
                completed = true;
                removePlayerFromCave(player);
                return;
            }
        }

    }

    private void attackNearestZombie(Hero player) {
        Zombie target = findNearestZombie(player.getPosition());
        if (target != null) {
            System.out.println("Вы атакуете ближайшего зомби!");
            target.takeDamage(50); // Условный урон
            if (target.isDead()) {
                System.out.println("Зомби уничтожен!");
                caveField.getCell(target.getPosition().x(), target.getPosition().y()).removeObject(target);
            } else {
                System.out.println("Зомби ранен, но ещё жив.");
            }
        } else {
            System.out.println("Врагов рядом нет.");
        }
    }

    private Zombie findNearestZombie(Position from) {
        int radius = 2; // Радиус поиска
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                int nx = from.x() + dx;
                int ny = from.y() + dy;
                if (nx >= 0 && ny >= 0 && nx < 5 && ny < 5) {
                    List<FieldObject> objects = caveField.getCell(nx, ny).getObjects();
                    for (FieldObject obj : objects) {
                        if (obj instanceof Zombie zombie && !zombie.isDead()) {
                            return zombie;
                        }
                    }
                }
            }
        }
        return null;
    }

    private boolean areAllZombiesDead() {
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                for (FieldObject obj : caveField.getCell(x, y).getObjects()) {
                    if (obj instanceof Zombie zombie && !zombie.isDead()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void removePlayerFromCave(Hero player) {
        caveField.getCell(player.getPosition().x(), player.getPosition().y()).removeObject(player);
        inCave = false;
        // Можно вернуть игрока в исходную точку карты
        player.setPosition(this.getPosition());
    }

    public boolean isInCave() {
        return inCave;
    }
    private void clearConsole() {
        for (int i = 0; i < 50; i++) System.out.println();
    }
}
