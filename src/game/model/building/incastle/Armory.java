package game.model.building.incastle;

public class Armory extends BuildingCastle {
    private static final int cost = 50;
    private static final String name = "оружейная";

    public Armory() {}

    @Override
    public int getCost() {
        return cost;
    }

    public static String getName() {
        return name;
    }
}
