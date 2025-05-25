package game.interf;

import game.core.engine.*;
import game.map.Field;
import game.model.hero.ComputerHero;
import game.model.hero.HumanHero;

import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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
    private final Scanner scanner = new Scanner(System.in);

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

        KarmaManager.updatePlayerKarma(playerName, 0);

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
                    Field customField = MapEditor.editMap(10, 10);
                    gmanager.setCustomField(customField);
                }
                case 5 -> {
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
                    GRADIENT_2 + "  ║  " + YELLOW + "4. " + CYAN + "Редактор карты         " + RESET + GRADIENT_2 + "║\n" +
                    GRADIENT_1 + "  ║  " + YELLOW + "5. " + CYAN + "Главное меню           " + RESET + GRADIENT_1 + "║\n" +
                    GRADIENT_2 + "  ╚════════════════════════════╝\n" + RESET);

            System.out.print("Ваш выбор: ");
            String input = scanner.nextLine().trim();

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(RED + "❌ Введите целое число от 1 до 5." + RESET);
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
                    MapEditor.editExistingField(gmanager.getGameSave().field); // редактируем текущее поле
                    System.out.println(GREEN + "✅ Изменения карты сохранены." + RESET);
                }
                case 5 -> {
                    clearConsole();
                    saveGameAuto(gmanager);
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
        String saveName = null;
        while (saveName == null || saveName.trim().isEmpty()) {
            System.out.println("Введите название сохранения:");
            saveName = scanner.nextLine().trim();
            if (saveName.isEmpty()) {
                System.out.println("❗ Имя не может быть пустым. Попробуйте снова.");
            }
        }

        GameSave.saveGame(gmanager.getGameSave(), saveName);
    }

    private void saveGameAuto(GameManager gmanager) {
        String saveName = "autoSave"+gmanager.getPlayerName();
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

        List<VictoryManager.PlayerStats> players = VictoryManager.loadAllStats();
        if (players.isEmpty()) {
            System.out.println(RED + "❌ Таблица лидеров недоступна: нет данных." + RESET);
            return;
        }

        System.out.println(GREEN + BOLD + "Таблица лидеров:" + RESET);
        System.out.println("-----------------------------------------");
        System.out.printf("%-15s %-10s %-10s%n", "Имя", "Карма", "Победы");
        System.out.println("-----------------------------------------");

        // используем тот же формат, что и в KarmaManager
        DecimalFormat karmaFmt = new DecimalFormat("0.#",
                DecimalFormatSymbols.getInstance(Locale.ENGLISH));

        for (VictoryManager.PlayerStats p : players) {
            String karmaStr = karmaFmt.format(p.getKarma());
            System.out.printf("%-15s %-10s %-10d%n",
                    p.getName(), karmaStr, p.getWins());
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

        System.out.println(GRADIENT_1 + "  ║ " + CYAN + "Ваши сохранения:" + RESET + GRADIENT_1 + "               ║");
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


    private void handleCastleMenu(HumanHero player) {
        int result;
        do {
            castleMenu.display();
            result = castleMenu.handleInput(player);
        } while (result != 4);
    }

    private void startNewGame() {
        System.out.println(
                GRADIENT_1 + "  ╔════════════════════════════════════════════╗\n" +
                        GRADIENT_2 + "  ║" + BOLD + "         🌍 Выбор режима игры          " + RESET + GRADIENT_2 + "     ║\n" +
                        GRADIENT_3 + "  ╠════════════════════════════════════════════╣\n" +
                        GRADIENT_1 + "  ║                                            ║\n" +
                        GRADIENT_2 + "  ║    " + YELLOW + "1. " + CYAN + "Стандартная генерация карты" +
                        " ".repeat(7) + GRADIENT_2 + "   ║\n" +
                        GRADIENT_3 + "  ║    " + YELLOW + "2. " + CYAN + "Загрузить свою карту" +
                        " ".repeat(16) + GRADIENT_3 + " ║\n" +
                        GRADIENT_1 + "  ║                                            ║\n" +
                        GRADIENT_2 + "  ╚════════════════════════════════════════════╝" + RESET
        );
        System.out.print("Ваш выбор: ");


        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();

        switch (input) {
            case "1" -> {
                System.out.println("Начинаем новую стандартную игру...");
                gmanager.startGame();
            }
            case "2" -> {
                Field customField = MapEditor.loadCustomMap();
                if (customField != null) {
                    gmanager.setCustomField(customField);
                    gmanager.startGameFromCustomMap();
                } else {
                    System.out.println("❌ Не удалось загрузить пользовательскую карту.");
                }
            }
            default -> System.out.println("❌ Неверный выбор.");
        }
    }

    public GameMenu getGameMenu() {
        return gameMenu;
    }
}
