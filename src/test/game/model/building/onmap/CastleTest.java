package game.model.building.onmap;

import game.api.Position;
import game.map.Field;
import game.model.building.incastle.GameBuildings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CastleTest {

    private Castle castle;
    private Field field;

    @BeforeEach
    void setup() {
        field = new Field(10, 10);
        castle = new Castle(new Position(2, 2), "\u001B[34;47m", 1000, field);
    }

    @Test
    void testInitialHealth() {
        assertEquals(1000, castle.getHealth());
        assertFalse(castle.isDestroyed());
    }

    @Test
    void testTakeDamageReducesHealth() {
        castle.takeDamage(300);
        assertEquals(700, castle.getHealth());

        castle.takeDamage(800); // превышает оставшееся
        assertEquals(0, castle.getHealth());
        assertTrue(castle.isDestroyed());
    }

    @Test
    void testAddAndContainsBuilding() {
        assertFalse(castle.contains(GameBuildings.STABLE));
        castle.addBuilding(GameBuildings.STABLE);
        assertTrue(castle.contains(GameBuildings.STABLE));
    }

    @Test
    void testSerializeAndDeserialize() {
        String serialized = castle.serialize();
        Castle deserialized = Castle.deserialize(serialized, field);

        assertEquals(castle.getHealth(), deserialized.getHealth());
        assertEquals(castle.getPosition(), deserialized.getPosition());
        assertEquals(castle.getColoredSymbol(), deserialized.getColoredSymbol());
    }

    @Test
    void testSetColoredSymbol() {
        castle.setColoredSymbol("🏰");
        assertEquals("🏰", castle.getColoredSymbol());
    }

    @Test
    void testGetFieldReturnsSame() {
        assertEquals(field, castle.getField());
    }
}
