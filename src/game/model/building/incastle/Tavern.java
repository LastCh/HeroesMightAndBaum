package game.model.building.incastle;

public class Tavern extends BuildingCastle {
    private int cost = 10;
    public Tavern() {}

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public void setCost(int cost) {
        this.cost = cost;
    }
}
