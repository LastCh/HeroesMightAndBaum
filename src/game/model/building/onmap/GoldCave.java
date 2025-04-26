package game.model.building.onmap;

import game.api.FieldObject;
import game.api.Immovable;
import game.api.Position;
import game.model.hero.Hero;

public class GoldCave extends FieldObject implements Immovable {
    private final int goldAmount;

    public GoldCave(Position position, int goldAmount) {
        super(position, "\u001B[33m$\u001B[0m", 4); // Желтый символ золота
        this.goldAmount = goldAmount;
    }

    @Override
    public void interact(Hero player) {
        System.out.println("Игрок нашел " + goldAmount + " золота в пещере!");
        // Логика добавления золота игроку
        // Например, player.addGold(goldAmount);
    }
}