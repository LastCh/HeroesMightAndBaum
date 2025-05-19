package game.interf;

import game.api.FieldObject;
import game.api.Position;
import game.map.Cell;
import game.map.Field;
import game.map.TerrainType;
import game.model.building.onmap.Castle;
import game.model.building.onmap.GoldCave;

import java.io.*;
import java.util.Scanner;

public class MapEditor {
    private static final Scanner scanner = new Scanner(System.in);

    public static Field editMap(int width, int height) {
        Field field = new Field(width, height);

        // Изначально заполняем травой
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                field.getCell(x, y).setTerrainType(TerrainType.GRASS);
            }
        }

        int castlesPlaced = 0;
        int cavesPlaced = 0;

        while (true) {
            System.out.println("\nРедактор карты:");
            System.out.println("1. Изменить тип местности");
            System.out.println("2. Разместить замок (макс. 2)");
            System.out.println("3. Разместить золотую пещеру (макс. 3)");
            System.out.println("4. Просмотр карты");
            System.out.println("5. Сохранить карту и выйти");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> setTerrain(field);
                case "2" -> {
                    if (castlesPlaced >= 2) {
                        System.out.println("❗ Уже размещено 2 замка.");
                        break;
                    }

                    Position pos = readPosition(width, height);
                    if (!field.getCell(pos.x(), pos.y()).isEmpty()) {
                        System.out.println("❗ Ячейка занята.");
                        break;
                    }

                    System.out.println("Это замок игрока или компьютера?");
                    System.out.println("1 — Игрок (синий)");
                    System.out.println("2 — Компьютер (красный)");
                    System.out.print("Ваш выбор: ");
                    String ownerInput = scanner.nextLine().trim();

                    String colorCode = "\u001B[34;47m";
                    switch (ownerInput) {
                        case "1" -> colorCode = "\u001B[34;47m"; // синий — игрок
                        case "2" -> colorCode = "\u001B[31;47m"; // красный — компьютер
                        default -> {
                            System.out.println("❌ Неверный выбор владельца замка.");
                            break;
                        }
                    }

                    field.getCell(pos.x(), pos.y()).addObject(new Castle(pos, colorCode, 2000, field));
                    castlesPlaced++;
                    System.out.println("✅ Замок размещён.");
                }

                case "3" -> {
                    if (cavesPlaced >= 3) {
                        System.out.println("❗ Уже размещено 3 пещеры.");
                        break;
                    }
                    Position pos = readPosition(width, height);
                    if (field.getCell(pos.x(), pos.y()).isEmpty()) {
                        field.getCell(pos.x(), pos.y()).addObject(new GoldCave(pos, 500));
                        cavesPlaced++;
                        System.out.println("✅ Пещера размещена.");
                    } else {
                        System.out.println("❗ Ячейка занята.");
                    }
                }
                case "4" -> field.render();
                case "5" -> {
                    System.out.print("Введите имя файла для сохранения: ");
                    String filename = scanner.nextLine().trim();
                    saveCustomMap(field, filename);
                    System.out.println("✅ Карта сохранена в " + filename);
                    return field;
                }
                default -> System.out.println("❌ Неверный ввод.");
            }
        }
    }

    private static void setTerrain(Field field) {
        Position pos = readPosition(field.getWidth(), field.getHeight());
        System.out.println("Выберите тип местности: GRASS, WATER, MOUNTAIN, FOREST");
        String input = scanner.nextLine().trim().toUpperCase();

        try {
            TerrainType type = TerrainType.valueOf(input);
            field.getCell(pos.x(), pos.y()).setTerrainType(type);
            System.out.println("✅ Тип местности изменён.");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Неизвестный тип местности.");
        }
    }

    private static Position readPosition(int width, int height) {
        int x = -1, y = -1;
        while (true) {
            try {
                System.out.print("Введите X (0-" + (width - 1) + "): ");
                x = Integer.parseInt(scanner.nextLine().trim());
                System.out.print("Введите Y (0-" + (height - 1) + "): ");
                y = Integer.parseInt(scanner.nextLine().trim());

                if (x >= 0 && x < width && y >= 0 && y < height) {
                    return new Position(x, y);
                }
                System.out.println("❌ Координаты вне диапазона.");
            } catch (NumberFormatException e) {
                System.out.println("❌ Введите целые числа.");
            }
        }
    }

    public static Field loadCustomMap(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String[] dims = reader.readLine().split(";");
            int width = Integer.parseInt(dims[0]);
            int height = Integer.parseInt(dims[1]);

            Field field = new Field(width, height);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    String cellData = reader.readLine();
                    // Передаём только field, т.к. остальные параметры (castlePlayer и т.п.) могут быть null
                    Cell cell = Cell.deserialize(cellData, field, null, null, null, null);
                    field.getCell(x, y).setTerrainType(cell.getTerrainType());
                    for (FieldObject obj : cell.getObjects()) {
                        field.getCell(x, y).addObject(obj);
                    }
                }
            }

            return field;
        } catch (IOException e) {
            System.out.println("❌ Ошибка чтения карты: " + e.getMessage());
            return null;
        }
    }

    public static void editExistingField(Field field) {
        int width = field.getWidth();
        int height = field.getHeight();

        while (true) {
            System.out.println("\nРедактор текущей карты:");
            System.out.println("1. Изменить тип местности");
            System.out.println("2. Просмотр карты");
            System.out.println("3. Выйти из редактора");
            System.out.println("4. Сохранить текущую карту в custom_maps");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> setTerrain(field);
                case "2" -> field.render();
                case "3" -> {
                    System.out.println("🚪 Выход из редактора карты.");
                    return;
                }
                case "4" -> {
                    System.out.print("Введите имя файла: ");
                    String filename = scanner.nextLine().trim();
                    saveCustomMap(field, filename);
                    System.out.println("✅ Карта сохранена в " + filename);
                }
                default -> System.out.println("❌ Неверный ввод.");
            }
        }
    }


    public static void saveCustomMap(Field field, String filename) {
        File dir = new File("custom_maps");
        if (!dir.exists()) dir.mkdirs();

        File file = new File(dir, filename + ".map");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(field.getWidth() + ";" + field.getHeight());
            writer.newLine();
            for (int y = 0; y < field.getHeight(); y++) {
                for (int x = 0; x < field.getWidth(); x++) {
                    Cell cell = field.getCell(x, y);
                    writer.write(cell.serialize());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Ошибка при сохранении карты: " + e.getMessage());
        }
    }
}
