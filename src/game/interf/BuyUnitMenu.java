package game.interf;

import game.model.building.incastle.*;
import game.model.unit.*;
import game.model.hero.HumanHero;

import java.util.Scanner;

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
        Scanner scanner = new Scanner(System.in);

        Unit selectedUnit = null;
        BuildingCastle requiredBuilding = null;
        String successMessage = "";

        switch (choice) {
            case 1:
                selectedUnit = GameUnits.SPEARMAN;
                requiredBuilding = GameBuildings.GUARD_POST;
                successMessage = GREEN + "🛡️ Копейщик нанят в вашу армию!" + RESET;
                break;
            case 2:
                selectedUnit = GameUnits.CROSSBOWMAN;
                requiredBuilding = GameBuildings.CROSSBOWMENS_TOWER;
                successMessage = GREEN + "🏹 Арбалетчик присоединился к отряду!" + RESET;
                break;
            case 3:
                selectedUnit = GameUnits.SWORDSMAN;
                requiredBuilding = GameBuildings.ARMORY;
                successMessage = GREEN + "⚔️ Мечник готов к бою!" + RESET;
                break;
            case 4:
                selectedUnit = GameUnits.CAVALRYMAN;
                requiredBuilding = GameBuildings.ARENA;
                successMessage = GREEN + "🐎 Кавалерия пополнена новыми бойцами!" + RESET;
                break;
            case 5:
                selectedUnit = GameUnits.PALADIN;
                requiredBuilding = GameBuildings.CATHEDRAL;
                successMessage = GREEN + "✨ Паладины освятили ваши ряды!" + RESET;
                break;
            case 6:
                System.out.println(CYAN + "↩️ Возвращение в меню управления замком..." + RESET);
                return choice;
            default:
                showGold.run();
                System.out.println(RED + "❌ Неверный выбор. Попробуйте снова." + RESET);
                return choice;
        }

        if (!player.getMyCastle().containsName(requiredBuilding)) {
            showGold.run();
            System.out.println(RED + "❌ Требуется " + requiredBuilding.getNameNotStat() + "!" + RESET);
            return choice;
        }

        System.out.print(BOLD + "🔢 Введите количество юнитов для найма: " + RESET);
        int count;
        try {
            count = Integer.parseInt(scanner.nextLine());
            if (count <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println(RED + "❌ Некорректное число!" + RESET);
            return choice;
        }

        int totalCost = selectedUnit.getCost() * count;
        if (player.noHaveMoney(totalCost)) {
            showGold.run();
            System.out.println(RED + "⚠️ Не хватает золота на " + count + " юнитов (нужно " + totalCost + ")" + RESET);
            return choice;
        }

        player.spendMoney(totalCost);
        for (int i = 0; i < count; i++) {
            player.addUnits(selectedUnit.clone());
        }

        showGold.run();
        System.out.println(successMessage + " (×" + count + ")");
        return choice;
    }
}