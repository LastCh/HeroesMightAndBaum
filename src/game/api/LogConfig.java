package game.api;

import java.io.IOException;
import java.util.logging.*;

public class LogConfig {
    public static Logger LOGGER = Logger.getLogger("GameLogger");
    public static final Logger NPC_LOGGER = Logger.getLogger("NpcLogger");

    public LogConfig(){
        System.out.println("CreateLog");
    }

    static {
        try {
            FileHandler fileHandler = new FileHandler("game.log", true);
            fileHandler.setEncoding("UTF-8");
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);

            LOGGER.setUseParentHandlers(false);
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);


            FileHandler npcHandler = new FileHandler("npc.log", false);
            npcHandler.setEncoding("UTF-8");
            npcHandler.setFormatter(new SimpleFormatter());
            npcHandler.setLevel(Level.INFO);

            NPC_LOGGER.setUseParentHandlers(false);
            NPC_LOGGER.addHandler(npcHandler);
            NPC_LOGGER.setLevel(Level.INFO);

        } catch (IOException e) {
            System.err.println("❌ Ошибка инициализации логгера: " + e.getMessage());
        }
    }
}
