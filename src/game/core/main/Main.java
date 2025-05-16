package game.core.main;

import game.core.engine.Render;
import game.interf.MenuManager;

public class Main {
    public static void main(String[] args) {
        MenuManager menuManager = new MenuManager();
        menuManager.run();
    }
}

