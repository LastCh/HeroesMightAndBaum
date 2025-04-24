package game.model.building.incastle;

public abstract class BuildingCastle {
    protected int cost;

    public BuildingCastle() { }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost1) {
        this.cost = cost1;
    }
}
