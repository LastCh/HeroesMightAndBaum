package game.model.hero;

import game.map.Field;
import game.model.hero.HumanHero;
import game.model.unit.GameUnits;
import game.api.Position;
import game.model.building.onmap.Castle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HeroTest {

    static Field field;

    @BeforeAll
    static void beforeAllTests() {
        field = new Field(5,5);
    }

    @Test
    void testAddUnitsIncreasesPower() {
        Castle castle = new Castle(new Position(0, 0), "\u001B[31;47m", 1, field);
        HumanHero hero = new HumanHero(new Position(1, 1), 10, castle,  100);

        int powerBefore = hero.getPower();
        hero.addUnits(GameUnits.SPEARMAN.clone());
        assertTrue(hero.getPower() > powerBefore);
    }
}