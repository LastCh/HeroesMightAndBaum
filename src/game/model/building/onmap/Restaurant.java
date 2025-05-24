package game.model.building.onmap;

import game.api.Position;
import game.interf.EnterpriseMenu;
import game.map.Field;
import game.service.ServiceType;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Restaurant extends Enterprise {
    public static final ServiceType SNACK = new ServiceType(
            "Просто перекус", 1500, 20, hero -> hero.boostMovement(2)
    );
    public static final ServiceType LUNCH = new ServiceType(
            "Плотный обед", 3000, 30, hero -> hero.boostMovement(3)
    );

    public Restaurant(Position position, Field field, String color) {
        super(position, color+ "\uD83C\uDF7D" + "\u001B[0m", 2, field,
                10, 14, 10, 0.2);
    }
    @Override
    public String getClassName() {
        return "Restaurant";
    }

    public static Restaurant deserialize(String data, Field field) {
        String[] parts = data.split(";", -1);
        String[] coords = parts[0].split(",");
        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);
        String color = parts[1];
        Restaurant r = new Restaurant(new Position(x, y), field, field.getCell(x,y).getTerrainType().getColoredBackground());
        r.setColoredSymbol(color);
        return r;
    }

    @Override
    public int displayMenu() {
        EnterpriseMenu.serveRestaurant();
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
                ? SNACK : LUNCH;
    }
}
