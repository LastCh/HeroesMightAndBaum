package game.model.building.incastle;

public class CrossbowmensTower extends BuildingCastle {
    private static final int cost = 40;
    private static final String name = "башня арбалетчиков";

    public CrossbowmensTower() {}

    @Override
    public int getCost() {
        return cost;
    }

    public static String getName() {
        return name;
    }

    public String getNameNotStat() { return name; }
}
