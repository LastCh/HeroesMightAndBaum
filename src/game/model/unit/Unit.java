package game.model.unit;

public abstract class Unit {
    protected int power;
    protected int cost;

    public int getPower() {
        return power;
    }

    public int getCost() {
        return cost;
    }

    public Unit clone() {
        try {
            return (Unit) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Клонирование не поддерживается", e);
        }
    }
}
