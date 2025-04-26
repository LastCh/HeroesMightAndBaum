package game.interf;

import game.core.engine.GameManager;
import game.model.hero.ComputerHero;
import game.model.hero.HumanHero;


public class MenuManager {
    private final StartMenu startMenu = new StartMenu();
    private final CastleMenu castleMenu = new CastleMenu();
    private final GameMenu gameMenu = new GameMenu();
    private final GameManager gmanager = new GameManager();
    private HumanHero hplayer;
    private ComputerHero cplayer;

    public void run() {
        boolean inMainMenu = true;
        while (inMainMenu) {
            startMenu.display();
            int choice = startMenu.handleInput();

            switch (choice) {
                case 1:
                    startNewGame();
                    inMainMenu = false;
                    break;
                case 2:
                    loadGame();
                    inMainMenu = false;
                    break;
                case 3:
                    System.out.println("Выход из игры...");
                    return;
                default:
                    startMenu.clearConsole();
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }

        }
    }

    public boolean showGameMenu() {
        // ANSI коды цветов
        final String RESET = "\u001B[0m";
        final String CYAN = "\u001B[36m";
        final String YELLOW = "\u001B[33m";
        final String PURPLE = "\u001B[35m";
        final String BOLD = "\u001B[1m";
        final String RED = "\u001B[31m";

        int choice;
        do {
            System.out.println("\n" +
                    "  ╔══════════════════════════════════╗\n" +
                    "  ║" + BOLD + PURPLE + "         🏰 ИГРОВОЕ МЕНЮ          " + RESET + "║\n" +
                    "  ╠══════════════════════════════════╣\n" +
                    "  ║ " + YELLOW + "1. " + CYAN + "Управление замком             " + RESET + "║\n" +
                    "  ║ " + YELLOW + "2. " + CYAN + "Вернуться в игру              " + RESET + "║\n" +
                    "  ║ " + YELLOW + "3. " + CYAN + "Главное меню                  " + RESET + "║\n" +
                    "  ╚══════════════════════════════════╝\n");

            choice = startMenu.handleInput();

            switch (choice) {
                case 1:
                    startMenu.clearConsole();
                    handleCastleMenu(hplayer);
                    break;
                case 3:
                    return true;
                default:
                    startMenu.clearConsole();
                    if (choice != 2) {
                        System.out.println("\n" + RED + "  ❌ Неверный выбор. Попробуйте снова." + RESET);
                    }
            }
        } while (choice != 2);
        return false;
    }

    public void callGame() {
        gameMenu.display();
    }

    private void handleCastleMenu(HumanHero player) {
        int result;
        do {
            castleMenu.display();
            result = castleMenu.handleInput(player);
        } while (result != 4);
    }

    private void startNewGame() {
        gameMenu.clearConsole();
        System.out.println("Начинаем новую игру...");
        gmanager.startGame();
    }

    public void setComputerPlayer(ComputerHero player) {
        cplayer = player;
    }

    public ComputerHero getComputerPlayer() {
        return cplayer;
    }

    public void setHumanPlayer(HumanHero player) {
        hplayer = player;
    }

    public HumanHero getHumanPlayer() {
        return hplayer;
    }

    public GameMenu getGameMenu() {
        return gameMenu;
    }

    private void loadGame() {
        System.out.println("Загрузка игры...");
    }
}