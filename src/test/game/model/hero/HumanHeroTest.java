package game.model.hero;

import game.api.Position;
import game.map.Field;
import game.model.building.onmap.Castle;
import game.model.monster.Zombie;
import game.model.unit.GameUnits;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HumanHeroTest {

    private HumanHero hero;
    private Field field;
    private Castle castle;

    @BeforeEach
    void setup() {
        field = new Field(10, 10);
        castle = new Castle(new Position(1, 1), 1000, field, "31;47");
        field.getCell(1, 1).addObject(castle);
        hero = new HumanHero(new Position(2, 2), 5, castle, 300);
        field.getCell(2, 2).addObject(hero);
    }

    @Test
    void testReceiveArtifact() {
        assertFalse(hero.hasArtifact());

        hero.receiveArtifact(2);
        assertTrue(hero.hasArtifact());
    }

    @Test
    void testUseArtifact() {
        hero.receiveArtifact(1);

        ComputerHero target = new ComputerHero(new Position(3, 2), new Position(0, 0), 2, castle, 100);
        field.getCell(3, 2).addObject(target);
        int before = target.getHealth();

        boolean used = hero.useArtifact(target);
        assertTrue(used);
        assertTrue(target.getHealth() <= 0);
    }

    @Test
    void testUseArtifactFailsWhenNone() {
        ComputerHero target = new ComputerHero(new Position(3, 2), new Position(0, 0), 2, castle, 100);
        field.getCell(3, 2).addObject(target);

        boolean used = hero.useArtifact(target);
        assertFalse(used);
        assertEquals(100, target.getHealth());
    }

    @Test
    void testMoveInCaveKillsZombie() {
        // Это чисто логический тест: создаём зомби в точке (3,3)
        Zombie zombie = new Zombie(new Position(3, 3), 10);
        Field cave = new Field(5, 5);
        cave.getCell(3, 3).addObject(zombie);

        hero.setPosition(new Position(2, 3));
        cave.getCell(2, 3).addObject(hero);

        hero.moveInCave(1, 0, cave);

        assertTrue(zombie.isDead());
        assertEquals(new Position(3, 3), hero.getPosition());
    }

    @Test
    void testSerializationAndDeserialization() {
        hero.receiveArtifact(2);
        hero.addUnits(GameUnits.SPEARMAN.cloneUnit());

        String data = hero.serialize();
        HumanHero restored = HumanHero.deserialize(data, field, castle);

        assertEquals(hero.getHealth(), restored.getHealth());
        assertEquals(hero.getGold(), restored.getGold());
        assertEquals(hero.getPosition(), restored.getPosition());
        assertEquals(hero.getUnits().size(), restored.getUnits().size());
        assertEquals(hero.hasArtifact(), restored.hasArtifact());
    }
}
