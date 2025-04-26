package game.model.unit;

public class Paladin extends Unit {
    public Paladin() {
        this.power = 13;
        this.cost = 150;
    }

    @Override
    public Paladin clone() {
        return new Paladin();
    }
}
