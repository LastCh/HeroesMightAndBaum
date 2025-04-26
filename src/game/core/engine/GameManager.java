package game.core.engine;

import game.api.Position;
import game.map.Field;
import game.model.building.onmap.GoldCave;
import game.model.hero.HumanHero;
import game.model.hero.ComputerHero;
import game.model.building.onmap.Castle;

import java.util.Scanner;

public class GameManager {
    private final Field field;
    private final HumanHero human;
    private final ComputerHero computer;
    private final Scanner scanner = new Scanner(System.in);
    private final Castle botCastle;
    private final Castle playerCastle;

    public GameManager() {
        Generation generation = new Generation();
        this.field = generation.generateField(10, 10);

        botCastle = new Castle(new Position(8, 8), scanner, "\u001B[31;47m", 2000, field);
        playerCastle = new Castle(new Position(1, 1), scanner, "\u001B[34;47m", 2000, field);

        field.getCell(8, 8).addObject(botCastle);
        field.getCell(1, 1).addObject(playerCastle);

        this.human = new HumanHero(new Position(0, 0), 10, playerCastle, 1000);
        this.computer = new ComputerHero(new Position(9, 9), playerCastle.getPosition(), 2, botCastle, 500);
        human.setHealth(1000);
        computer.setHealth(1000);

        field.getCell(0, 0).addObject(human);
        field.getCell(9, 9).addObject(computer);
        for (int i = 0; i < 3; i++) { // 3 пещеры
            int x, y;
            do {
                x = (int) (Math.random() * field.getWidth());
                y = (int) (Math.random() * field.getHeight());
            } while (!field.getCell(x, y).isEmpty()); // ищем свободное место

            GoldCave cave = new GoldCave(new Position(x, y), 500 + i * 100);
            field.getCell(x, y).addObject(cave);
        }
    }

    public void startGame() {
        Render render = new Render(field, human, scanner, computer, botCastle, playerCastle);
        render.startGameLoop();
    }
}