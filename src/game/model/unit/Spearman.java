package game.model.unit;

public class Spearman extends Unit {
    public Spearman() {
        this.power = 11;
        this.cost = 10;
    }

    @Override
    public Spearman cloneUnit() {
        return new Spearman();
    }

}
