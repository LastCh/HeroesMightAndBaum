package game.model.unit;

public class Cavalryman extends Unit {
    public Cavalryman() {
        this.power = 14;
        this.cost = 40;
    }

    @Override
    public Cavalryman cloneUnit() {
        return new Cavalryman();
    }

}
