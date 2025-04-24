package game.model.building.incastle;

public class Armory extends BuildingCastle {
    private int cost = 50;
    public Armory() {
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
