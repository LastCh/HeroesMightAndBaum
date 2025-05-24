package game.api;

import java.io.IOException;
import java.util.logging.*;

public class LogConfig {
    public static Logger LOGGER = Logger.getLogger("GameLogger");

    public LogConfig(){
        System.out.println("CreateLog");
    }

    static {
        System.out.println("Log");
        try {
            FileHandler fileHandler = new FileHandler("game.log", true);
            fileHandler.setEncoding("UTF-8");
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);

            LOGGER.setUseParentHandlers(false);
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);

        } catch (IOException e) {
            System.err.println("❌ Ошибка инициализации логгера: " + e.getMessage());
        }
    }
}
