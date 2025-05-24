package game.model.unit;

public class Unit {
    protected int power;
    protected int cost;

    public int getPower() {
        return power;
    }

    public int getCost() {
        return cost;
    }

    public void setPower(int pow){
        power = pow;
    }

    public Unit cloneUnit() {
        return new Unit();
    }
}
