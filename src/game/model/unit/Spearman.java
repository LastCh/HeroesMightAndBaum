package game.model.unit;

public class Spearman extends Unit {
    public Spearman() {
        this.power = 11;
        this.cost = 10;
    }

    @Override
    public Spearman clone() {
        return new Spearman(); // Т.к. он без состояния, просто новый объект
    }
}
