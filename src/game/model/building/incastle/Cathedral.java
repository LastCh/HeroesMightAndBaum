package game.model.building.incastle;

public class Cathedral extends BuildingCastle {
    private int cost = 70;
    public Cathedral() {
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public void setCost(int cost) {
        this.cost = cost;
    }
}
