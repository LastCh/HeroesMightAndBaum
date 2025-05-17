package game.map;

import game.api.Position;
import game.model.hero.Hero;
import game.model.monster.Zombie;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DungeonField extends Field {
    private final List<Zombie> zombies = new ArrayList<>();
    private final Random random = new Random();

    public DungeonField(int width, int height) {
        super(width, height);
    }

    // Генерация от 2 до 4 зомби
    public int addZombies() {
        int zombiesCount = random.nextInt(3) + 2; // От 2 до 4
        zombies.clear();

        int spawned = 0;
        while (spawned < zombiesCount) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            if (getCell(x, y).isEmpty()) {
                Zombie zombie = new Zombie(new Position(x, y), 100);
                zombies.add(zombie);
                getCell(x, y).addObject(zombie);
                spawned++;
            }
        }

        return zombiesCount; // Возвращает количество зомби для артефактов
    }

    public List<Zombie> getZombies() {
        return zombies;
    }

    // Зомби делают ход (движение и атака, если герой рядом)
    public void zombiesTurn(Hero player) {
        for (Zombie zombie : zombies) {
            if (zombie.isDead()) continue;

            Position playerPos = player.getPosition();
            Position zombiePos = zombie.getPosition();

            int dx = Integer.compare(playerPos.x(), zombiePos.x());
            int dy = Integer.compare(playerPos.y(), zombiePos.y());

            Position newPos = new Position(zombiePos.x() + dx, zombiePos.y() + dy);

            if (playerPos.equals(newPos)) {
                zombie.attack(player);
            } else if (isValidPosition(newPos) && getCell(newPos.x(), newPos.y()).isEmpty()) {
                moveObject(zombie, zombiePos.x(), zombiePos.y(), newPos.x(), newPos.y());
                zombie.setPosition(newPos);
            }
        }
    }

    private boolean isValidPosition(Position pos) {
        return pos.x() >= 0 && pos.x() < width && pos.y() >= 0 && pos.y() < height;
    }
}
