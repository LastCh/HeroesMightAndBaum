package game.model.building.incastle;

public class Tavern extends BuildingCastle {
    private static final int cost = 10;

    public Tavern() {}

    @Override
    public int getCost() {
        return cost;
    }
}
