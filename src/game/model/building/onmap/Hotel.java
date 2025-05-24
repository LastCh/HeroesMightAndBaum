package game.model.building.onmap;

import game.api.Position;
import game.interf.EnterpriseMenu;
import game.map.Field;
import game.service.ServiceType;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;


public class Hotel extends Enterprise {
    public static final ServiceType SHORT_REST = new ServiceType(
            "Короткий отдых", 10000, 50, hero -> hero.healUnits(2)
    );
    public static final ServiceType LONG_REST = new ServiceType(
            "Длинный отдых", 30000, 100, hero -> hero.healUnits(3)
    );

    public Hotel(Position position, Field field, String color) {
        super(position,  color + "\uD83C\uDFE0" + "\u001B[0m", 5, field,
                4, 5, 15, 0.15);
    }

    public static Hotel deserialize(String data, Field field) {
        String[] parts = data.split(";", -1);

        String[] coords = parts[0].split(",");
        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);
        String color = parts[1]; // цветовая строка
        Hotel c = new Hotel(new Position(x, y), field, field.getCell(x,y).getTerrainType().getColoredBackground());
        c.setColoredSymbol(color);

        return c;
    }

    @Override
    public String getClassName() {
        return "Hotel";
    }

    @Override
    public int displayMenu() {
        EnterpriseMenu.serveHotel();
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
                ? SHORT_REST : LONG_REST;
    }

}
