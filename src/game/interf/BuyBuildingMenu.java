package game.interf;

import game.model.building.incastle.BuildingCastle;
import game.model.building.incastle.GuardPost;
import game.model.building.incastle.Stable;
import game.model.building.incastle.Tavern;
import game.model.player.HumanPlayer;

public class BuyBuildingMenu extends Inter {
    private int gold = 200;
    private GuardPost guardPost;

    @Override
    public void display() {
        System.out.println("Меню покупки зданий:");
        System.out.println("1. Купить таверну (10 золота)");
        System.out.println("2. Купить конюшню (20 золота)");
        System.out.println("3. Купить сторожевой пост (30 золота)");
        System.out.println("4. Купить башню арбалетчиков (40 золота)");
        System.out.println("5. Купить башню оружейную (50 золота)");
        System.out.println("6. Купить башню арену (60 золота)");
        System.out.println("7. Купить башню собор (70 золота)");
        System.out.println("8. Вернуться в главное меню");
    }

    public int handleInput(HumanPlayer player) {
        int choice = super.handleInput();
        clearConsole();
        switch (choice) {
            case 1:
                if (gold >= 10) {

                } else {
                    System.out.println("Не хватает золота!");
                }
                break;
            case 2:
                if (gold >= 20) {

                } else {
                    System.out.println("Не хватает золота!");
                }
                break;
            case 3:
                if (!player.haveMoney(10)) {
                    System.out.println("Не хватает золота!");
                    break;
                }

                player.spendMoney(10);
                player.getMyCastle().addBuilding(guardPost);
                System.out.println("Сторожевая башня куплена, теперь вы можете приобретать копейщиков!");
                break;
            case 4:
                System.out.println("Возврат в игровое меню...");
                break;
            case 5:
                System.out.println("Возврат в игровое меню...");
                break;
            case 6:
                System.out.println("Возврат в игровое меню...");
                break;
            case 7:
                System.out.println("Возврат в игровое меню...");
                break;
            case 8:
                System.out.println("Возврат в игровое меню...");
                break;
            default:
                System.out.println("Неверный выбор. Попробуйте снова.");
        }
        return choice;
    }
}