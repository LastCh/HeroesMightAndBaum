package game.model.hero;

import game.api.Position;
import game.map.Field;
import game.model.building.onmap.Castle;
import game.model.unit.GameUnits;
import game.model.unit.Spearman;
import game.model.unit.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HeroTest {

    Field field;
    Castle castle;
    HumanHero hero;

    @BeforeEach
    void setup() {
        field = new Field(10, 10);
        castle = new Castle(new Position(1, 1), "\u001B[34;47m", 2000, field);
        hero = new HumanHero(new Position(2, 2), 5, castle, 100);
        field.getCell(2, 2).addObject(hero);
    }

    @Test
    void testAddUnitsIncreasesPower() {
        int powerBefore = hero.getPower();
        hero.addUnits(new Spearman());
        assertTrue(hero.getPower() > powerBefore);
    }

    @Test
    void testTakeDamageReducesHealth() {
        int initialHealth = hero.getHealth();
        hero.takeDamage(10);
        assertEquals(initialHealth - 10, hero.getHealth());
    }

    @Test
    void testAttackDealsDamage() {
        Castle enemyCastle = new Castle(new Position(3, 3), "\u001B[31;47m", 2000, field);
        HumanHero enemy = new HumanHero(new Position(3, 3), 5, enemyCastle, 100);
        field.getCell(3, 3).addObject(enemy);

        hero.setPower(30);
        int hpBefore = enemy.getHealth();
        hero.attack(enemy);
        assertTrue(enemy.getHealth() < hpBefore);
    }

    @Test
    void testSpendAndResetMovementPoints() {
        hero.resetMovementPoints();
        int initial = hero.getMovementPoints();
        hero.spendMovementPoints(2);
        assertEquals(initial - 2, hero.getMovementPoints());

        hero.resetMovementPoints();
        assertEquals(hero.getMovementPoints(), 5);
    }

    @Test
    void testGoldOperations() {
        hero.setGold(100);
        hero.spendMoney(30);
        assertEquals(70, hero.getGold());

        hero.addGold(50);
        assertEquals(120, hero.getGold());

        assertTrue(hero.noHaveMoney(1000));
    }

    @Test
    void testSerializeAndDeserializeUnits() {
        hero.addUnits(new Spearman());
        hero.addUnits(GameUnits.SWORDSMAN.cloneUnit());

        String serialized = hero.serializeUnits();

        ArrayList<Unit> oldUnits = hero.getUnits();
        hero.setUnits(new ArrayList<>()); // clear

        hero.deserializeUnits(serialized);
        assertEquals(oldUnits.size(), hero.getUnits().size());
        assertEquals(oldUnits.get(0).getClass(), hero.getUnits().get(0).getClass());
    }

    @Test
    void testIsInRangeWorks() {
        HumanHero enemy = new HumanHero(new Position(3, 2), 5, castle, 50);
        assertTrue(hero.isInRange(enemy, 1));
        assertFalse(hero.isInRange(enemy, 0));
    }

    @Test
    void testIsAlive() {
        hero.setHealth(1);
        assertTrue(hero.isAlive());

        hero.takeDamage(100);
        assertFalse(hero.isAlive());
    }

    @Test
    void testMovePositionUpdates() {
        hero.updatePosition(4, 4);
        assertEquals(new Position(4, 4), hero.getPosition());
    }

    @Test
    void testGetColoredSymbol() {
        String symbol = hero.getColoredSymbol();
        assertNotNull(symbol);
        assertTrue(symbol.contains("☻"));
    }
}
