package game.model.building.incastle;

public abstract class BuildingCastle {
    protected static int cost;
    protected static String name;

    public BuildingCastle() {}

    public int getCost() {
        return cost;
    }

    public static String getName() {
        return name;
    }

    public String getNameNotStat() { return name; }
}
