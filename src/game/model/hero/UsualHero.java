package game.model.hero;

import game.api.Direction;
import game.api.Position;
import game.map.Field;
import game.model.building.onmap.Castle;

public class UsualHero extends Hero{

    public UsualHero(Position startPosition, Direction startDirection, String colorCode, Castle castle, int priority, int points, int gold) {
        super(startPosition, startDirection, colorCode, castle, priority, points, gold);
    }

    @Override
    public void makeMove(Field field) {

    }

    @Override
    public void interact(Hero player) {

    }
}
