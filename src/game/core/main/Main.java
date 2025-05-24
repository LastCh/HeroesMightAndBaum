package game.core.main;

import game.interf.MenuManager;

import static game.api.LogConfig.LOGGER;


public class Main {
    public static void main(String[] args) {
        LOGGER.info("Начало генерации карты");
        MenuManager menuManager = new MenuManager();
        menuManager.run();
    }
}

