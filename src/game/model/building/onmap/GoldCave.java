package game.model.building.onmap;

import game.api.FieldObject;
import game.api.Immovable;
import game.api.Position;
import game.map.DungeonField;
import game.model.hero.HumanHero;
import game.model.monster.Zombie;
import game.model.hero.Hero;

import java.util.Scanner;

public class GoldCave extends FieldObject implements Immovable {
    private final int goldAmount;
    private boolean completed = false;
    private DungeonField dungeonField;
    private boolean inCave = false;
    private final Scanner scanner = new Scanner(System.in);
    private final Position caveEntry = new Position(0, 0);

    public GoldCave(Position position, int goldAmount) {
        super(position, "\u001B[33m" + "\u001B[41m" + "💰" + "\u001B[0m", 4);
        this.goldAmount = goldAmount;
        int min = 1;
        int max = 500;
        int randomNumber = (int)(Math.random() * (max - min + 1)) + min;
        goldAmount = randomNumber;

        // Создаем мини-подземелье 5x5
        dungeonField = new DungeonField(5, 5);
        dungeonField.AddZombies();

    }

    public void interact(HumanHero player) {
        if (completed) {
            System.out.println("Пещера уже очищена.");
            return;
        }

        System.out.println("Игрок входит в Золотую пещеру!");
        player.setPosition(caveEntry);
        dungeonField.getCell(caveEntry.x(), caveEntry.y()).addObject(player);
        inCave = true;

        while (true) {
            clearConsole();
            dungeonField.render();
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
                    player.moveInCave(0, -1, dungeonField);
                    if (areAllZombiesDead()) {
                        System.out.println("Вы победили всех зомби и получаете артефакт!");
                        player.receiveArtifact();
                        completed = true;
                        removePlayerFromCave(player);
                        return;
                    }
                }
                case "S" ->{
                    player.moveInCave(0, 1, dungeonField);
                    if (areAllZombiesDead()) {
                        System.out.println("Вы победили всех зомби и получаете артефакт!");
                        player.receiveArtifact();
                        completed = true;
                        removePlayerFromCave(player);
                        return;
                    }
                }
                case "A" -> {
                    player.moveInCave(-1, 0, dungeonField);
                    if (areAllZombiesDead()) {
                        System.out.println("Вы победили всех зомби и получаете артефакт!");
                        player.receiveArtifact();
                        completed = true;
                        removePlayerFromCave(player);
                        return;
                    }
                }
                case "D" -> {
                    player.moveInCave(1, 0, dungeonField);
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
                System.out.println("Вы победили всех зомби и получаете "+ goldAmount +" золота с артефактом!");
                player.receiveArtifact();
                player.addGold(goldAmount);
                completed = true;
                removePlayerFromCave(player);
                return;
            }
        }

    }

    private boolean areAllZombiesDead() {
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                for (FieldObject obj : dungeonField.getCell(x, y).getObjects()) {
                    if (obj instanceof Zombie zombie && !zombie.isDead()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void removePlayerFromCave(Hero player) {
        dungeonField.getCell(player.getPosition().x(), player.getPosition().y()).removeObject(player);
        inCave = false;
        // Можно вернуть игрока в исходную точку карты
        player.setPosition(this.getPosition());
    }

    public boolean isInCave() {
        return inCave;
    }

    public int getGoldAmount() {
        return goldAmount;
    }

    private void clearConsole() {
        for (int i = 0; i < 50; i++) System.out.println();
    }
}
