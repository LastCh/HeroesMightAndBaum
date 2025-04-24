package game.model.building.incastle;

public class Arena extends BuildingCastle {
    private int cost = 60;
    public Arena() {
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
