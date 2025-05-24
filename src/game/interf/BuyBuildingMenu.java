package game.interf;

import game.model.building.incastle.*;
import game.model.hero.HumanHero;

public class BuyBuildingMenu extends Inter {
    @Override
    public void display() {
        System.out.println("\n" +
                "  ╔════════════════════════════════════════════╗\n" +
                "  ║" + BOLD + PURPLE + "       🏰 МЕНЮ ПОКУПКИ ЗДАНИЙ       " + RESET + "        ║\n" +
                "  ╠════════════════════════════════════════════╣\n" +
                "  ║ " + YELLOW + "1. " + CYAN + "Купить таверну (" + GameBuildings.TAVERN.getCost() + " золота)" +
                String.format("%" + (16 - String.valueOf(GameBuildings.TAVERN.getCost()).length()) + "s", "") + RESET + "║\n" +
                "  ║ " + YELLOW + "2. " + CYAN + "Купить конюшню (" + GameBuildings.STABLE.getCost() + " золота)" +
                String.format("%" + (16 - String.valueOf(GameBuildings.STABLE.getCost()).length()) + "s", "") + RESET + "║\n" +
                "  ║ " + YELLOW + "3. " + CYAN + "Купить сторожевой пост (" + GameBuildings.GUARD_POST.getCost() + " золота)" +
                String.format("%" + (8 - String.valueOf(GameBuildings.GUARD_POST.getCost()).length()) + "s", "") + RESET + "║\n" +
                "  ║ " + YELLOW + "4. " + CYAN + "Купить башню арбалетчиков (" + GameBuildings.CROSSBOWMENS_TOWER.getCost() + " золота)" + RESET + "   ║\n" +
                "  ║ " + YELLOW + "5. " + CYAN + "Купить оружейную (" + GameBuildings.ARMORY.getCost() + " золота)" +
                String.format("%" + (14 - String.valueOf(GameBuildings.ARMORY.getCost()).length()) + "s", "") + RESET + "║\n" +
                "  ║ " + YELLOW + "6. " + CYAN + "Купить арену (" + GameBuildings.ARENA.getCost() + " золота)" +
                String.format("%" + (18 - String.valueOf(GameBuildings.ARENA.getCost()).length()) + "s", "") + RESET + "║\n" +
                "  ║ " + YELLOW + "7. " + CYAN + "Купить собор (" + GameBuildings.CATHEDRAL.getCost() + " золота)" +
                String.format("%" + (18 - String.valueOf(GameBuildings.CATHEDRAL.getCost()).length()) + "s", "") + RESET + "║\n" +
                "  ║ " + YELLOW + "8. " + CYAN + "Вернуться в меню управления замком" + " ".repeat(2) + RESET + "    ║\n" +
                "  ╚════════════════════════════════════════════╝\n");
    }

