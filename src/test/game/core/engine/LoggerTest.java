package game.core.engine;

import game.api.LogConfig;
import org.junit.jupiter.api.*;
import java.io.*;
import java.util.logging.*;

import static org.junit.jupiter.api.Assertions.*;

class LoggerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private final ByteArrayOutputStream logContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));

        // Перенаправляем логгер в ByteArray
        Logger logger = LogConfig.LOGGER;
        logger.setUseParentHandlers(false);
        for (Handler handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }

        StreamHandler testHandler = new StreamHandler(logContent, new SimpleFormatter());
        testHandler.setLevel(Level.ALL);
        logger.addHandler(testHandler);
        logger.setLevel(Level.ALL);
    }

    @Test
    void testLogging() {
        Logger logger = LogConfig.LOGGER;

        logger.info("Test INFO message");
        logger.warning("Test WARNING message");
        logger.severe("Test ERROR message");

        for (Handler h : logger.getHandlers()) {
            h.flush();
        }

        String logs = logContent.toString();
        assertTrue(logs.contains("INFO"), "INFO log should be present");
        assertTrue(logs.contains("WARNING"), "WARNING log should be present");
        assertTrue(logs.contains("SEVERE"), "ERROR log should be present");
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }
}

