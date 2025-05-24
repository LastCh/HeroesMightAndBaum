package game.model.building.onmap;

import game.api.Position;
import game.interf.EnterpriseMenu;
import game.map.Field;
import game.model.hero.HumanHero;
import game.service.ServiceType;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import static game.api.LogConfig.NPC_LOGGER;

public class Barbershop extends Enterprise {
    public static final ServiceType SIMPLE_CUT = new ServiceType(
            "Простая стрижка", 1000, 10, hero -> {}
    );
    public static final ServiceType FASHION_CUT = new ServiceType(
            "Модная стрижка", 3000, 40, visitor -> {
        if (visitor instanceof HumanHero hero) {
            hero.reduceCastleCaptureTime();
        } else {
            NPC_LOGGER.info("[NPC] попытался пользоваться сервисом, которым ему не дают(");
        }
    }
    );

    public Barbershop(Position position, Field field, String color) {
        super(position, color+ "\u2702" + "\u001B[0m", 2, field,
                5, 10, 10, 0.3);
    }

    public static Barbershop deserialize(String data, Field field) {
        String[] parts = data.split(";", -1);
        String[] coords = parts[0].split(",");
        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);
        String color = parts[1];
        Barbershop b = new Barbershop(new Position(x, y), field, field.getCell(x,y).getTerrainType().getColoredBackground());
        b.setColoredSymbol(color);
        return b;
    }

    @Override
    public String getClassName() {
        return "Barbershop";
    }

    @Override
    public int displayMenu() {
        EnterpriseMenu.serveBarbershop();
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.print("Введите номер услуги: ");
            choice = scanner.nextInt();
        } while (choice < 1 || choice > 3); // 3 — выход
        return choice;
    }

    @Override
    protected ServiceType randomService() {
        return ThreadLocalRandom.current().nextBoolean()
                ? SIMPLE_CUT : FASHION_CUT;
    }

}
