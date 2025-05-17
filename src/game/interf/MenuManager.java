package game.interf;

import game.core.engine.GameManager;
import game.core.engine.GameSave;
import game.model.hero.ComputerHero;
import game.model.hero.HumanHero;

import java.io.*;
import java.util.List;
import java.util.Scanner;

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
            }
        }

        System.out.println("Добро пожаловать, " + playerName + "!");
        this.gmanager = new GameManager(playerName);

        while (true) {
            startMenu.display();
            int choice = startMenu.handleInput();

            switch (choice) {
                case 1 -> startNewGame();
                case 2 -> loadGame(gmanager);
                case 3 -> {
                    System.out.println("Выход из игры...");
                    return;
                }
                default -> {
                    startMenu.clearConsole();
                    System.out.println(RED + "❌ Неверный выбор. Попробуйте снова." + RESET);
                }
            }
        }
    }

    public boolean showGameMenu(HumanHero hplayer, ComputerHero cplayer, GameManager gmanager) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n" +
                    GRADIENT_1 + "  ╔════════════════════════════╗\n" +
                    GRADIENT_2 + "  ║" + BOLD + "        🏰 Игровое меню     " + RESET + GRADIENT_2 + "║\n" +
                    GRADIENT_3 + "  ╠════════════════════════════╣\n" +
                    GRADIENT_1 + "  ║  " + YELLOW + "1. " + CYAN + "Управление замком      " + RESET + GRADIENT_1 + "║\n" +
                    GRADIENT_2 + "  ║  " + YELLOW + "2. " + CYAN + "Вернуться в игру       " + RESET + GRADIENT_2 + "║\n" +
                    GRADIENT_3 + "  ║  " + YELLOW + "3. " + CYAN + "Сохранить игру         " + RESET + GRADIENT_3 + "║\n" +
                    GRADIENT_1 + "  ║  " + YELLOW + "4. " + CYAN + "Главное меню           " + RESET + GRADIENT_1 + "║\n" +
                    GRADIENT_2 + "  ╚════════════════════════════╝\n" + RESET);


            int choice = scanner.nextInt();
            switch (choice) {
                case 1 ->{
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
                default ->{
                    clearConsole();
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
        System.out.println("Введиет название сохранения:");
        Scanner scanner = new Scanner(System.in);
        String saveName = scanner.nextLine().trim();
        GameSave gameSave = new GameSave();
        gameSave.loadGame(saveName);
        gmanager.loadGame(gameSave);

    }

    private void loadGameN() {
        clearConsole();
        System.out.println(CYAN + "      Для закрытия меню нажмите "
                + BOLD + "Q" + RESET + GRADIENT_2 + "    ");
        System.out.println("\n" +
                GRADIENT_1 + "  ╔════════════════════════════════╗\n" +
                GRADIENT_2 + "  ║" + BOLD + "         📂 Загрузить игру      " + RESET + GRADIENT_2 + "║\n" +
                GRADIENT_3 + "  ╠════════════════════════════════╣" + RESET);
        // Сначала читаем все записи в память, чтобы не открывать файл на каждую попытку
        List<String> saves;
        try (BufferedReader reader = new BufferedReader(new FileReader("saves.txt"))) {
            saves = reader.lines().toList();
        } catch (IOException e) {
            System.out.println("  " + RED + "❌ Ошибка чтения списка сохранений: " + e.getMessage() + RESET);
            System.out.println(GRADIENT_1 + "  ╚════════════════════════════════╝" + RESET);
            return;
        }

        if (saves.isEmpty()) {
            System.out.println("  " + YELLOW + "⚠️ Нет доступных сохранений." + RESET);
            System.out.println(GRADIENT_1 + "  ╚════════════════════════════════╝" + RESET);
            return;
        }

        System.out.println(GRADIENT_1 + "  ║ " + CYAN + "Доступные сохранения:" + RESET + GRADIENT_1 + "          ║");
        for (String line : saves) {
            String name = line.split(";")[0];
            System.out.println(GRADIENT_2 + "  ║   " + YELLOW + "- " + name + RESET +
                    GRADIENT_2 + " ".repeat(24 - name.length()) + "   ║");
        }
        System.out.println(GRADIENT_3 + "  ╠════════════════════════════════╣" + RESET);

        Scanner scanner = new Scanner(System.in);
        boolean loaded = false;

        while (!loaded) {
            System.out.print(BOLD + "  Введите имя сохранения: " + RESET);
            String chosen = scanner.nextLine().trim();

            for (String line : saves) {
                String[] parts = line.split(";");
                if (parts[0].equals(chosen)) {
                    clearConsole();
                    System.out.println("  " + GREEN + "✅ Сохранение \"" + chosen + "\" загружено." + RESET);
                    loaded = true;
                }
            }

            if (chosen.equalsIgnoreCase("Q")) {
                break;
            }

            if (!loaded) {
                System.out.println("  " + RED + "⚠️ Сохранение не найдено. Повторите ввод." + RESET);
            }else{
                for (String line : saves) {
                    String[] parts = line.split(";");
                    if (parts[0].equals(chosen)) {


                        break;
                    }
                }
            }
        }

        System.out.println(GRADIENT_1 + "  ╚════════════════════════════════╝" + RESET);
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
