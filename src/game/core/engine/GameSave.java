package game.core.engine;

import game.api.FieldObject;
import game.map.Cell;
import game.map.Field;
import game.model.building.onmap.Castle;
import game.model.hero.ComputerHero;
import game.model.hero.HumanHero;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
    }

    public static void saveGame(GameSave saveData, String slotName) {
        try {
            File dir = new File("saves");
            if (!dir.exists()) dir.mkdirs();

            File file = new File(dir, slotName + ".txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

                // 👉 1. Сохраняем имя сохранения и имя владельца
                writer.write(slotName + ";" + saveData.ownerName);
                writer.newLine();

                // 👉 2. Сохраняем размеры поля
                Field field = saveData.field;
                writer.write(field.getWidth() + ";" + field.getHeight());
                writer.newLine();

                // 👉 3. Сохраняем каждую ячейку поля
                for (int x = 0; x < field.getWidth(); x++) {
                    for (int y = 0; y < field.getHeight(); y++) {
                        Cell cell = field.getCell(x, y);
                        writer.write(cell.serialize());
                        writer.newLine();
                    }
                }

                // 👉 4. Сохраняем замки
                writer.write(saveData.castlePlayer.serialize());
                writer.newLine();
                writer.write(saveData.castleComputer.serialize());
                writer.newLine();

                // 👉 5. Сохраняем героев
                writer.write(saveData.humanHero.serialize());
                writer.newLine();
                writer.write(saveData.computerHero.serialize());
                writer.newLine();

                System.out.println("✅ Игра сохранена в слот " + slotName);
            }
        } catch (IOException e) {
            System.out.println("❌ Ошибка при сохранении: " + e.getMessage());
        }
    }


    public GameSave loadGame(String slotName) {
        try {
            File file = new File("saves", slotName + ".txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));

            // 1. Имя и владелец
            String[] meta = reader.readLine().split(";");
            String saveName = meta[0];
            String ownerName = meta[1];

            // 2. Размеры поля
            String[] dims = reader.readLine().split(";");
            int width = Integer.parseInt(dims[0]);
            int height = Integer.parseInt(dims[1]);

            // 3. Считываем все ячейки поля (width * height строк)
            List<String> cellLines = new ArrayList<>();
            for (int i = 0; i < width * height; i++) {
                cellLines.add(reader.readLine());
            }

            // 4. Чтение замков
            String castlePlayerData = reader.readLine();
            String castleComputerData = reader.readLine();

            // 5. Чтение героев
            String humanData = reader.readLine();
            String computerData = reader.readLine();

            // 6. Временное создание пустого поля
            field = new Field(width, height);

            // 7. Временное создание замков (с полем)
            castlePlayer = Castle.deserialize(castlePlayerData, field);
            castleComputer = Castle.deserialize(castleComputerData, field);


            // 8. Временное создание героев (с полем + замками)
            humanHero = HumanHero.deserialize(humanData, field, castlePlayer);
            computerHero = ComputerHero.deserialize(computerData, field, castleComputer);

            // 9. Заполняем клетки
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
                    field.getCell(x, y).getObjects().clear(); // очистка по-умолчанию
                    for (FieldObject obj : cell.getObjects()) {
                        field.getCell(x, y).addObject(obj);
                    }
                    field.getCell(x, y).setTerrainType(cell.getTerrainType());
                }
            }

            reader.close();
            System.out.println("✅ Игра загружена из слота " + slotName);

            return new GameSave(computerHero, humanHero, castlePlayer, castleComputer, field, saveName, ownerName);
        } catch (IOException e) {
            System.out.println("❌ Ошибка при загрузке: " + e.getMessage());
            return null;
        }
    }



}