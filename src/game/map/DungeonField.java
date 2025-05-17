package game.map;

import game.api.Position;
import game.model.monster.Zombie;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;


public class DungeonField extends Field {
    private final List<Zombie> zombies = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    public DungeonField(int width, int height) {
        super(width, height);
    }

    public void AddZombies() {
        for (int i = 0; i < 3; i++) {
            Random random1 = new Random();
            Random random2 = new Random();
            int min = 1;
            int max = 4;
            int randomNumber1 = random1.nextInt(max - min + 1) + min;
            int randomNumber2 = random2.nextInt(max - min + 1) + min;
            this.getCell(randomNumber1, randomNumber2).addObject(new Zombie(new Position(randomNumber1, randomNumber2), 100));
        }
    }

}
