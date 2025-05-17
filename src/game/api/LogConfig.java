package game.api;

import java.io.IOException;
import java.util.logging.*;

public class LogConfig {
    public static final Logger LOGGER = Logger.getLogger("GameLogger");

    static {
        try {
            FileHandler fileHandler = new FileHandler("game.log", true);
            fileHandler.setEncoding("UTF-8");
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);

            LOGGER.setUseParentHandlers(false); // отключаем вывод в консоль
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);

        } catch (IOException e) {
            System.err.println("❌ Ошибка инициализации логгера: " + e.getMessage());
        }
    }
}
