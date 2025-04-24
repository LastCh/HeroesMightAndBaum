package game.interf;

import game.model.building.incastle.*;
import game.model.unit.*;
import game.model.player.HumanPlayer;

public class BuyUnitMenu extends Inter {

    @Override
    public void display() {
        System.out.println("Меню покупки юнитов:");
        System.out.println("1. Купить копейщика (10 золота)");
        System.out.println("2. Купить арбалетчика (20 золота)");
        System.out.println("3. Купить мечника (30 золота)");
        System.out.println("4. Купить кавалериста (40 золота)");
        System.out.println("5. Купить паладина (50 золота)");
        System.out.println("6. Вернуться в главное меню");
    }

    public int handleInput(HumanPlayer player) {
        int choice = super.handleInput();
        clearConsole();
        switch (choice) {
            case 1:
                if (!player.haveMoney(10)) {
                    System.out.println("Не хватает золота!");
                    break;
                }

                if(!player.getMyCastle().contains(guardPost)){
                    System.out.println("У вас нет сторожевого поста для найма копейщика!");
                    break;
                }

                player.spendMoney(10);
                player.addUnits(spearman);
                System.out.println("Копейщик куплен!");
                break;
            case 2:
                if (!player.haveMoney(20)) {
                    System.out.println("Не хватает золота!");
                    break;
                }

                if(!player.getMyCastle().contains(crossbowmensTower)){
                    System.out.println("У вас нет башни арбалетчиков для найма арбалетчика!");
                    break;
                }

                player.spendMoney(20);
                player.addUnits(crossbowman);
                System.out.println("Арбалетчик куплен!");
                break;
            case 3:
                if (!player.haveMoney(30)) {
                    System.out.println("Не хватает золота!");
                    break;
                }

                if(!player.getMyCastle().contains(armory)){
                    System.out.println("У вас нет оружейной для найма мечника!");
                    break;
                }

                player.spendMoney(30);
                player.addUnits(swordsman);
                System.out.println("Мечник куплен!");
                break;
            case 4:
                if (!player.haveMoney(40)) {
                    System.out.println("Не хватает золота!");
                    break;
                }

                if(!player.getMyCastle().contains(arena)){
                    System.out.println("У вас нет арены для найма кавалеристов!");
                    break;
                }

                player.spendMoney(40);
                player.addUnits(cavalryman);
                System.out.println("Кавалерист куплен!");
                break;
            case 5:
                if (!player.haveMoney(50)) {
                    System.out.println("Не хватает золота!");
                    break;
                }

                if(!player.getMyCastle().contains(cathedral)){
                    System.out.println("У вас нет Собора для найма паладина!");
                    break;
                }

                player.spendMoney(50);
                player.addUnits(paladin);
                System.out.println("Паладин куплен!");
                break;
            case 6:
                System.out.println("Возврат в игровое меню...");
                break;
            default:
                System.out.println("Неверный выбор. Попробуйте снова.");
        }
        return choice;
    }

    private Cavalryman cavalryman;
    private Paladin paladin;
    private Swordsman swordsman;
    private Crossbowman crossbowman;
    private Spearman spearman;
    private GuardPost guardPost;
    private Tavern tavern;
    private Stable stable;
    private CrossbowmensTower crossbowmensTower;
    private Armory armory;
    private Arena arena;
    private Cathedral cathedral;
}