package game.model.unit;

public abstract class Unit implements CloneableUnit {
    protected int power;
    protected int cost;

    public int getPower() {
        return power;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public Unit clone() {
        try {
            return (Unit) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Клонирование не поддерживается", e);
        }
    }
}
