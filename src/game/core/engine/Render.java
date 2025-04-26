package game.core.engine;

import game.api.Position;
import game.interf.MenuManager;
import game.map.Field;
import game.model.building.onmap.Castle;
import game.model.hero.ComputerHero;
import game.model.hero.Hero;
import game.model.hero.HumanHero;
import game.model.hero.PurchasableHero;

import java.util.List;
import java.util.Scanner;

public class Render {
    private final Field field;
    private final HumanHero player;
    private final ComputerHero computerPlayer;
    private final Scanner scanner; // Теперь передаётся извне
    private final MenuManager menu = new MenuManager();
    private final Castle castlePlayer;
    private final Castle castleBot;

    public Render(Field field, HumanHero player, Scanner sharedScanner,
                  ComputerHero computer, Castle cb, Castle cp) {
        this.field = field;
        this.player = player;
        this.scanner = sharedScanner;
        this.computerPlayer = computer;
        this.castleBot = cb;
        this.castlePlayer = cp;
        menu.setComputerPlayer(computer);
        menu.setHumanPlayer(player);

    }

    public void startGameLoop() {
        System.out.println("Для начала игры купите первое здание в свой замок и наймите юнитов," +
                " чтобы ваш герой смог передвигаться!");
        while (true) {
            renderField();
            menu.callGame();
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
                    player.resetMovementPoints();
                    if (computerPlayer.isAlive()) {
                        computerPlayer.makeMove(field); // Новый вызов хода ИИ
                        computerPlayer.resetMovementPoints();
                        computerPlayer.addGold(200);
                    }
                    player.addGold(200);
                    moveAllHeroes();
                    clearConsole();
                }
                case "M" -> {
                    clearConsole();
                    if (menu.showGameMenu()) {
                        return; // Выход в главное меню
                    }
                }
                case "X" -> {
                    System.out.println("\n");
                    if (!player.hasArtifact()) {
                        System.out.println("У вас нет артефакта для использования!");
                        break;
                    }
                    try {
                        System.out.println("Укажите координаты цели для артефакта:");
                        System.out.print("X: ");
                        int x = scanner.nextInt();
                        System.out.print("Y: ");
                        int y = scanner.nextInt();
                        scanner.nextLine(); // Очистка сканнера после чисел

                        Position position = new Position(x, y);
                        Hero enemy = field.getHeroAt(position);

                        if (enemy != null && enemy != player) {
                            if (player.useArtifact(enemy)) {
                                field.removePlayer(enemy);
                            }
                        } else {
                            System.out.println("На этих координатах нет подходящей цели.");
                        }
                    } catch (Exception e) {
                        System.out.println("Ошибка ввода! Пожалуйста, введите корректные координаты.");
                        scanner.nextLine(); // Очистка сканнера в случае ошибки
                    }
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
                default -> {
                    clearConsole();
                    System.out.println("Неверная команда.");
                }
            }

            if (checkCastleCaptured()) {
                System.out.println("Замок захвачен!");
                break;
            }

            if (!player.isAlive()) {
                System.out.println("Ваш герой погиб!");
                break;
            }

            if (!computerPlayer.isAlive()) {
                field.removePlayer(computerPlayer);
            }
        }
    }

    private void moveAllHeroes() {
        // Получаем список всех героев на поле
        List<PurchasableHero> heroes = field.getAllHeroes();

        // Делаем ход для каждого героя
        for (PurchasableHero hero : heroes) {
            if (hero.isAlive()) {
                hero.makeMove(field);  // Пусть каждый герой сделает свой ход
            }else{
                field.removePlayer(hero);
            }
        }
    }

    public void renderField() {
        field.render();
        menu.getGameMenu().display(player.getMovementPoints(), castlePlayer.getHealth(),
                castleBot.getHealth(), player.getGold(), player.getHealth(), player.getPower());
    }

    private boolean checkCastleCaptured() {
        if (castleBot == null || castlePlayer == null) return false;

        // Проверка захвата компьютерного замка игроком
        if (castleBot.isDestroyed()) {
            System.out.println("Игрок захватил вражеский замок!");
            return true;
        }

        // Проверка захвата игрового замка ботом
        if (castlePlayer.isDestroyed()) {
            System.out.println("Компьютер захватил ваш замок!");
            return true;
        }

        return false;
    }

    private void clearConsole() {
        for (int i = 0; i < 50; i++) System.out.println();
    }
}