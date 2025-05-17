package game.interf;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class CaveInterfaceTest {

    @Test
    void testPrintOutputsCorrectText() {
        CaveInterface ui = new CaveInterface();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        ui.print("Проверка вывода");

        assertTrue(outContent.toString().contains("Проверка вывода"));
    }

    @Test
    void testWaitMillisDoesNotThrow() {
        CaveInterface ui = new CaveInterface();
        assertDoesNotThrow(() -> ui.waitMillis(100));
    }

}
