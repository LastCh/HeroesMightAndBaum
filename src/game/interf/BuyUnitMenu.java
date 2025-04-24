package game.interf;

import game.model.building.incastle.GuardPost;
import game.model.player.HumanPlayer;
import game.model.unit.Crossbowman;
import game.model.unit.Spearman;

public class BuyUnitMenu extends Inter {
    private GuardPost guardPost;
    private Crossbowman crossbowman;
    private Spearman spearman;

    @Override
    public void display() {
        System.out.println("Меню покупки юнитов:");
        System.out.println("1. Купить копейщика (10 золота)");
        System.out.println("2. Купить арбалетчика (20 золота)");
        System.out.println("3. Купить месника (30 золота)");
        System.out.println("4. Купить кавалериста (40 золота)");
        System.out.println("5. Купить паладина (50 золота)");
        System.out.println("6. Вернуться в главное меню");
    }

    public int handleInput(HumanPlayer player) {
        int choice = super.handleInput();
        clearConsole();
        int gold = player.getGold();
        switch (choice) {
            case 1:
                if (!player.haveMoney(10)) {
                    System.out.println("Не хватает золота!");
                    break;
                }

                if(!player.getMyCastle().contains(guardPost)){
                    System.out.println("У вас нет сторожевого поста для найма кавалериста!");
                    break;
                }

                player.spendMoney(10);
                player.addUnits(spearman);
                System.out.println("Пехотинец куплен!");
                break;
            case 2:
                if (gold >= 20) {
                    player.setGold(gold - 20);
                    System.out.println("Кавалерист куплен!");
                } else {
                    System.out.println("Не хватает золота!");
                }
                break;
            case 3:
                if (gold >= 30) {
                    player.setGold(gold - 30);
                    System.out.println("Кавалерист куплен!");
                } else {
                    System.out.println("Не хватает золота!");
                }
                break;
            case 4:
                if (gold >= 40) {
                    player.setGold(gold - 40);
                    System.out.println("Кавалерист куплен!");
                } else {
                    System.out.println("Не хватает золота!");
                }
                break;
            case 5:
                if (gold >= 50) {
                    player.setGold(gold - 50);
                    System.out.println("Кавалерист куплен!");
                } else {
                    System.out.println("Не хватает золота!");
                }
                break;
            case 6:
                System.out.println("Возврат в игровое меню...");
                break;
            default:
                System.out.println("Неверный выбор. Попробуйте снова.");
        }
        return choice;
    }

}