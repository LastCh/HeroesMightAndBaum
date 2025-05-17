package game.interf;

import game.core.engine.GameManager;
import game.core.engine.GameSave;
import game.model.hero.ComputerHero;
import game.model.hero.HumanHero;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import static game.api.LogConfig.LOGGER;
import static game.interf.Inter.*;

public class MenuManager {
    private final StartMenu startMenu = new StartMenu();
    private final CastleMenu castleMenu = new CastleMenu();
    private final GameMenu gameMenu = new GameMenu();
    private GameManager gmanager;

    public void run() {
        Scanner scanner = new Scanner(System.in);

        String playerName = null;
        while (playerName == null || playerName.trim().isEmpty()) {
            System.out.print("Введите ваше имя: ");
            playerName = scanner.nextLine().trim();
            if (playerName.isEmpty()) {
                System.out.println("❗ Имя не может быть пустым. Попробуйте снова.");
                LOGGER.warning("Попытка ввода пустого имени.");
            }
        }

        LOGGER.info("Игрок ввел имя: " + playerName);
        System.out.println("Добро пожаловать, " + playerName + "!");
        this.gmanager = new GameManager(playerName);

        updatePlayerInfo(playerName, 0);

        while (true) {
            startMenu.display();
            int choice = startMenu.handleInput();

            switch (choice) {
                case 1 -> {
                    LOGGER.info("Пользователь начал новую игру.");
                    startNewGame();
                }
                case 2 ->{
                    LOGGER.info("Пользователь выбрал загрузку игры.");
                    loadGame(gmanager);
                }
                case 3 -> {
                    LOGGER.info("Пользователь выбрал таблицу лидеров.");
                    showLeaderboard();
                }
                case 4 -> {
                    LOGGER.info("Пользователь вышел из игры.");
                    System.out.println("Выход из игры...");
                    return;
                }
                default -> {
                    startMenu.clearConsole();
                    LOGGER.warning("Неверный выбор в главном меню: " + choice);
                    System.out.println(RED + "❌ Неверный выбор. Попробуйте снова." + RESET);
                }
            }
        }
    }

    public boolean showGameMenu(HumanHero hplayer, ComputerHero cplayer, GameManager gmanager) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            clearConsole();
            System.out.println("\n" +
                    GRADIENT_1 + "  ╔════════════════════════════╗\n" +
                    GRADIENT_2 + "  ║" + BOLD + "        🏰 Игровое меню     " + RESET + GRADIENT_2 + "║\n" +
                    GRADIENT_3 + "  ╠════════════════════════════╣\n" +
                    GRADIENT_1 + "  ║  " + YELLOW + "1. " + CYAN + "Управление замком      " + RESET + GRADIENT_1 + "║\n" +
                    GRADIENT_2 + "  ║  " + YELLOW + "2. " + CYAN + "Вернуться в игру       " + RESET + GRADIENT_2 + "║\n" +
                    GRADIENT_3 + "  ║  " + YELLOW + "3. " + CYAN + "Сохранить игру         " + RESET + GRADIENT_3 + "║\n" +
                    GRADIENT_1 + "  ║  " + YELLOW + "4. " + CYAN + "Главное меню           " + RESET + GRADIENT_1 + "║\n" +
                    GRADIENT_2 + "  ╚════════════════════════════╝\n" + RESET);

