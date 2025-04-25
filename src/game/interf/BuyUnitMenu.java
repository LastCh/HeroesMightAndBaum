package game.interf;

import game.model.building.incastle.*;
import game.model.unit.*;
import game.model.player.HumanPlayer;

public class BuyUnitMenu extends Inter {

    @Override
    public void display() {
        System.out.println("\nМеню покупки юнитов:");
        System.out.println("1. Купить копейщика (" + GameUnits.SPEARMAN.getCost() +
                " золота) - требуется " + GameBuildings.GUARD_POST.getName());
        System.out.println("2. Купить арбалетчика (" + GameUnits.CROSSBOWMAN.getCost() +
                " золота) - требуется " + GameBuildings.CROSSBOWMENS_TOWER.getName());
        System.out.println("3. Купить мечника (" + GameUnits.SWORDSMAN.getCost() +
                " золота) - требуется " + GameBuildings.ARMORY.getName());
        System.out.println("4. Купить кавалериста (" + GameUnits.CAVALRYMAN.getCost() +
                " золота) - требуется " + GameBuildings.ARENA.getName());
        System.out.println("5. Купить паладина (" + GameUnits.PALADIN.getCost() +
                " золота) - требуется " + GameBuildings.CATHEDRAL.getName());
        System.out.println("6. Вернуться в меню управления замком\n");
    }

    public int handleInput(HumanPlayer player) {
        int choice = super.handleInput();
        clearConsole();
        switch (choice) {
            case 1: // Копейщик
                if (!player.haveMoney(GameUnits.SPEARMAN.getCost())) {
                    System.out.println("Ваше золото: " + player.getGold() + "\n");
                    System.out.println("Не хватает золота!");
                    break;
                }

                if (!player.getMyCastle().contains(GameBuildings.GUARD_POST)) {
                    System.out.println("Ваше золото: " + player.getGold() + "\n");
                    System.out.println("У вас отсутствует " + GameBuildings.GUARD_POST.getName() +
                            " для найма копейщика!");
                    break;
                }

                player.spendMoney(GameUnits.SPEARMAN.getCost());
                player.addUnits(GameUnits.SPEARMAN);
                System.out.println("Ваше золото: " + player.getGold() + "\n");
                System.out.println("Копейщик куплен!");
                break;

            case 2: // Арбалетчик
                if (!player.haveMoney(GameUnits.CROSSBOWMAN.getCost())) {
                    System.out.println("Ваше золото: " + player.getGold() + "\n");
                    System.out.println("Не хватает золота!");
                    break;
                }

                if (!player.getMyCastle().contains(GameBuildings.CROSSBOWMENS_TOWER)) {
                    System.out.println("Ваше золото: " + player.getGold() + "\n");
                    System.out.println("У вас отсутствует " + GameBuildings.CROSSBOWMENS_TOWER.getName() +
                            " для найма арбалетчика!");
                    break;
                }

                player.spendMoney(GameUnits.CROSSBOWMAN.getCost());
                player.addUnits(GameUnits.CROSSBOWMAN);
                System.out.println("Ваше золото: " + player.getGold() + "\n");
                System.out.println("Арбалетчик куплен!");
                break;

            case 3: // Мечник
                if (!player.haveMoney(GameUnits.SWORDSMAN.getCost())) {
                    System.out.println("Ваше золото: " + player.getGold() + "\n");
                    System.out.println("Не хватает золота!");
                    break;
                }

                if (!player.getMyCastle().contains(GameBuildings.ARMORY)) {
                    System.out.println("Ваше золото: " + player.getGold() + "\n");
                    System.out.println("У вас отсутствует " + GameBuildings.ARMORY.getName() +
                            " для найма мечника!");
                    break;
                }

                player.spendMoney(GameUnits.SWORDSMAN.getCost());
                player.addUnits(GameUnits.SWORDSMAN);
                System.out.println("Ваше золото: " + player.getGold() + "\n");
                System.out.println("Мечник куплен!");
                break;

            case 4: // Кавалерист
                if (!player.haveMoney(GameUnits.CAVALRYMAN.getCost())) {
                    System.out.println("Ваше золото: " + player.getGold() + "\n");
                    System.out.println("Не хватает золота!");
                    break;
                }

                if (!player.getMyCastle().contains(GameBuildings.ARENA)) {
                    System.out.println("Ваше золото: " + player.getGold() + "\n");
                    System.out.println("У вас отсутствует " + GameBuildings.ARENA.getName() +
                            " для найма кавалериста!");
                    break;
                }

                player.spendMoney(GameUnits.CAVALRYMAN.getCost());
                player.addUnits(GameUnits.CAVALRYMAN);
                System.out.println("Ваше золото: " + player.getGold() + "\n");
                System.out.println("Кавалерист куплен!");
                break;

            case 5: // Паладин
                if (!player.haveMoney(GameUnits.PALADIN.getCost())) {
                    System.out.println("Ваше золото: " + player.getGold() + "\n");
                    System.out.println("Не хватает золота!");
                    break;
                }

                if (!player.getMyCastle().contains(GameBuildings.CATHEDRAL)) {
                    System.out.println("Ваше золото: " + player.getGold() + "\n");
                    System.out.println("У вас отсутствует " + GameBuildings.CATHEDRAL.getName() +
                            " для найма паладина!");
                    break;
                }

                player.spendMoney(GameUnits.PALADIN.getCost());
                player.addUnits(GameUnits.PALADIN);
                System.out.println("Ваше золото: " + player.getGold() + "\n");
                System.out.println("Паладин куплен!");
                break;

            case 6:
                System.out.println("Возврат в игровое меню...");
                break;

            default:
                System.out.println("Ваше золото: " + player.getGold() + "\n");
                System.out.println("Неверный выбор. Попробуйте снова.");
        }
        return choice;
    }
}