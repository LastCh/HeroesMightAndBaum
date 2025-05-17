package game.model.monster;

import game.api.Position;
import game.model.hero.Hero;

public class Zombie extends Hero {
    public Zombie(Position position, int health) {
        super(position, "\u001B[32mZ\u001B[0m", null, 0, 1, 0);
        this.setHealth(health);
        this.power = 10;
    }

    public void attack(Hero target) {
        target.takeDamage(this.power);
        System.out.println("Зомби атаковал вас! Ваше здоровье: " + target.getHealth());
    }

    @Override
    public void takeDamage(int amount) {
        super.takeDamage(amount);
        if (getHealth() <= 0) {
            System.out.println("Зомби уничтожен!");
        }
    }

    public boolean isDead() {
        return getHealth() <= 0;
    }

    @Override
    public String serialize() {
        return position.x() + "," + position.y() + ";" + getHealth();
    }

    public static Zombie deserialize(String data) {
        String[] parts = data.split(";");
        String[] coords = parts[0].split(",");
        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);
        int health = Integer.parseInt(parts[1]);

        return new Zombie(new Position(x, y), health);
    }


    @Override
    public String getClassName() {
        return "Zombie";
    }
}
