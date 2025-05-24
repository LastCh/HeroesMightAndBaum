package game.model.hero;

import game.api.Position;
import game.map.Field;
import game.model.building.onmap.Castle;
import game.model.unit.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PurchasableHeroTest {

    private Field field;
    private Castle playerCastle;
    private Castle enemyCastle;
    private ElfHero elf;
    private HumanHero owner;

    @BeforeEach
    void setup() {
        field = new Field(10, 10);

        playerCastle = new Castle(new Position(1, 1), 1000, field,"31;47");
        enemyCastle = new Castle(new Position(8, 8), 1000, field,"31;47");
        field.getCell(1, 1).addObject(playerCastle);
        field.getCell(8, 8).addObject(enemyCastle);

        owner = new HumanHero(new Position(0, 0), 10, playerCastle, 100);
        elf = new ElfHero(new Position(2, 2), playerCastle, 5, 2, 150, owner);
        field.getCell(2, 2).addObject(elf);
        field.getCell(0, 0).addObject(owner);
    }

    @Test
    void testAddUnitsOnMakeMove() {
        int unitCountBefore = elf.getUnits().size();
        HumanHero enemy = new HumanHero(new Position(5, 5), 5, enemyCastle, 100);
        field.getCell(5, 5).addObject(enemy);
        elf.makeMove(field);
        int unitCountAfter = elf.getUnits().size();

        assertTrue(unitCountAfter > unitCountBefore, "Юнит должен был добавиться при makeMove");
    }

    @Test
    void testMoveTowardsEnemyCastleEventuallyMoves() {
        Field field = new Field(5, 5);

        Castle enemyCastle = new Castle(new Position(4, 4), 2000, field, "31;47");
        field.getCell(4, 4).addObject(enemyCastle);

        Castle myCastle = new Castle(new Position(0, 0), 2000, field, "31;47");
        field.getCell(0, 0).addObject(myCastle);

        ComputerHero owner = new ComputerHero(new Position(1, 1), enemyCastle.getPosition(), 10, myCastle, 1000);
        field.getCell(1, 1).addObject(owner);

        HumanHero enemy = new HumanHero(new Position(4, 3), 10, enemyCastle, 100);
        field.getCell(4, 3).addObject(enemy);

        ElfHero elf = new ElfHero(new Position(2, 2), myCastle, 5, 1, 50, owner);
        elf.resetMovementPoints();
        field.getCell(2, 2).addObject(elf);

        Position before = elf.getPosition();

        elf.makeMove(field);

        Position after = elf.getPosition();

        assertNotEquals(before, after, "Герой должен был сдвинуться к замку");
    }




    @Test
    void testAttackEnemyHeroInRange() {
        HumanHero enemy = new HumanHero(new Position(3, 3), 10, enemyCastle, 100);
        field.getCell(3, 3).addObject(enemy);

        int hpBefore = enemy.getHealth();
        elf.resetMovementPoints();
        elf.makeMove(field);
        int hpAfter = enemy.getHealth();

        assertTrue(hpAfter < hpBefore, "Здоровье врага должно уменьшиться");
    }
}
