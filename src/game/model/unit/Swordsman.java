package game.model.unit;

public class Swordsman extends Unit {
    public Swordsman() {
        this.power = 11;
        this.cost = 30;
    }

    @Override
    public Swordsman clone() {
        return new Swordsman();
    }
}
