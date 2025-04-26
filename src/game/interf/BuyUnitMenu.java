package game.interf;

import game.model.building.incastle.*;
import game.model.unit.*;
import game.model.hero.HumanHero;

public class BuyUnitMenu extends Inter {
    @Override
    public void display() {
        System.out.println("\n" +
                "  ╔══════════════════════════════════════════════════════════╗\n" +
                "  ║" + BOLD + PURPLE + "                 ⚔️ МЕНЮ ПОКУПКИ ЮНИТОВ                " + RESET + "   ║\n" +
                "  ╠══════════════════════════════════════════════════════════╣\n" +
                "  ║ " + YELLOW + "1. " + CYAN + "Копейщик (" + GameUnits.SPEARMAN.getCost() +
                " золота) - требует " + GuardPost.getName() +
                String.format("%" + (25 - String.valueOf(GameUnits.SPEARMAN.getCost()).length() -
                        GuardPost.getName().length()) + "s", "") + RESET + "║\n" +
                "  ║ " + YELLOW + "2. " + CYAN + "Арбалетчик (" + GameUnits.CROSSBOWMAN.getCost() +
                " золота) - требует " + CrossbowmensTower.getName() +
                String.format("%" + (17 - String.valueOf(GameUnits.CROSSBOWMAN.getCost()).length() -
                        CrossbowmensTower.getName().length()) + "s", "") + RESET + "║\n" +
                "  ║ " + YELLOW + "3. " + CYAN + "Мечник (" + GameUnits.SWORDSMAN.getCost() +
                " золота) - требует " + Armory.getName() +
                String.format("%" + (27 - String.valueOf(GameUnits.SWORDSMAN.getCost()).length() -
                        Armory.getName().length()) + "s", "") + RESET + "║\n" +
                "  ║ " + YELLOW + "4. " + CYAN + "Кавалерист (" + GameUnits.CAVALRYMAN.getCost() +
                " золота) - требует " + Arena.getName() +
                String.format("%" + (23 - String.valueOf(GameUnits.CAVALRYMAN.getCost()).length() -
                        Arena.getName().length()) + "s", "") + RESET + "║\n" +
                "  ║ " + YELLOW + "5. " + CYAN + "Паладин (" + GameUnits.PALADIN.getCost() +
                " золота) - требует " + Cathedral.getName() +
                String.format("%" + (26 - String.valueOf(GameUnits.PALADIN.getCost()).length() -
                        Cathedral.getName().length()) + "s", "") + RESET + "║\n" +
                "  ║ " + YELLOW + "6. " + CYAN + "Вернуться в меню управления замком" +
                String.format("%" + 20 + "s", "") + RESET + "║\n" +
                "  ╚══════════════════════════════════════════════════════════╝\n");
    }

    public int handleInput(HumanHero player) {
        int choice = super.handleInput();
        clearConsole();

        Runnable showGold = () -> System.out.println(BOLD + GOLD_COLOR + "💰 Ваше золото: " + player.getGold() + RESET + "\n");

        switch (choice) {
            case 1: // Копейщик
                if (player.noHaveMoney(GameUnits.SPEARMAN.getCost())) {
                    showGold.run();
                    System.out.println(RED + "⚠️ Не хватает золота!" + RESET);
                    break;
                }

                if (!player.getMyCastle().contains(GameBuildings.GUARD_POST)) {
                    showGold.run();
                    System.out.println(RED + "❌ Требуется " + GuardPost.getName() + "!" + RESET);
                    break;
                }

                player.spendMoney(GameUnits.SPEARMAN.getCost());
                player.addUnits(GameUnits.SPEARMAN.clone());
                showGold.run();
                System.out.println(GREEN + "🛡️ Копейщик нанят в вашу армию!" + RESET);
                break;

            case 2: // Арбалетчик
                if (player.noHaveMoney(GameUnits.CROSSBOWMAN.getCost())) {
                    showGold.run();
                    System.out.println(RED + "⚠️ Не хватает золота!" + RESET);
                    break;
                }

                if (!player.getMyCastle().contains(GameBuildings.CROSSBOWMENS_TOWER)) {
                    showGold.run();
                    System.out.println(RED + "❌ Требуется " + CrossbowmensTower.getName() + "!" + RESET);
                    break;
                }

                player.spendMoney(GameUnits.CROSSBOWMAN.getCost());
                player.addUnits(GameUnits.CROSSBOWMAN.clone());
                showGold.run();
                System.out.println(GREEN + "🏹 Арбалетчик присоединился к отряду!" + RESET);
                break;

            case 3: // Мечник
                if (player.noHaveMoney(GameUnits.SWORDSMAN.getCost())) {
                    showGold.run();
                    System.out.println(RED + "⚠️ Не хватает золота!" + RESET);
                    break;
                }

                if (!player.getMyCastle().contains(GameBuildings.ARMORY)) {
                    showGold.run();
                    System.out.println(RED + "❌ Требуется " + Armory.getName() + "!" + RESET);
                    break;
                }

                player.spendMoney(GameUnits.SWORDSMAN.getCost());
                player.addUnits(GameUnits.SWORDSMAN.clone());
                showGold.run();
                System.out.println(GREEN + "⚔️ Мечник готов к бою!" + RESET);
                break;

            case 4: // Кавалерист
                if (player.noHaveMoney(GameUnits.CAVALRYMAN.getCost())) {
                    showGold.run();
                    System.out.println(RED + "⚠️ Не хватает золота!" + RESET);
                    break;
                }

                if (!player.getMyCastle().contains(GameBuildings.ARENA)) {
                    showGold.run();
                    System.out.println(RED + "❌ Требуется " + Arena.getName() + "!" + RESET);
                    break;
                }

                player.spendMoney(GameUnits.CAVALRYMAN.getCost());
                player.addUnits(GameUnits.CAVALRYMAN.clone());
                showGold.run();
                System.out.println(GREEN + "🐎 Кавалерия пополнена новым бойцом!" + RESET);
                break;

            case 5: // Паладин
                if (player.noHaveMoney(GameUnits.PALADIN.getCost())) {
                    showGold.run();
                    System.out.println(RED + "⚠️ Не хватает золота!" + RESET);
                    break;
                }

                if (!player.getMyCastle().contains(GameBuildings.CATHEDRAL)) {
                    showGold.run();
                    System.out.println(RED + "❌ Требуется " + Cathedral.getName() + "!" + RESET);
                    break;
                }

                player.spendMoney(GameUnits.PALADIN.getCost());
                player.addUnits(GameUnits.PALADIN.clone());
                showGold.run();
                System.out.println(GREEN + "✨ Паладин освятил ваши ряды!" + RESET);
                break;

            case 6: // Выход
                System.out.println(CYAN + "↩️ Возвращение в меню управления замком..." + RESET);
                break;

            default: // Неверный ввод
                showGold.run();
                System.out.println(RED + "❌ Неверный выбор. Попробуйте снова." + RESET);
        }
        return choice;
    }
}