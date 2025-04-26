package game.api;
import game.model.hero.Hero;

public interface Immovable extends GameObject {

    // Взаимодействие с объектом
    void interact(Hero player);

    // Позиция на карте
    Position getPosition();

}