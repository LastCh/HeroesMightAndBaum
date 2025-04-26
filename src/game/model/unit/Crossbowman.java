package game.model.unit;

public class Crossbowman extends Unit {
    public Crossbowman() {
        this.power = 12;
        this.cost = 20;
    }

    @Override
    public Crossbowman clone() {
        return new Crossbowman();
    }
}