            System.out.print("Ваш выбор: ");
            String input = scanner.nextLine().trim();

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(RED + "❌ Введите целое число от 1 до 4." + RESET);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException en) {
                    Thread.currentThread().interrupt();
                }
                continue;
            }

            switch (choice) {
                case 1 -> {
                    clearConsole();
                    handleCastleMenu(hplayer);
                }
                case 2 -> {
                    clearConsole();
                    return false;
                }
                case 3 -> {
                    clearConsole();
                    saveGame(gmanager);
                }
                case 4 -> {
                    clearConsole();
                    return true;
                }
                default -> {
                    System.out.println(RED + "❌ Неверный выбор. Попробуйте снова." + RESET);
                }
            }
        }
    }


    public void clearConsole() {
        for (int i = 0; i < 50; i++) System.out.println();
    }

    private void saveGame(GameManager gmanager) {
        Scanner scanner = new Scanner(System.in);

        String saveName = null;
        while (saveName == null || saveName.trim().isEmpty()) {
            System.out.println("Введиет название сохранения:");
            saveName = scanner.nextLine().trim();
            if (saveName.isEmpty()) {
                System.out.println("❗ Имя не может быть пустым. Попробуйте снова.");
            }
        }

        GameSave.saveGame(gmanager.getGameSave(), saveName);
    }

    private void loadGame(GameManager gmanager) {
        // Получаем имя сохранения через меню выбора
        String saveName = loadGameN(gmanager.getPlayerName());
        if (saveName == null) {
            LOGGER.info("Пользователь отменил загрузку игры.");
            return;
        }

        GameSave loaded = new GameSave().loadGame(saveName);
        if (loaded == null) {
            System.out.println(RED + "❌ Не удалось загрузить игру." + RESET);
            return;
        }

        gmanager.setGameName(saveName); // Обновим имя игры в менеджере
        gmanager.loadGame(loaded);      // Передаём загруженный объект GameSave
        LOGGER.info("Игра успешно загружена из сохранения: " + saveName);
    }

    private void showLeaderboard() {
        clearConsole();
        File file = new File("players.txt");

        if (!file.exists()) {
            System.out.println(RED + "❌ Таблица лидеров недоступна: файл players.txt не найден." + RESET);
            return;
        }

        List<String[]> players = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 2) {
                    if (parts.length < 3) {
                        parts = new String[]{parts[0], parts[1], "0"};
                    }
                    players.add(parts);
                }
            }
        }
        catch (IOException e) {
            System.out.println(RED + "Ошибка чтения players.txt" + RESET);
            return;
        }

        players.sort((a, b) -> {
            int winsA = Integer.parseInt(a[2].trim());
            int winsB = Integer.parseInt(b[2].trim());

            double karmaA = Double.parseDouble(a[1].replace(',', '.'));
            double karmaB = Double.parseDouble(b[1].replace(',', '.'));

            if (winsA != winsB) return Integer.compare(winsB, winsA);
            return Double.compare(karmaB, karmaA);
        });


        System.out.println(GREEN + BOLD + "Таблица лидеров:" + RESET);
        System.out.println("-----------------------------------------");
        System.out.printf("%-15s %-10s %-10s%n", "Имя", "Карма", "Победы");
        System.out.println("-----------------------------------------");

        for (String[] p : players) {
            System.out.printf("%-15s %-10s %-10s%n", p[0], p[1], p[2]);
        }

        System.out.println("\nНажмите Enter для возврата...");
        new Scanner(System.in).nextLine();
    }


    private String loadGameN(String playerName) {
        clearConsole();
        File saveDir = new File("saves");
        if (!saveDir.exists() || !saveDir.isDirectory()) {
            System.out.println(RED + "❌ Папка сохранений не найдена." + RESET);
            return null;
        }

        File[] saveFiles = saveDir.listFiles((dir, name) -> name.endsWith(".txt"));
        if (saveFiles == null || saveFiles.length == 0) {
            System.out.println(YELLOW + "⚠️ Сохранений не найдено." + RESET);
            return null;
        }

        System.out.println(CYAN + "      Для закрытия меню нажмите " + BOLD + "Q" + RESET + GRADIENT_2);
        System.out.println("\n" +
                GRADIENT_1 + "  ╔════════════════════════════════╗\n" +
                GRADIENT_2 + "  ║" + BOLD + "         📂 Загрузить игру      " + RESET + GRADIENT_2 + "║\n" +
                GRADIENT_3 + "  ╠════════════════════════════════╣" + RESET);


        List<String> playerSaveNames = new ArrayList<>();

        for (File file : saveFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String firstLine = reader.readLine(); // Строка вида: saveName;ownerName
                if (firstLine == null) continue;

                String[] parts = firstLine.split(";");
                if (parts.length < 2) continue;

                String saveName = parts[0];
                String owner = parts[1];

                if (owner.equalsIgnoreCase(playerName)) {
                    playerSaveNames.add(saveName);
                }
            } catch (IOException e) {
                LOGGER.warning("Не удалось прочитать файл сохранения: " + file.getName());
            }
        }

        if (playerSaveNames.isEmpty()) {
            System.out.println(GRADIENT_1 + "  ║ " +YELLOW + "⚠️ У вас нет сохранений." + RESET+ GRADIENT_1 + "       ║");
            System.out.println(GRADIENT_3 + "  ╠════════════════════════════════╣" + RESET);
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String chosen = scanner.nextLine().trim();
                if (chosen.equalsIgnoreCase("Q")) return null;
            }
        }

        System.out.println(GRADIENT_1 + "  ║ " + CYAN + "Ваши сохранения:" + RESET + GRADIENT_1 + "                ║");
        for (String name : playerSaveNames) {
            System.out.println(GRADIENT_2 + "  ║   " + YELLOW + "- " + name + RESET +
                    GRADIENT_2 + " ".repeat(24 - name.length()) + "   ║");
        }

        System.out.println(GRADIENT_3 + "  ╠════════════════════════════════╣" + RESET);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print(BOLD + "  Введите имя сохранения: " + RESET);
            String chosen = scanner.nextLine().trim();

            if (chosen.equalsIgnoreCase("Q")) return null;

            if (playerSaveNames.contains(chosen)) {
                clearConsole();
                System.out.println(GREEN + "✅ Сохранение \"" + chosen + "\" выбрано." + RESET);
                System.out.println(GRADIENT_1 + "  ╚════════════════════════════════╝" + RESET);
                return chosen;
            }

            System.out.println(RED + "⚠️ Сохранение не найдено. Повторите ввод." + RESET);
        }
    }


    private void updatePlayerInfo(String name, double addedKarma) {
        File file = new File("players.txt");
        List<String[]> lines = new ArrayList<>();
        boolean updated = false;

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts[0].equalsIgnoreCase(name)) {
                        double karma = parts.length >= 2 ? Double.parseDouble(parts[1].replace(',', '.')) : 0;
                        int wins = parts.length >= 3 ? Integer.parseInt(parts[2].replace(',', '.')) : 0;
                        karma += addedKarma;
                        lines.add(new String[]{name, String.format(Locale.US, "%.2f", karma), String.valueOf(wins)});

                        updated = true;
                    } else {
                        lines.add(parts);
                    }
                }
            } catch (IOException | NumberFormatException e) {
                LOGGER.severe("Ошибка чтения players.txt: " + e.getMessage());
            }
        }

        if (!updated) {
            lines.add(new String[]{name, String.format("%.2f", addedKarma), "0"});
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String[] line : lines) {
                writer.write(String.join(";", line));
                writer.newLine();
            }
        } catch (IOException e) {
            LOGGER.severe("Ошибка записи в players.txt: " + e.getMessage());
        }

        LOGGER.info("Игрок " + name + " успешно обновлён или добавлен.");
    }

    private void handleCastleMenu(HumanHero player) {
        int result;
        do {
            castleMenu.display();
            result = castleMenu.handleInput(player);
        } while (result != 4);
    }

    private void startNewGame() {
        System.out.println("Начинаем новую игру...");
        gmanager.startGame();
    }

    public GameMenu getGameMenu() {
        return gameMenu;
    }
}
