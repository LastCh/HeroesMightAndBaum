package game.model.building.incastle;

public class GuardPost extends BuildingCastle {
    private static final int cost = 30;
    private static final String name = "сторожевой пост";

    public GuardPost() { }

    @Override
    public int getCost() {
        return cost;
    }

    public static String getName() {
        return name;
    }
}
