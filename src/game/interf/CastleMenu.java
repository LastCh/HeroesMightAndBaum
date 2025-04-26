package game.interf;

import game.model.hero.HumanHero;

public class CastleMenu extends Inter {
    private final BuyBuildingMenu buyBuildingMenu = new BuyBuildingMenu();
    private final BuyUnitMenu buyUnitMenu = new BuyUnitMenu();
    private final BuyHeroesMenu buyHeroesMenu = new BuyHeroesMenu();

    @Override
    public void display() {
        System.out.println("\n" +
                "  ╔══════════════════════════════════╗\n" +
                "  ║" + BOLD + PURPLE + "         🏰 МЕНЮ ЗАМКА          " + RESET + "  ║\n" +
                "  ╠══════════════════════════════════╣\n" +
                "  ║ " + YELLOW + "1. " + CYAN + "Улучшить замок               " + RESET + " ║\n" +
                "  ║ " + YELLOW + "2. " + CYAN + "Нанять юнитов                " + RESET + " ║\n" +
                "  ║ " + YELLOW + "3. " + CYAN + "Купить героев                " + RESET + " ║\n" +
                "  ║ " + YELLOW + "4. " + CYAN + "Вернуться в игровое меню     " + RESET + " ║\n" +
                "  ╚══════════════════════════════════╝\n");
    }

    public int handleInput(HumanHero player) {
        int choice = super.handleInput();
        clearConsole();
        switch (choice) {
            case 1:
                clearConsole();
                System.out.println(BOLD + GOLD_COLOR + "💰 Ваше золото: " + player.getGold() + RESET + "\n");
                handleBuildingMenu(player);
                break;
            case 2:
                clearConsole();
                System.out.println(BOLD + GOLD_COLOR + "💰 Ваше золото: " + player.getGold() + RESET + "\n");
                handleUnitMenu(player);
                break;
            case 3:
                clearConsole();
                System.out.println(BOLD + GOLD_COLOR + "💰 Ваше золото: " + player.getGold() + RESET + "\n");
                handleHeroesMenu(player);
                break;
            case 4:
                System.out.println(CYAN + "↩️ Возвращение в игровое меню..." + RESET);
                break;
            default:
                System.out.println(RED + "❌ Неверный выбор. Попробуйте снова." + RESET);
        }
        return choice;
    }

    private void handleUnitMenu(HumanHero player) {
        int result;
        do {
            buyUnitMenu.display();
            result = buyUnitMenu.handleInput(player);
        } while (result != 6);
    }

    private void handleHeroesMenu(HumanHero player) {
        int result;
        do {
            buyHeroesMenu.display();
            result = buyHeroesMenu.handleInput(player);
        } while (result != 4);
    }

    private void handleBuildingMenu(HumanHero player) {
        int result;
        do {
            buyBuildingMenu.display();
            result = buyBuildingMenu.handleInput(player);
        } while (result != 8);
    }
}