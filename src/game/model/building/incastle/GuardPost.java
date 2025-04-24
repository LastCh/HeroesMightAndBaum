package game.model.building.incastle;

public class GuardPost extends BuildingCastle {
    private int cost = 10;
    public GuardPost() {

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
