package game.model.building.incastle;

public class Tavern extends BuildingCastle {
    private static final int cost = 10;
    private static final String name = "таверна";

    public Tavern() {}

    @Override
    public int getCost() {
        return cost;
    }

    public static String getName() {
        return name;
    }
}
