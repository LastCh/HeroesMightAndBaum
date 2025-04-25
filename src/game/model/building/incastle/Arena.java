package game.model.building.incastle;

public class Arena extends BuildingCastle {
    private static final int cost = 60;
    private static final String name = "арена";

    public Arena() {
    }

    @Override
    public int getCost() {
        return cost;
    }

    public static String getName() {
        return name;
    }
}
