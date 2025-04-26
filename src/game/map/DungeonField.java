package game.map;

import game.api.Position;
import game.model.hero.Hero;
import game.model.monster.Zombie;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DungeonField {
    private final List<Zombie> zombies = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    public DungeonField() {
        // Простая генерация зомби
        for (int i = 0; i < 3; i++) {
            zombies.add(new Zombie(new Position(i, 0), 100));

        }
    }

    public boolean enter(Hero hero) {
        while (!zombies.isEmpty() && hero.isAlive()) {
            Zombie target = zombies.get(0);
            System.out.println("На вас нападает зомби с " + target.getHealth() + " HP!");

            System.out.println("Нажмите Enter чтобы атаковать...");
            scanner.nextLine();

            hero.attack(target);
            if (!target.isAlive()) {
                zombies.remove(target);
                System.out.println("Зомби побежден!");
            } else {
                target.attack(hero);
                System.out.println("Зомби атакует в ответ!");
            }
        }

        return hero.isAlive();
    }
}
