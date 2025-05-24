package game.model.hero;

import game.api.Position;
import game.map.Field;
import game.model.building.incastle.GameBuildings;
import game.model.building.onmap.Castle;
import game.model.unit.GameUnits;
import game.model.unit.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComputerHeroTest {

    private Field field;
    private Castle playerCastle;
    private Castle aiCastle;
    private ComputerHero aiHero;

    @BeforeEach
    void setUp() {
        field = new Field(10, 10);
        playerCastle = new Castle(new Position(1, 1), "\u001B[34;47m", 2000, field);
        aiCastle = new Castle(new Position(8, 8), "\u001B[31;47m", 2000, field);
        field.getCell(1, 1).addObject(playerCastle);
        field.getCell(8, 8).addObject(aiCastle);

        aiHero = new ComputerHero(new Position(9, 9), playerCastle.getPosition(), 5, aiCastle, 500);
        field.getCell(9, 9).addObject(aiHero);
    }

    @Test
    void testInitialState() {
        assertEquals(500, aiHero.getGold());
        assertEquals(9, aiHero.getPosition().x());
        assertTrue(aiHero.isAlive());
    }

    @Test
    void testAutoBuyStableAndFirstSpearman() {
        aiHero.addGold(1000);
        aiHero.resetMovementPoints();

        // Выполняем покупку
        aiHero.makeMove(field); // turnCounter = 1
        aiHero.makeMove(field); // turnCounter = 2


        assertTrue(aiCastle.contains(GameBuildings.STABLE));
        List<Unit> units = aiHero.getUnits();
        assertFalse(units.isEmpty());
        assertTrue(units.get(0) instanceof Unit);
    }

    @Test
    void testAttackCastleIfInRange() {
        aiHero.setPower(100);
        aiHero.updatePosition(2, 2);  // вблизи playerCastle (1,1)
        field.getCell(2, 2).addObject(aiHero);

        int hpBefore = playerCastle.getHealth();
        aiHero.makeMove(field);
        assertTrue(playerCastle.getHealth() < hpBefore);
    }

    @Test
    void testEnhanceRadiusIncreasesIfEnoughCrossbowmen() {
        aiHero.setRadius(1);
        aiHero.setUnits(new java.util.ArrayList<>(List.of(
                GameUnits.CROSSBOWMAN.cloneUnit(),
                GameUnits.CROSSBOWMAN.cloneUnit(),
                GameUnits.CROSSBOWMAN.cloneUnit()
        )));
        aiHero.enhanceRadius(aiHero.getUnits());
        assertEquals(2, aiHero.getRadius());
    }

    @Test
    void testSerializeAndDeserialize() {
        aiHero.addUnits(GameUnits.SPEARMAN.cloneUnit());
        String serialized = aiHero.serialize();

        ComputerHero copy = ComputerHero.deserialize(serialized, field, aiCastle);
        assertEquals(aiHero.getPower(), copy.getPower());
        assertEquals(aiHero.getGold(), copy.getGold());
        assertEquals(aiHero.getHealth(), copy.getHealth());
        assertEquals(aiHero.getUnits().size(), copy.getUnits().size());
    }

    @Test
    void testMakeMoveWhenDeadTriggersRevive() {
        aiHero.setHealth(0);
        aiHero.makeMove(field);
        assertEquals(new Position(9, 9), aiHero.getPosition());
        assertTrue(aiHero.getUnits().isEmpty());
    }
}
