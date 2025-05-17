package game.api;

public abstract class FieldObject {
    protected Position position;
    protected String coloredSymbol;
    protected int priority;

    public FieldObject(Position position, String coloredSymbol, int priority) {
        this.position = position;
        this.coloredSymbol = coloredSymbol;
        this.priority = priority;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getColoredSymbol() {
        return coloredSymbol;
    }

    public int getPriority() {
        return priority;
    }

    // 👇 ОБЯЗАТЕЛЬНО ДОБАВЬ ЭТИ МЕТОДЫ
    public abstract String serialize();           // состояние объекта
    public abstract String getClassName();        // имя класса для восстановления
}
