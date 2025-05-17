package game.core.engine;

import game.api.Position;
import game.interf.GameMenu;
import game.interf.MenuManager;
import game.map.Field;
import game.model.building.onmap.Castle;
import game.model.hero.ComputerHero;
import game.model.hero.Hero;
import game.model.hero.HumanHero;
import game.model.hero.PurchasableHero;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static game.api.LogConfig.LOGGER;
import static game.interf.Inter.RED;
import static game.interf.Inter.RESET;
import static game.interf.Inter.CYAN;

public class Render {
    private final Field field;
    private final HumanHero player;
    private final ComputerHero computerPlayer;
    private final Scanner scanner;
    private final MenuManager menu = new MenuManager();
    private final GameMenu gameMenu = new GameMenu();
    private final Castle castlePlayer;
    private final Castle castleBot;

    public Render(Field field, HumanHero player, Scanner sharedScanner,
                  ComputerHero computer, Castle playerCastle, Castle botCastle) {
        this.field = field;
        this.player = player;
        this.scanner = sharedScanner;
        this.computerPlayer = computer;
        this.castlePlayer = playerCastle;
        this.castleBot = botCastle;
    }

    public void startGameLoop(GameManager gmanager) {
        System.out.println("Для начала игры купите первое здание в свой замок и наймите юнитов, чтобы герой смог ходить!");
        while (true) {
            LOGGER.info("Игровой цикл запущен");
            renderField();

            // Показываем управление
            gameMenu.display();

            // Считаем ввод
            String input = scanner.nextLine().trim().toUpperCase();

            switch (input) {
                case "W" -> {
                    clearConsole();
                    player.move(0, -1, field, 0);
                }
                case "S" -> {
                    clearConsole();
                    player.move(0, 1, field, 0);
                }
                case "A" -> {
                    clearConsole();
                    player.move(-1, 0, field,0);
                }
                case "D" -> {
                    clearConsole();
                    player.move(1, 0, field,0 );
                }
                case "Q" -> {
                    clearConsole();
                    endTurn();
                }
                case "X" -> {
                    clearConsole();
                    useArtifact();
                }
                case "SD", "DS" -> {
                    clearConsole();
                    player.move(1, 1, field, 1);
                }
                case "AS", "SA" -> {
                    clearConsole();
                    player.move(-1, 1, field, 1);
                }
                case "AW", "WA" -> {
                    clearConsole();
                    player.move(-1, -1, field, 1);
                }
                case "WD", "DW" -> {
                    clearConsole();
                    player.move(1, -1, field, 1);
                }

                case "M" -> {
                    clearConsole();
                    boolean back = menu.showGameMenu(player, computerPlayer, gmanager);
                    if (back) return;
                }

                default -> {
                    clearConsole();
                    LOGGER.warning("Неверный ввод команды: " + input);
                    System.out.println(RED + "❌ Неверный выбор. Попробуйте снова." + RESET);
                }
            }

            if (checkCastleCaptured(gmanager)) {
                break;
            }

            if (!player.isAlive()) {
                System.out.println("Ваш герой погиб!");
                player.addKarma(-0.1);
                player.resetMovementPoints();
                player.setGold(200);
                player.move(0,0,field, 0);
                player.setHealth(100);
            }
            if (!computerPlayer.isAlive()) {
                computerPlayer.resetMovementPoints();
                computerPlayer.setGold(200);
                computerPlayer.makeMove(field);
                computerPlayer.setHealth(100);
            }
        }
    }

    private void endTurn() {
        clearConsole();
        player.resetMovementPoints();

        if (computerPlayer.isAlive()) {
            computerPlayer.resetMovementPoints();
            computerPlayer.makeMove(field);
            computerPlayer.addGold(200);
        }

        moveAllHeroes();

        player.addGold(200);
    }

    private void useArtifact() {
        clearConsole();
        if (!player.hasArtifact()) {
            LOGGER.warning("Попытка использования артефакта без наличия");
            System.out.println("У вас нет артефакта!");
            return;
        }
        try {
            System.out.print("X: ");
            int x = scanner.nextInt();

            System.out.print("Y: ");
            int y = scanner.nextInt();

            scanner.nextLine();
            Position pos = new Position(x, y);
            Hero target = field.getHeroAt(pos);

            if (target != null && target != player && player.useArtifact(target)) {
                field.removePlayer(target);
            } else {
                System.out.println("Нет подходящей цели.");
            }
        } catch (Exception e) {
            System.out.println("Ошибка ввода.");
            LOGGER.severe("Ошибка при использовании артефакта: " + e.getMessage());
            scanner.nextLine();
        }
    }

    private void moveAllHeroes() {
        List<PurchasableHero> heroes = field.getAllHeroes();

        for (PurchasableHero hero : heroes) {
            if (hero.isAlive()) {
                hero.makeMove(field);
                hero.resetMovementPoints();
            }else{
                field.removePlayer(hero);
            }
        }
    }

    private void renderField() {
        field.render();
        menu.getGameMenu().display(
                player.getMovementPoints(),
                castlePlayer.getHealth(),
                castleBot.getHealth(),
                player.getGold(),
                player.getHealth(),
                player.getPower()
        );
    }

    private static void addVictory(String playerName) {
        File file = new File("players.txt");
        List<String[]> entries = new ArrayList<>();
        boolean updated = false;

        // Считываем и обновляем победы
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts[0].equalsIgnoreCase(playerName)) {
                        double karma = Double.parseDouble(parts[1]);
                        int wins = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;
                        wins++;
                        entries.add(new String[]{playerName, String.valueOf(karma), String.valueOf(wins)});
                        updated = true;
                    } else {
                        entries.add(parts);
                    }
                }
            } catch (IOException | NumberFormatException e) {
                LOGGER.warning("Ошибка при чтении players.txt: " + e.getMessage());
            }
        }

        // Если игрок не найден, добавляем новую строку
        if (!updated) {
            entries.add(new String[]{playerName, "0.0", "1"});
        }

        // Сортировка по победам (в убывающем порядке)
        entries.sort((a, b) -> {
            int winsA = a.length > 2 ? Integer.parseInt(a[2]) : 0;
            int winsB = b.length > 2 ? Integer.parseInt(b[2]) : 0;
            return Integer.compare(winsB, winsA);
        });

        // Записываем обратно
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String[] entry : entries) {
                String line = String.join(";", entry);
                writer.write(line);
                writer.newLine();
            }
            LOGGER.info("Обновлена статистика побед для игрока: " + playerName);
        } catch (IOException e) {
            LOGGER.severe("Ошибка записи players.txt: " + e.getMessage());
        }
    }


    private boolean checkCastleCaptured(GameManager gmanager) {
        if (castleBot.isDestroyed()) {
            clearConsole();
            System.out.println("Игрок захватил вражеский замок!");
            LOGGER.info("Игрок победил: вражеский замок уничтожен");
            System.out.println("\n\n\n\n\n\n"+CYAN + "КОНЕЦ!" + RESET);
            addVictory(gmanager.getPlayerName());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return true;
        }
        if (castlePlayer.isDestroyed()) {
            clearConsole();
            System.out.println("\n\n\n\n\n\n"+CYAN + "КОНЕЦ!" + RESET);
            LOGGER.info("Компьютер победил: замок игрока уничтожен");
            System.out.println("Компьютер захватил ваш замок!");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return true;
        }
        return false;
    }

    private void clearConsole() {
        for (int i = 0; i < 50; i++) System.out.println();
    }
}