    public int handleInput(HumanHero player) {
        int choice = super.handleInput();
        clearConsole();

        Runnable showGold = () -> System.out.println(BOLD + GOLD_COLOR + "💰 Ваше золото: " + player.getGold() + RESET + "\n");

        switch (choice) {
            case 1: // Таверна
                if (player.noHaveMoney(GameBuildings.TAVERN.getCost())) {
                    showGold.run();
                    System.out.println(RED + "⚠️ Не хватает золота!" + RESET);
                    break;
                }
                if (player.getMyCastle().containsName(GameBuildings.TAVERN)) {
                    showGold.run();
                    System.out.println(RED + "🏠 В замке уже есть таверна!" + RESET);
                    break;
                }
                player.spendMoney(GameBuildings.TAVERN.getCost());
                player.getMyCastle().addBuilding(GameBuildings.TAVERN);
                showGold.run();
                System.out.println(GREEN + "🍺 Таверна куплена! Теперь вы можете нанимать героев за золото!" + RESET);
                break;

            case 2: // Конюшня
                if (player.noHaveMoney(GameBuildings.STABLE.getCost())) {
                    showGold.run();
                    System.out.println(RED + "⚠️ Не хватает золота!" + RESET);
                    break;
                }
                if (player.getMyCastle().containsName(GameBuildings.STABLE)) {
                    showGold.run();
                    System.out.println(RED + "🏠 В замке уже есть конюшня!" + RESET);
                    break;
                }
                player.spendMoney(GameBuildings.STABLE.getCost());
                player.getMyCastle().addBuilding(GameBuildings.STABLE);
                showGold.run();
                System.out.println(GREEN + "🐴 Конюшня куплена! После посещения замка герои будут передвигаться быстрее!" + RESET);
                break;

            case 3: // Сторожевой пост
                if (player.noHaveMoney(GameBuildings.GUARD_POST.getCost())) {
                    showGold.run();
                    System.out.println(RED + "⚠️ Не хватает золота!" + RESET);
                    break;
                }
                if (player.getMyCastle().containsName(GameBuildings.GUARD_POST)) {
                    showGold.run();
                    System.out.println(RED + "🏠 В замке уже есть сторожевой пост!" + RESET);
                    break;
                }
                player.spendMoney(GameBuildings.GUARD_POST.getCost());
                player.getMyCastle().addBuilding(GameBuildings.GUARD_POST);
                showGold.run();
                System.out.println(GREEN + "🛡️ Сторожевая башня построена! Доступны копейщики!" + RESET);
                break;

            case 4: // Башня арбалетчиков
                if (player.noHaveMoney(GameBuildings.CROSSBOWMENS_TOWER.getCost())) {
                    showGold.run();
                    System.out.println(RED + "⚠️ Не хватает золота!" + RESET);
                    break;
                }
                if (player.getMyCastle().containsName(GameBuildings.CROSSBOWMENS_TOWER)) {
                    showGold.run();
                    System.out.println(RED + "🏠 В замке уже есть башня арбалетчиков!" + RESET);
                    break;
                }
                player.spendMoney(GameBuildings.CROSSBOWMENS_TOWER.getCost());
                player.getMyCastle().addBuilding(GameBuildings.CROSSBOWMENS_TOWER);
                showGold.run();
                System.out.println(GREEN + "🏹 Башня арбалетчиков построена! Теперь можно нанимать арбалетчиков!" + RESET);
                break;

            case 5: // Оружейная
                if (player.noHaveMoney(GameBuildings.ARMORY.getCost())) {
                    showGold.run();
                    System.out.println(RED + "⚠️ Не хватает золота!" + RESET);
                    break;
                }
                if (player.getMyCastle().containsName(GameBuildings.ARMORY)) {
                    showGold.run();
                    System.out.println(RED + "🏠 В замке уже есть оружейная!" + RESET);
                    break;
                }
                player.spendMoney(GameBuildings.ARMORY.getCost());
                player.getMyCastle().addBuilding(GameBuildings.ARMORY);
                showGold.run();
                System.out.println(GREEN + "⚔️ Оружейная куплена! Доступны мечники!" + RESET);
                break;

            case 6: // Арена
                if (player.noHaveMoney(GameBuildings.ARENA.getCost())) {
                    showGold.run();
                    System.out.println(RED + "⚠️ Не хватает золота!" + RESET);
                    break;
                }
                if (player.getMyCastle().containsName(GameBuildings.ARENA)) {
                    showGold.run();
                    System.out.println(RED + "🏠 В замке уже есть арена!" + RESET);
                    break;
                }
                player.spendMoney(GameBuildings.ARENA.getCost());
                player.getMyCastle().addBuilding(GameBuildings.ARENA);
                showGold.run();
                System.out.println(GREEN + "🏟️ Арена построена! Теперь можно нанимать кавалеристов!" + RESET);
                break;

            case 7: // Собор
                if (player.noHaveMoney(GameBuildings.CATHEDRAL.getCost())) {
                    showGold.run();
                    System.out.println(RED + "⚠️ Не хватает золота!" + RESET);
                    break;
                }
                if (player.getMyCastle().containsName(GameBuildings.CATHEDRAL)) {
                    showGold.run();
                    System.out.println(RED + "🏠 В замке уже есть собор!" + RESET);
                    break;
                }
                player.spendMoney(GameBuildings.CATHEDRAL.getCost());
                player.getMyCastle().addBuilding(GameBuildings.CATHEDRAL);
                showGold.run();
                System.out.println(GREEN + "⛪ Собор построен! Теперь можно нанимать паладинов!" + RESET);
                break;

            case 8: // Выход
                System.out.println(CYAN + "↩️ Возвращение в меню управления замком..." + RESET);
                break;

            default: // Неверный ввод
                showGold.run();
                System.out.println(RED + "❌ Неверный выбор. Попробуйте снова." + RESET);
        }
        return choice;
    }

}


