package game.core.engine;

import game.api.Position;
import game.map.Cell;
import game.map.Field;
import game.map.TerrainType;
import game.model.building.onmap.*;
import game.model.hero.HumanHero;
import game.model.hero.ComputerHero;

import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static game.api.LogConfig.LOGGER;
import static game.api.LogConfig.NPC_LOGGER;

public class GameManager {
    private final ScheduledExecutorService circusSpawn = Executors.newSingleThreadScheduledExecutor();
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
            botCastle = new Castle(new Position(8, 8), "31;47", 2000, field);
            playerCastle = new Castle(new Position(1, 1),  "34;47", 2000, field);
        } catch (Exception e) {
            LOGGER.severe("Ошибка при создании замков: " + e.getMessage());
        }

        if (botCastle != null) field.getCell(botCastle.getPosition().x(),botCastle.getPosition().y()).addObject(botCastle);
        if (playerCastle != null) field.getCell(playerCastle.getPosition().x(),playerCastle.getPosition().y()).addObject(playerCastle);

        if (botCastle == null || playerCastle == null) {
            LOGGER.warning("Один из замков не инициализирован!");
        }

        human = new HumanHero(new Position(0, 0), 10, playerCastle, 1000);
        computer = new ComputerHero(new Position(9, 9), playerCastle.getPosition(), 2, botCastle, 500);
        human.initAllKarma(playerName);

        field.getCell(human.getPosition().x(),human.getPosition().y()).addObject(human);
        field.getCell(computer.getPosition().x(),computer.getPosition().y()).addObject(computer);

        double karma = KarmaManager.loadPlayerKarma(playerName);
        computer.addBenefit(karma);
        LOGGER.info("Эффект от дурной славы успешно передан противнику");

        for (int i = 0; i < ThreadLocalRandom.current().nextInt(2, 5); i++) {
            int x, y;
            do {
                x = (int) (Math.random() * field.getWidth());
                y = (int) (Math.random() * field.getHeight());
            } while (!field.getCell(x, y).isEmpty());
            GoldCave cave = new GoldCave(new Position(x, y), 500 + i * 100);
            field.getCell(x, y).addObject(cave);
        }

        int buildingsToPlace = ThreadLocalRandom.current().nextInt(3, 7);
        int attemptsLimit = 100;

        while (buildingsToPlace > 0 && attemptsLimit-- > 0) {

            int x = ThreadLocalRandom.current().nextInt(field.getWidth());
            int y = ThreadLocalRandom.current().nextInt(field.getHeight());

            if (!field.getCell(x, y).isEmpty()) continue;

            Enterprise ent = null;
            int typeAttempts = 5;

            while (ent == null && typeAttempts-- > 0) {
                int kind = ThreadLocalRandom.current().nextInt(3);   // 0..2

                switch (kind) {
                    case 0 -> ent = new Barbershop(
                            new Position(x, y), field,
                            field.getCell(x, y).getTerrainType().getColoredBackground());

                    case 1 -> ent = new Restaurant(
                            new Position(x, y), field,
                            field.getCell(x, y).getTerrainType().getColoredBackground());

                    case 2 -> {
                        if (hasMountainNeighbour(field, x, y)) {
                            ent = new Hotel(
                                    new Position(x, y), field,
                                    field.getCell(x, y).getTerrainType().getColoredBackground());
                        }
                    }
                }
            }

            if (ent != null) {
                field.getCell(x, y).addObject(ent);
                buildingsToPlace--;
            }
        }

        gameSave = new GameSave(computer, human, playerCastle, botCastle, field, gameName, playerName);

        LOGGER.info("Запуск игрового цикла");
        Render render = new Render(field, human, scanner, computer, playerCastle, botCastle);
        clearConsole();
        startCircusSpawner();
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
        human.initAllKarma(playerName);

        gameSave = new GameSave(computer, human, playerCastle, botCastle, field, gameName, playerName);

        Render render = new Render(field, human, scanner, computer, playerCastle, botCastle);
        clearConsole();
        startCircusSpawner();
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

        double karma = KarmaManager.loadPlayerKarma(playerName);
        computer.addBenefit(karma);
        LOGGER.info("Карма успешно передана противнику");

        field.getCell(human.getPosition().x(), human.getPosition().y()).addObject(human);
        field.getCell(computer.getPosition().x(), computer.getPosition().y()).addObject(computer);

        this.gameSave = new GameSave(computer, human, playerCastle, botCastle, field, gameName, playerName);

        System.out.println("✅ Игра загружена с пользовательской карты.");
        Render render = new Render(field, human, scanner, computer, playerCastle, botCastle);
        clearConsole();
        startCircusSpawner();
        render.startGameLoop(this);
    }

    private static boolean hasMountainNeighbour(Field f, int cx, int cy) {
        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, -1, 0, 1};

        for (int i = 0; i < 4; i++) {
            int nx = cx + dx[i], ny = cy + dy[i];
            Cell n = f.getCell(nx, ny);
            if (n != null && n.getTerrainType() == TerrainType.MOUNTAIN)
                return true;
        }
        return false;
    }

    public void clearConsole() {
        for (int i = 0; i < 50; i++) System.out.println();
    }

    private void trySpawnCircus() {
        if (field == null) return;

        field.findObjects(WanderingCircus.class).removeIf(circus -> {
            if (circus.isExpired()) {
                field.getCell(circus.getPosition().x(), circus.getPosition().y())
                        .removeObject(circus);
                NPC_LOGGER.info("[CIRCUS] исчез в " + circus.getPosition());
                return true;
            }
            return false;
        });

        NPC_LOGGER.info("[CIRCUS-TICK]");

        if (field.findObjects(WanderingCircus.class).size() >= 2) return;
        if (ThreadLocalRandom.current().nextDouble() >= 0.15)      return;

        field.randomFreeCell().ifPresent(pos -> {
            WanderingCircus c = new WanderingCircus(pos, field);
            field.getCell(pos.x(), pos.y()).addObject(c);
            NPC_LOGGER.info("[CIRCUS] появился в " + pos);
        });
    }


    private void startCircusSpawner() {
        // повторно не запускаем
        if (!circusSpawn.isShutdown())
            circusSpawn.scheduleAtFixedRate(this::trySpawnCircus,
                    0, 10, TimeUnit.SECONDS);
    }

    public GameSave getGameSave() { return gameSave; }

    public String getPlayerName() { return playerName; }

    public void setGameName(String gName) { this.gameName = gName; }

    public void setCustomField(Field customField) { this.customField = customField; }

}
