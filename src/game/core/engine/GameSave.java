package game.core.engine;

import game.api.FieldObject;
import game.map.Cell;
import game.map.Field;
import game.model.building.onmap.Castle;
import game.model.hero.ComputerHero;
import game.model.hero.HumanHero;
import game.model.hero.PurchasableHero;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static game.api.LogConfig.LOGGER;

public class GameSave {
    public ComputerHero computerHero;
    public HumanHero humanHero;
    public Castle castlePlayer;
    public Castle castleComputer;
    public String saveName;
    public Field field;
    public String ownerName;


    public GameSave() {}

    public GameSave(ComputerHero cHero, HumanHero hHero, Castle cPlayer, Castle cComp, Field oField, String name, String pOwnerName) {
        this.computerHero = cHero;
        this.humanHero = hHero;
        this.castlePlayer = cPlayer;
        this.castleComputer = cComp;
        this.field = oField;
        this.ownerName = pOwnerName;
        this.saveName = name;
    }

    public static void saveGame(GameSave saveData, String slotName) {
        try {
            File dir = new File("saves");
            if (!dir.exists() && !dir.mkdirs()) {
                LOGGER.severe("Не удалось создать директорию для сохранений: " + dir.getAbsolutePath());
                throw new IOException("Путь к файлу пуст");
            }

            File file = new File(dir, slotName + ".txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

                writer.write(slotName + ";" + saveData.ownerName);
                writer.newLine();
                LOGGER.info("Сохранены название сохранения и имя владельца");

                Field field = saveData.field;
                writer.write(field.getWidth() + ";" + field.getHeight());
                writer.newLine();
                LOGGER.info("Сохранены размеры поля");

                for (int x = 0; x < field.getWidth(); x++) {
                    for (int y = 0; y < field.getHeight(); y++) {
                        Cell cell = field.getCell(x, y);
                        writer.write(cell.serialize());
                        writer.newLine();
                    }
                }
                LOGGER.info("Сохранены все ячейки поля");

                writer.write(saveData.castlePlayer.serialize());
                writer.newLine();
                writer.write(saveData.castleComputer.serialize());
                writer.newLine();
                LOGGER.info("Замки сохранены");

                KarmaManager.updatePlayerKarma(saveData.ownerName, saveData.humanHero.getKarma());
                saveData.humanHero.resetKarma();

                writer.write(saveData.humanHero.serialize());
                writer.newLine();
                writer.write(saveData.computerHero.serialize());
                writer.newLine();
                LOGGER.info("Герои сохранены");

                System.out.println("✅ Игра сохранена в слот " + slotName);
            }
        } catch (IOException e) {
            LOGGER.severe("Ошибка при сохранении: " + e.getMessage());
            System.out.println("❌ Ошибка при сохранении: " + e.getMessage());
        }
    }

    public GameSave loadGame(String slotName) {
        try {
            File file = new File("saves", slotName + ".txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String[] meta = reader.readLine().split(";");
            String saveName = meta[0];
            String ownerName = meta[1];
            LOGGER.info("Загружено название сохранения и владелец");

            String[] dims = reader.readLine().split(";");
            int width = Integer.parseInt(dims[0]);
            int height = Integer.parseInt(dims[1]);
            LOGGER.info("Загружен размер поля");

            List<String> cellLines = new ArrayList<>();
            for (int i = 0; i < width * height; i++) {
                cellLines.add(reader.readLine());
            }
            LOGGER.info("Загружен все ячейки игрового поля");

            String castlePlayerData = reader.readLine();
            String castleComputerData = reader.readLine();
            LOGGER.info("Загружены замки");

            String humanData = reader.readLine();
            String computerData = reader.readLine();
            LOGGER.info("Загружены герои");

            field = new Field(width, height);
            LOGGER.info("Создано поле");

            castlePlayer = Castle.deserialize(castlePlayerData, field);
            castleComputer = Castle.deserialize(castleComputerData, field);
            LOGGER.info("Созданы замки");

            humanHero = HumanHero.deserialize(humanData, field, castlePlayer);
            computerHero = ComputerHero.deserialize(computerData, field, castleComputer);
            LOGGER.info("Созданы герои");

            int index = 0;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Cell cell = Cell.deserialize(
                            cellLines.get(index++),
                            field,
                            castlePlayer,
                            castleComputer,
                            computerHero,
                            humanHero
                    );
                    field.getCell(x, y).getObjects().clear();
                    for (FieldObject obj : cell.getObjects()) {
                        field.getCell(x, y).addObject(obj);
                    }
                    field.getCell(x, y).setTerrainType(cell.getTerrainType());
                }
            }
            LOGGER.info("Все клетки поля заполнены");

            field.getCell(castlePlayer.getPosition().x(), castlePlayer.getPosition().y()).addObject(castlePlayer);
            field.getCell(castleComputer.getPosition().x(), castleComputer.getPosition().y()).addObject(castleComputer);

            reader.close();
            System.out.println("✅ Игра загружена из слота " + slotName);

            double karma = KarmaManager.loadPlayerKarma(ownerName);
            computerHero.addBenefit(karma);

            addPurchasableHeroesToAll(field);
            LOGGER.info("Все герои учтены");

            return new GameSave(computerHero, humanHero, castlePlayer, castleComputer, field, saveName, ownerName);
        } catch (IOException e) {
            LOGGER.severe("Ошибка при загрузке: " + e.getMessage());
            System.out.println("❌ Ошибка при загрузке: " + e.getMessage());
            return null;
        }
    }

    private void addPurchasableHeroesToAll(Field field) {
        field.getAllHeroes().clear();
        for (int x = 0; x < field.getWidth(); x++) {
            for (int y = 0; y < field.getHeight(); y++) {
                field.getCell(x, y).getObjects().stream()
                        .filter(o -> o instanceof PurchasableHero)
                        .map(o -> (PurchasableHero)o)
                        .forEach(field::addHeroToAll);
            }
        }
    }

}