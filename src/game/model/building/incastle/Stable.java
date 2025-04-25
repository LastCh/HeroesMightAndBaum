package game.model.building.incastle;

public class Stable extends BuildingCastle {
    private static final int cost = 20;
    private static final String name = "конюшня";

    public Stable(){ }

    @Override
    public int getCost() {
        return cost;
    }

    public static String getName() {
        return name;
    }
}
