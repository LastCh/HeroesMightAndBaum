package game.api;

import game.map.Field;

public interface Movable {
    // Перемещение объекта
    void move(int dx, int dy, Field field);

    // Изменение направления
    void turn(Direction direction);

    // Получение текущей позиции
    Position getPosition();

    // Получение текущего направления
    Direction getDirection();

}