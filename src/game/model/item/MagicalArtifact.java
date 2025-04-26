package game.model.item;

import game.model.hero.Hero;

public class MagicalArtifact extends Item{
    public MagicalArtifact(int count, Hero player) {
        super(count, player);
    }

    public void addArtifact(int quantity){
        amount+=quantity;
    }

    public int getAmount() {
        return amount;
    }

    public void spendArtifact(){
        amount--;
    }
}
