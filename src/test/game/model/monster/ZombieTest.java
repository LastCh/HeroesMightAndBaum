package game.model.monster;

import game.api.Position;
import game.map.Field;
import game.model.building.onmap.Castle;
import game.model.hero.HumanHero;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ZombieTest {

    Zombie zombie;
    HumanHero target;
    Field field;
    Castle dummyCastle;

    @BeforeEach
    void setup() {
        field = new Field(5, 5);
        dummyCastle = new Castle(new Position(0, 0), 100, field, "31;47");
        zombie = new Zombie(new Position(2, 2), 50);
        target = new HumanHero(new Position(2, 3), 5, dummyCastle, 100);
    }

    @Test
    void testZombieInitialState() {
        assertEquals(50, zombie.getHealth());
        assertEquals(10, zombie.getPower());
        assertFalse(zombie.isDead());
    }

    @Test
    void testZombieAttack() {
        int targetHpBefore = target.getHealth();
        zombie.attack(target);
        assertEquals(targetHpBefore - 10, target.getHealth());
    }

    @Test
    void testTakeDamageAndDeath() {
        zombie.takeDamage(30);
        assertEquals(20, zombie.getHealth());
        assertFalse(zombie.isDead());

        zombie.takeDamage(25);
        assertTrue(zombie.isDead());
        assertEquals(0, zombie.getHealth());
    }

    @Test
    void testSerialization() {
        String serialized = zombie.serialize();
        assertEquals("2,2;50", serialized);
    }

    @Test
    void testDeserialization() {
        String data = "3,1;40";
        Zombie deserialized = Zombie.deserialize(data);

        assertEquals(40, deserialized.getHealth());
        assertEquals(new Position(3, 1), deserialized.getPosition());
    }

    @Test
    void testGetClassNameIsImplemented() {
        assertNotNull(zombie.getClassName());
    }
}
