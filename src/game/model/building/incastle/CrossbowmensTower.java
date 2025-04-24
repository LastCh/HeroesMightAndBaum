package game.model.building.incastle;

public class CrossbowmensTower extends BuildingCastle {
    private int cost = 40;
    public CrossbowmensTower() { }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public void setCost(int cost) {
        this.cost = cost;
    }
}
