package game.core.engine;

import game.api.Position;
import game.interf.MenuManager;
import game.map.Field;
import game.model.building.onmap.Castle;
import game.model.building.onmap.GoldCave;
import game.model.hero.HumanHero;
import game.model.hero.ComputerHero;

import java.util.Scanner;

public class GameManager {
    private Field field;
    private HumanHero human;
    private ComputerHero computer;
    private final Scanner scanner = new Scanner(System.in);
    private Castle botCastle;
    private Castle playerCastle;
    private final MenuManager menu = new MenuManager();
    private GameSave gameSave;
    private String playerName;
    private String gameName = "name";

    public GameManager(String playerN){
        playerName = playerN;
    }

    public void startGame() {
        Generation generation = new Generation();
        field = generation.generateField(10, 10);

        botCastle = new Castle(new Position(8, 8), "\u001B[31;47m", 2000, field);
        playerCastle = new Castle(new Position(1, 1),  "\u001B[34;47m", 2000, field);
        field.getCell(8, 8).addObject(botCastle);
        field.getCell(1, 1).addObject(playerCastle);

        human = new HumanHero(new Position(0, 0), 10, playerCastle, 1000);
        computer = new ComputerHero(new Position(9, 9), playerCastle.getPosition(), 2, botCastle, 500);
        field.getCell(0, 0).addObject(human);
        field.getCell(9, 9).addObject(computer);

        for (int i = 0; i < 3; i++) {
            int x, y;
            do {
                x = (int) (Math.random() * field.getWidth());
                y = (int) (Math.random() * field.getHeight());
            } while (!field.getCell(x, y).isEmpty());
            GoldCave cave = new GoldCave(new Position(x, y), 500 + i * 100);
            field.getCell(x, y).addObject(cave);
        }

        gameSave = new GameSave(computer, human, playerCastle, botCastle, field, gameName, playerName);

        Render render = new Render(field, human, scanner, computer, playerCastle, botCastle);
        clearConsole();
        render.startGameLoop(this);
    }

    public void loadGame(GameSave gSave) {
        field = gSave.field;

        botCastle = gSave.castleComputer;
        botCastle.setColoredSymbol("\u001B[31;47m"+"♜♜" + "\u001B[0m");
        playerCastle = gSave.castlePlayer;
        playerCastle.setColoredSymbol("\u001B[34;47m"+"♜♜" + "\u001B[0m");

        human = gSave.humanHero;
        computer = gSave.computerHero;

        gameSave = new GameSave(computer, human, playerCastle, botCastle, field, gameName, playerName);

        Render render = new Render(field, human, scanner, computer, playerCastle, botCastle);
        clearConsole();
        render.startGameLoop(this);
    }

    public void clearConsole() {
        for (int i = 0; i < 50; i++) System.out.println();
    }

    public GameSave getGameSave() { return gameSave; }

    public void setPlayerName(String pName) { playerName = pName; }

    public void setGameName(String gName) {
        this.gameName = gName;
    }
}
