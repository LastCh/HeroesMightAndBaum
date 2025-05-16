package game.model.monster;

import game.api.Position;
import game.model.hero.Hero;

public class Zombie extends Hero {
    private boolean dead = false;

    public Zombie(Position position, int health) {
        // Зомби без замка, без владельца, без команды
        super(position, "\u001B[32mZ\u001B[0m", null, 0, 0, 0);
        this.setHealth(health);
        this.power = 10;
    }

    public void makeMove() {
    }


    @Override
    public void takeDamage(int amount) {
        this.setHealth(getHealth() - amount);
        if (getHealth() <= 0) {
            dead = true;
        }
    }

    public boolean isDead() {
        return dead;
    }
}
