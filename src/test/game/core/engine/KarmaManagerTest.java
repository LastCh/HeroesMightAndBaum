package game.core.engine;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class KarmaManagerTest {

    @TempDir Path dir;

    @BeforeEach
    void useTempFile() {
        KarmaManager._setPlayersFile(dir.resolve("players.txt").toFile());
    }

    @Test
    void updateNewPlayer_createsRecord() {
        KarmaManager.updatePlayerKarma("Alice", 1.5);
        assertEquals(1.5, KarmaManager.loadPlayerKarma("Alice"));
    }

    @Test
    void updateExistingPlayer_addsDelta() {
        KarmaManager.updatePlayerKarma("Bob", 1.0);
        KarmaManager.updatePlayerKarma("Bob", 0.7);
        assertEquals(1.7, KarmaManager.loadPlayerKarma("Bob"));
    }

    @Test
    void karmaIsClampedToMaxFive() {
        KarmaManager.updatePlayerKarma("Carol", 10);
        assertEquals(5.0, KarmaManager.loadPlayerKarma("Carol"));
    }

    @Test
    void karmaIsClampedToMinMinusFive() {
        KarmaManager.updatePlayerKarma("Dave", 0);
        KarmaManager.updatePlayerKarma("Dave", -10);
        assertEquals(-5.0, KarmaManager.loadPlayerKarma("Dave"));
    }


    @Test
    void loadUnknownPlayer_returnsZero() {
        assertEquals(0.0, KarmaManager.loadPlayerKarma("Nobody"));
    }
}
