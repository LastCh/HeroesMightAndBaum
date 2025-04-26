package game.model.item;

import game.model.hero.Hero;

public abstract class Item {
    protected int amount;
    protected Hero affiliation;

    public Item(int count, Hero player) {
        this.amount = count;
        this.affiliation = player;
    }
}
