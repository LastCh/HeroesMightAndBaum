package game.core.engine;

import game.api.FieldObject;
import game.api.Position;
import game.map.Cell;
import game.map.Field;
import game.map.TerrainType;
import game.model.building.onmap.*;

import java.io.*;
import java.util.Scanner;

public class MapEditor {
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Редактор новой карты. Позволяет:
     * - Изменять тип местности
     * - Размещать ровно 2 замка
     * - Размещать до 3 золотых пещер
     * - Размещать парикмахерские, отели и рестораны
     * - Просматривать карту и сохранять её
     */
    public static Field editMap(int width, int height) {
        Field field = new Field(width, height);
        // Залить травой
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                field.getCell(x, y).setTerrainType(TerrainType.GRASS);
            }
        }
        return editField(field, true);
    }

    /**
     * Редактирование уже существующего игрового поля: не заливает травой.
     */
    public static void editExistingField(Field field) {
        editField(field, false);
    }


    private static Field editField(Field field, boolean isNew) {
        int width = field.getWidth();
        int height = field.getHeight();
        int castlesPlaced = (int) countObjects(field, Castle.class);
        int cavesPlaced = (int) countObjects(field, GoldCave.class);

        while (true) {
            System.out.println("\nРедактор карты:");
            System.out.println("1. Изменить тип местности");
            System.out.println("2. Разместить замок (макс. 2)");
            System.out.println("3. Разместить золотую пещеру (макс. 3)");
            System.out.println("4. Разместить парикмахерскую");
            System.out.println("5. Разместить отель");
            System.out.println("6. Разместить ресторан");
            System.out.println("7. Просмотр карты");
            System.out.println(isNew ? "8. Сохранить карту и выйти" : "8. Выйти из редактора");
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
                    Cell cell = field.getCell(pos.x(), pos.y());
                    if (!cell.getObjects().isEmpty()) {
                        System.out.println("❗ Ячейка занята.");
                        break;
                    }

                    // Выбор владельца замка
                    System.out.println("Это замок игрока или компьютера?");
                    System.out.println("1 — Игрок (синий)");
                    System.out.println("2 — Компьютер (красный)");
                    System.out.print("Ваш выбор: ");
                    String ownerInput = scanner.nextLine().trim();

                    String colorCode = null;
                    String ownerType = null;
                    switch (ownerInput) {
                        case "1" -> {
                            colorCode = "34;47";
                            ownerType = "player";
                        }
                        case "2" -> {
                            colorCode = "31;47";
                            ownerType = "computer";
                        }
                        default -> {
                            System.out.println("❌ Неверный ввод владельца.");
                            break;
                        }
                    }
                    if (colorCode == null) break;

                    // Проверяем, нет ли уже замка этого владельца
                    boolean exists = false;
                    for (int xx = 0; xx < width; xx++) {
                        for (int yy = 0; yy < height; yy++) {
                            for (FieldObject o : field.getCell(xx, yy).getObjects()) {
                                if (o instanceof Castle c) {
                                    String sym = c.getColoredSymbol();
                                    if (ownerType.equals("player") && sym.startsWith("\u001B[34;47m")) exists = true;
                                    if (ownerType.equals("computer") && sym.startsWith("\u001B[31;47m")) exists = true;
                                }
                            }
                        }
                    }
                    if (exists) {
                        System.out.println("❗ Замок данного владельца уже размещён.");
                        break;
                    }

                    // Создаём замок
                    cell.addObject(new Castle(pos, colorCode, 2000, field));
                    castlesPlaced++;
                    System.out.println("✅ Замок размещён.");
                }

                case "3" -> {
                    if (cavesPlaced >= 3) {
                        System.out.println("❗ Уже размещено 3 пещеры."); break;
                    }
                    Position pos = readPosition(width, height);
                    Cell cell = field.getCell(pos.x(), pos.y());
                    if (!cell.getObjects().isEmpty()) {
                        System.out.println("❗ Ячейка занята."); break;
                    }
                    cell.addObject(new GoldCave(pos, 500));
                    cavesPlaced++;
                    System.out.println("✅ Пещера размещена.");
                }

                case "4" -> placeEnterprise(field, Barbershop.class, "парикмахерскую");

                case "5" -> placeEnterprise(field, Hotel.class, "отель");

                case "6" -> placeEnterprise(field, Restaurant.class, "ресторан");

                case "7" -> field.render();

                case "8" -> {
                    if (isNew) {
                        if (castlesPlaced != 2) {
                            System.out.println("❗ Для сохранения требуется ровно 2 замка, сейчас: " + castlesPlaced);
                            break;
                        }
                        System.out.print("Введите имя файла для сохранения: ");
                        String filename = scanner.nextLine().trim();
                        saveCustomMap(field, filename);
                        System.out.println("✅ Сохранено: custom_maps/" + filename + ".map");
                        return field;
                    } else {
                        return field;
                    }
                }
                default -> System.out.println("❌ Неверный ввод.");
            }
        }
    }

    private static <T> long countObjects(Field field, Class<T> cls) {
        long count = 0;
        for (int x = 0; x < field.getWidth(); x++) {
            for (int y = 0; y < field.getHeight(); y++) {
                for (FieldObject obj : field.getCell(x, y).getObjects()) {
                    if (cls.isInstance(obj)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private static void placeEnterprise(Field field, Class<? extends FieldObject> cls, String name) {
        Position pos = readPosition(field.getWidth(), field.getHeight());
        Cell cell = field.getCell(pos.x(), pos.y());
        if (cell.getObjects().stream().anyMatch(o -> o instanceof Castle)) {
            System.out.println("❗ Нельзя ставить предприятие на клетку с замком."); return;
        }
        if (cell.getObjects().stream().anyMatch(o -> o instanceof Barbershop || o instanceof Hotel || o instanceof Restaurant)) {
            System.out.println("❗ Предприятие уже есть."); return;
        }
        try {
            FieldObject ent = cls.getDeclaredConstructor(Position.class, Field.class, String.class)
                    .newInstance(pos, field, cell.getTerrainType().getColoredBackground());
            cell.addObject(ent);
            System.out.println("✅ " + name + " размещено.");
        } catch (Exception e) {
            System.out.println("❌ Ошибка при размещении " + name + ": " + e.getMessage());
        }
    }

    private static void setTerrain(Field field) {
        System.out.print("Введите X Y TYPE (например: 3 5 FOREST): ");
        try {
            String[] p = scanner.nextLine().trim().split("\\s+");
            int x = Integer.parseInt(p[0]), y = Integer.parseInt(p[1]);
            TerrainType t = TerrainType.valueOf(p[2].toUpperCase());
            field.getCell(x, y).setTerrainType(t);
            System.out.println("✅ Тип местности изменён.");
        } catch (Exception e) {
            System.out.println("❌ Ошибка ввода.");
        }
    }

    private static Position readPosition(int w, int h) {
        while (true) {
            try {
                System.out.print("X (0-" + (w-1) + "): ");
                int x = Integer.parseInt(scanner.nextLine().trim());
                System.out.print("Y (0-" + (h-1) + "): ");
                int y = Integer.parseInt(scanner.nextLine().trim());
                if (x<0||x>=w||y<0||y>=h) throw new Exception();
                return new Position(x,y);
            } catch (Exception e) {
                System.out.println("❌ Некорректные координаты.");
            }
        }
    }

    public static void saveCustomMap(Field field, String filename) {
        File dir = new File("custom_maps");
        if (!dir.exists()) dir.mkdirs();
        File file = new File(dir, filename + ".map");

        try (BufferedWriter w = new BufferedWriter(new FileWriter(file))) {
            // Заголовок
            w.write(field.getWidth() + ";" + field.getHeight());
            w.newLine();

            // Для каждой ячейки вручную собираем строку с фильтрацией объектов
            for (int y = 0; y < field.getHeight(); y++) {
                for (int x = 0; x < field.getWidth(); x++) {
                    Cell cell = field.getCell(x, y);
                    StringBuilder sb = new StringBuilder();

                    // Всегда сохраняем тип местности
                    sb.append(cell.getTerrainType().name());

                    // Только «карточные» объекты
                    for (FieldObject obj : cell.getObjects()) {
                        if (obj instanceof Castle
                                || obj instanceof GoldCave
                                || obj instanceof Barbershop
                                || obj instanceof Hotel
                                || obj instanceof Restaurant) {
                            sb.append("|")
                                    .append(obj.getClassName())
                                    .append("#")
                                    .append(obj.serialize());
                        }
                    }

                    w.write(sb.toString());
                    w.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Ошибка сохранения: " + e.getMessage());
        }
    }


    public static Field loadCustomMap() {
        File dir = new File("custom_maps");
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("❌ Папка custom_maps не найдена.");
            return null;
        }

        File[] files = dir.listFiles((d, n) -> n.toLowerCase().endsWith(".map"));
        if (files == null || files.length == 0) {
            System.out.println("❌ Нет доступных карт.");
            return null;
        }

        System.out.println("Выберите карту (номер или имя без \".map\"):");
        for (int i = 0; i < files.length; i++) {
            String base = files[i].getName().replaceFirst("(?i)\\.map$", "");
            System.out.printf("%d. %s%n", i + 1, base);
        }
        System.out.print("Ваш выбор: ");
        String input = scanner.nextLine().trim();

        File selected = null;
        try {
            int idx = Integer.parseInt(input) - 1;
            if (idx >= 0 && idx < files.length) {
                selected = files[idx];
            }
        } catch (NumberFormatException ignored) { }
        if (selected == null) {
            String target = input + ".map";
            for (File f : files) {
                if (f.getName().equalsIgnoreCase(target)) {
                    selected = f;
                    break;
                }
            }
        }
        if (selected == null) {
            System.out.println("❌ Карта не найдена.");
            return null;
        }

        try (BufferedReader r = new BufferedReader(new FileReader(selected))) {
            String hdr = r.readLine();
            if (hdr == null || !hdr.matches("\\d+;\\d+")) {
                System.out.println("❌ Неверный формат заголовка в " + selected.getName());
                return null;
            }
            String[] dims = hdr.split(";");
            int width  = Integer.parseInt(dims[0]);
            int height = Integer.parseInt(dims[1]);

            Field field = new Field(width, height);
            int blueCount = 0, redCount = 0;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    String line = r.readLine();
                    if (line == null) {
                        System.out.println("❌ Недостаточно строк в файле.");
                        return null;
                    }

                    // разделяем по '|'
                    String[] parts = line.split("\\|");
                    // 1) terrain
                    TerrainType terrain = TerrainType.valueOf(parts[0]);
                    Cell cell = field.getCell(x, y);
                    cell.setTerrainType(terrain);

                    // 2) объекты
                    for (int i = 1; i < parts.length; i++) {
                        String[] objParts = parts[i].split("#", 2);
                        String type = objParts[0].trim();
                        String objData = objParts[1];

                        switch (type) {
                            case "Castle" -> {
                                Castle c = Castle.deserialize(objData, field);
                                cell.addObject(c);
                                if (c.isPlayerCastle()) blueCount++;
                                else if (c.isComputerCastle()) redCount++;
                            }
                            case "GoldCave" -> {
                                cell.addObject(GoldCave.deserialize(objData));
                            }
                            case "Barbershop" -> {
                                cell.addObject(Barbershop.deserialize(objData, field));
                            }
                            case "Hotel" -> {
                                cell.addObject(Hotel.deserialize(objData, field));
                            }
                            case "Restaurant" -> {
                                cell.addObject(Restaurant.deserialize(objData, field));
                            }
                            default -> {
                                // если вдруг встретился неожиданный тип — сообщаем в лог
                                System.out.println("⚠️  Неизвестный объект в карте: \"" + type + "\"");
                            }
                        }
                    }
                }
            }

            System.out.printf("Найдено замков: синих=%d, красных=%d%n", blueCount, redCount);
            if (blueCount != 1 || redCount != 1) {
                System.out.println("❌ В карте должно быть по одному замку игрока (синий) и компьютера (красный).");
                return null;
            }
            return field;

        } catch (IOException e) {
            System.out.println("❌ Ошибка чтения карты: " + e.getMessage());
            return null;
        }
    }



}
