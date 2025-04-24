package game.interf;

import game.model.building.incastle.*;
import game.model.player.HumanPlayer;

public class BuyBuildingMenu extends Inter {
    @Override
    public void display() {
        System.out.println("Меню покупки зданий:");
        System.out.println("1. Купить таверну ("+tavern.getCost()+" золота)");
        System.out.println("2. Купить конюшню ("+stable.getCost()+" золота)");
        System.out.println("3. Купить сторожевой пост ("+guardPost.getCost()+" золота)");
        System.out.println("4. Купить башню арбалетчиков ("+crossbowmensTower.getCost()+" золота)");
        System.out.println("5. Купить оружейную ("+armory.getCost()+" золота)");
        System.out.println("6. Купить арену ("+arena.getCost()+" золота)");
        System.out.println("7. Купить собор ("+cathedral.getCost()+" золота)");
        System.out.println("8. Вернуться в главное меню");
    }

    public int handleInput(HumanPlayer player) {
        int choice = super.handleInput();
        clearConsole();
        switch (choice) {
            case 1:
                if (!player.haveMoney(tavern.getCost())) {
                    System.out.println("Не хватает золота!");
                    break;
                }

                if(player.getMyCastle().contains(tavern)){
                    System.out.println("В замке уже есть таверна!");
                    break;
                }

                player.spendMoney(tavern.getCost());
                player.getMyCastle().addBuilding(tavern);
                System.out.println("Таверна куплена, теперь вы сможете приобретать героев за золото!");
                break;
            case 2:
                if (!player.haveMoney(stable.getCost())) {
                    System.out.println("Не хватает золота!");
                    break;
                }

                if(player.getMyCastle().contains(stable)){
                    System.out.println("В замке уже есть конюшня!");
                    break;
                }

                player.spendMoney(stable.getCost());
                player.getMyCastle().addBuilding(stable);
                System.out.println("Конюшня куплена, после посещения замка ваши герои смогут передвигаться быстрее!");
                break;
            case 3:
                if (!player.haveMoney(guardPost.getCost())) {
                    System.out.println("Не хватает золота!");
                    break;
                }

                if(player.getMyCastle().contains(guardPost)){
                    System.out.println("В замке уже есть сторожевой пост!");
                    break;
                }

                player.spendMoney(guardPost.getCost());
                player.getMyCastle().addBuilding(guardPost);
                System.out.println("Сторожевая башня куплена, теперь вы можете приобретать копейщиков!");
                break;
            case 4:
                if (!player.haveMoney(crossbowmensTower.getCost())) {
                    System.out.println("Не хватает золота!");
                    break;
                }

                if(player.getMyCastle().contains(crossbowmensTower)){
                    System.out.println("В замке уже есть башня арбалетчиков!");
                    break;
                }

                player.spendMoney(crossbowmensTower.getCost());
                player.getMyCastle().addBuilding(crossbowmensTower);
                System.out.println("Башня арбалетчиков куплена, теперь вы можете приобретать арбалетчиков!");
                break;
            case 5:
                if (!player.haveMoney(armory.getCost())) {
                    System.out.println("Не хватает золота!");
                    break;
                }

                if(player.getMyCastle().contains(armory)){
                    System.out.println("В замке уже есть оружейная!");
                    break;
                }

                player.spendMoney(armory.getCost());
                player.getMyCastle().addBuilding(armory);
                System.out.println("Оружейная куплена, теперь вы можете приобретать мечников!");
                break;
            case 6:
                if (!player.haveMoney(arena.getCost())) {
                    System.out.println("Не хватает золота!");
                    break;
                }

                if(player.getMyCastle().contains(arena)){
                    System.out.println("В замке уже есть арена!");
                    break;
                }

                player.spendMoney(arena.getCost());
                player.getMyCastle().addBuilding(arena);
                System.out.println("Арена куплена, теперь вы можете приобретать кавалеристов!");
                break;
            case 7:
                if (!player.haveMoney(cathedral.getCost())) {
                    System.out.println("Не хватает золота!");
                    break;
                }

                if(player.getMyCastle().contains(cathedral)){
                    System.out.println("В замке уже есть собор!");
                    break;
                }

                player.spendMoney(cathedral.getCost());
                player.getMyCastle().addBuilding(cathedral);
                System.out.println("Собор куплен, теперь вы можете приобретать паладинов!");
                break;
            case 8:
                System.out.println("Возврат в игровое меню...");
                break;
            default:
                System.out.println("Неверный выбор. Попробуйте снова.");
        }
        return choice;
    }
    private static final Arena arena = new Arena();
    private static final GuardPost guardPost = new GuardPost();
    private static final Tavern tavern = new Tavern();
    private static final Stable stable = new Stable();
    private static final CrossbowmensTower crossbowmensTower = new CrossbowmensTower();
    private static final Armory armory = new Armory();
    private static final Cathedral cathedral = new Cathedral();
}