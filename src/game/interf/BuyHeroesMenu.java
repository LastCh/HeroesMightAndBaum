package game.interf;

import game.api.Position;
import game.map.Field;
import game.model.hero.*;

import java.util.List;
import java.util.ArrayList;


public class BuyHeroesMenu extends Inter {

    public BuyHeroesMenu() { }

    @Override
    public void display() {
        System.out.println("\n" +
                "  ╔════════════════════════════════════════════════════╗\n" +
                "  ║" + BOLD + GREEN + "             🧝 МЕНЮ НАЙМА ГЕРОЕВ              " + RESET + "     ║\n" +
                "  ╠════════════════════════════════════════════════════╣\n" +
                "  ║ " + YELLOW + "1. " + CYAN + "Эльф (120 золота)" + RESET + "                               ║\n" +
                "  ║ " + YELLOW + "2. " + CYAN + "Орк (100 золота)" + RESET + "                                ║\n" +
                "  ║ " + YELLOW + "3. " + CYAN + "Гном (90 золота)" + RESET + "                                ║\n" +
                "  ║ " + YELLOW + "4. " + CYAN + "Назад в меню" + RESET + "                                    ║\n" +
                "  ╚════════════════════════════════════════════════════╝\n");
    }

    public int handleInput(HumanHero player) {
        int choice = super.handleInput();
        clearConsole();

        Runnable showGold = () -> System.out.println(BOLD + GOLD_COLOR + "💰 Ваше золото: " + player.getGold() + RESET + "\n");
        Field field = player.getMyCastle().getField();

        switch (choice) {
            case 1: // Эльф
                if (player.noHaveMoney(120)) {
                    showGold.run();
                    System.out.println(RED + "⚠️ Недостаточно золота для найма эльфа!" + RESET);
                    break;
                }

                Position castlePos = player.getMyCastle().getPosition();
                Position spawnPos = field.findFreeAdjacent(castlePos);

                if (spawnPos != null) {
                    PurchasableHero elf = new ElfHero(spawnPos, player.getDirection(), GREEN, player.getMyCastle(), 3, 6, 120, player);
                    field.getCell(spawnPos.x(), spawnPos.y()).addObject(elf);
                    field.addHeroToAll(elf);
                    player.spendMoney(120);
                    showGold.run();
                    System.out.println(GREEN + "🧝 Эльф присоединился к вашей армии!" + RESET);
                } else {
                    System.out.println(RED + "⚠️ Нет свободного места рядом с замком!" + RESET);
                }
                break;

            case 2: // Орк
                if (player.noHaveMoney(100)) {
                    showGold.run();
                    System.out.println(RED + "⚠️ Недостаточно золота для найма орка!" + RESET);
                    break;
                }

                castlePos = player.getMyCastle().getPosition();
                spawnPos = field.findFreeAdjacent(castlePos);

                if (spawnPos != null) {
                    PurchasableHero orc = new OrcHero(spawnPos, player.getDirection(), GREEN, player.getMyCastle(), 2, 5, 100, player);
                    field.getCell(spawnPos.x(), spawnPos.y()).addObject(orc);
                    field.addHeroToAll(orc);
                    player.spendMoney(100);
                    showGold.run();
                    System.out.println(GREEN + "💢 Орк готов к бою!" + RESET);
                } else {
                    System.out.println(RED + "⚠️ Нет свободного места рядом с замком!" + RESET);
                }
                break;

            case 3: // Гном
                if (player.noHaveMoney(90)) {
                    showGold.run();
                    System.out.println(RED + "⚠️ Недостаточно золота для найма гнома!" + RESET);
                    break;
                }

                castlePos = player.getMyCastle().getPosition();
                spawnPos = field.findFreeAdjacent(castlePos);

                if (spawnPos != null) {
                    PurchasableHero dwarf = new DwarfHero(spawnPos, player.getDirection(), GREEN, player.getMyCastle(), 1, 4, 90, player);
                    field.getCell(spawnPos.x(), spawnPos.y()).addObject(dwarf);
                    field.addHeroToAll(dwarf);
                    player.spendMoney(90);
                    showGold.run();
                    System.out.println(GREEN + "⛏️ Гном готов к приключениям!" + RESET);
                } else {
                    System.out.println(RED + "⚠️ Нет свободного места рядом с замком!" + RESET);
                }
                break;


            case 4:
                System.out.println(CYAN + "↩️ Возврат в замковое меню..." + RESET);
                break;

            default:
                showGold.run();
                System.out.println(RED + "❌ Неверный выбор. Попробуйте снова." + RESET);
        }
        return choice;
    }

}
