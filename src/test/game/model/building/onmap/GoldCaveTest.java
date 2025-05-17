package game.model.building.onmap;

import game.api.Position;
import game.map.Field;
import game.model.hero.Hero;
import game.model.hero.HumanHero;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoldCaveTest {

    GoldCave cave;
    Field field;

    @BeforeEach
    void setup() {
        cave = new GoldCave(new Position(3, 3), 500);
        field = new Field(10, 10);
    }

    @Test
    void testInitialization() {
        assertEquals(500, cave.getGoldAmount());
        assertEquals(new Position(3, 3), cave.getPosition());
        assertEquals("GoldCave", cave.getClassName());
        assertFalse(cave.isInCave());
    }

    @Test
    void testSerializationAndDeserialization() {
        String serialized = cave.serialize();
        assertEquals("3,3;500", serialized);

        GoldCave deserialized = GoldCave.deserialize(serialized);
        assertEquals(cave.getGoldAmount(), deserialized.getGoldAmount());
        assertEquals(cave.getPosition(), deserialized.getPosition());
    }


}
