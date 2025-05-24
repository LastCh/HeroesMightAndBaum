package game.core.engine;

import game.api.Position;
import game.map.Field;
import game.model.building.onmap.Castle;
import game.model.building.onmap.GoldCave;
import game.model.hero.HumanHero;
import game.model.hero.ComputerHero;

import java.util.Scanner;

import static game.api.LogConfig.LOGGER;

public class GameManager {
    private Field field;
    private HumanHero human;
    private ComputerHero computer;
    private final Scanner scanner = new Scanner(System.in);
    private Castle botCastle;
    private Castle playerCastle;
    private GameSave gameSave;
    private final String playerName;
    private String gameName = "name";
    private Field customField = null;

    public GameManager(String playerN){
        playerName = playerN;
    }

    public void startGame() {
        LOGGER.info("Начало генерации карты");
        Generation generation = new Generation();
        field = generation.generateField(10, 10);
        LOGGER.info("Карта сгенерирована");

        try {
            botCastle = new Castle(new Position(8, 8), "\u001B[31;47m", 2000, field);
            playerCastle = new Castle(new Position(1, 1),  "\u001B[34;47m", 2000, field);
        } catch (Exception e) {
            LOGGER.severe("Ошибка при создании замков: " + e.getMessage());
        }

        field.getCell(8, 8).addObject(botCastle);
        field.getCell(1, 1).addObject(playerCastle);

        if (botCastle == null || playerCastle == null) {
            LOGGER.warning("Один из замков не инициализирован!");
        }

        human = new HumanHero(new Position(0, 0), 10, playerCastle, 1000);
        computer = new ComputerHero(new Position(9, 9), playerCastle.getPosition(), 2, botCastle, 500);
        field.getCell(0, 0).addObject(human);
        field.getCell(9, 9).addObject(computer);

        GameSave gameSave1 = new GameSave();
        double karma = gameSave1.loadPlayerKarma(playerName);
        computer.addBenefit(karma);
        LOGGER.info("Карма успешно передана противнику");

        for (int i = 0; i < 3; i++) {
            int x, y;
            do {
                x = (int) (Math.random() * field.getWidth());
                y = (int) (Math.random() * field.getHeight());
            } while (!field.getCell(x, y).isEmpty());
            GoldCave cave = new GoldCave(new Position(x, y), 500 + i * 100);
            field.getCell(x, y).addObject(cave);
        }

        gameSave = new GameSave(computer, human, playerCastle, botCastle, field, gameName, playerName);

        LOGGER.info("Запуск игрового цикла");
        Render render = new Render(field, human, scanner, computer, playerCastle, botCastle);
        clearConsole();
        render.startGameLoop(this);
    }

    public void loadGame(GameSave gSave) {
        field = gSave.field;

        botCastle = gSave.castleComputer;
        botCastle.setColoredSymbol("\u001B[31;47m"+"♜♜" + "\u001B[0m");
        playerCastle = gSave.castlePlayer;
        playerCastle.setColoredSymbol("\u001B[34;47m"+"♜♜" + "\u001B[0m");

        human = gSave.humanHero;
        computer = gSave.computerHero;

        gameSave = new GameSave(computer, human, playerCastle, botCastle, field, gameName, playerName);

        Render render = new Render(field, human, scanner, computer, playerCastle, botCastle);
        clearConsole();
        render.startGameLoop(this);
    }

    public void startGameFromCustomMap() {
        if (customField == null) {
            System.out.println("❌ Кастомное поле не задано.");
            return;
        }

        this.field = customField;

        // Ищем замки и героев
        Castle firstCastle = null, secondCastle = null;
        for (int x = 0; x < field.getWidth(); x++) {
            for (int y = 0; y < field.getHeight(); y++) {
                for (var obj : field.getCell(x, y).getObjects()) {
                    if (obj instanceof Castle castle) {
                        if (firstCastle == null) {
                            firstCastle = castle;
                        } else {
                            secondCastle = castle;
                        }
                    }
                }
            }
        }

        if (firstCastle == null || secondCastle == null) {
            System.out.println("❌ Не хватает замков на карте.");
            return;
        }

        this.playerCastle = firstCastle;
        this.botCastle = secondCastle;

        this.human = new HumanHero(firstCastle.getPosition(), 10, firstCastle, 1000);
        this.computer = new ComputerHero(secondCastle.getPosition(), firstCastle.getPosition(), 2, secondCastle, 500);

        GameSave gameSave1 = new GameSave();
        double karma = gameSave1.loadPlayerKarma(playerName);
        computer.addBenefit(karma);
        LOGGER.info("Карма успешно передана противнику");

        field.getCell(firstCastle.getPosition().x(),firstCastle.getPosition().y()).addObject(human);
        field.getCell(firstCastle.getPosition().x(),firstCastle.getPosition().y()).addObject(computer);

        this.gameSave = new GameSave(computer, human, playerCastle, botCastle, field, gameName, playerName);

        System.out.println("✅ Игра загружена с пользовательской карты.");
        Render render = new Render(field, human, scanner, computer, playerCastle, botCastle);
        clearConsole();
        render.startGameLoop(this);
    }


    public void clearConsole() {
        for (int i = 0; i < 50; i++) System.out.println();
    }

    public GameSave getGameSave() { return gameSave; }

    public String getPlayerName() { return playerName; }

    public void setGameName(String gName) { this.gameName = gName; }

    public void setCustomField(Field customField) { this.customField = customField; }

}
