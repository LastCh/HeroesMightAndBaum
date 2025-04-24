package game.model.building.incastle;

public class Stable extends BuildingCastle {
    private int cost = 20;
    public Stable(){
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
