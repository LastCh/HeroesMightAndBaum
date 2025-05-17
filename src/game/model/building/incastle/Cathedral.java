package game.model.building.incastle;

public class Cathedral extends BuildingCastle {
    private static final int cost = 70;
    private static final String name = "собор";

    public Cathedral() {}

    @Override
    public int getCost() {
        return cost;
    }

    public static String getName() {
        return name;
    }

    public String getNameNotStat() { return name; }
}
