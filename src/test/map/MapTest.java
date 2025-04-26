package map;

import game.api.FieldObject;
import game.api.Position;
import game.map.*;
import game.model.hero.Hero;
import game.model.hero.HumanHero;
import game.model.building.onmap.Castle;
import game.model.monster.Zombie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class MapTest {
    private Field field;
    private DungeonField dungeon;
    private Cell cell;
    private Castle castle;

    @BeforeEach
    void setUp() {
        Scanner scanner = new Scanner(System.in);
        field = new Field(5, 5);
        dungeon = new DungeonField(5, 5);
        cell = new Cell();
        castle = new Castle(new Position(0, 0), scanner, 100, field);
    }

    // ===== Тесты для Cell =====
    @Test
    void testCellAddAndRemoveObject() {
        FieldObject dummy = new Zombie(new Position(0, 0), 100);
        assertFalse(cell.contains(dummy));

        cell.addObject(dummy);
        assertTrue(cell.contains(dummy));

        cell.removeObject(dummy);
        assertFalse(cell.contains(dummy));
    }

    @Test
    void testCellTerrainType() {
        assertEquals(TerrainType.GRASS, cell.getTerrainType());

        cell.setTerrainType(TerrainType.WATER);
        assertEquals(TerrainType.WATER, cell.getTerrainType());
    }

    @Test
    void testCellDisplaySymbol() {
        // Проверка отображения terrain
        assertTrue(cell.getDisplaySymbol().contains(",")); // GRASS символ

        // Проверка приоритета объекта над terrain
        FieldObject zombie = new Zombie(new Position(0, 0), 100);
        cell.addObject(zombie);
        assertTrue(cell.getDisplaySymbol().contains("Z")); // Символ зомби
    }

    // ===== Тесты для Field =====
    @Test
    void testFieldDimensions() {
        assertEquals(5, field.getWidth());
        assertEquals(5, field.getHeight());
    }

    @Test
    void testFieldCellAccess() {
        assertNotNull(field.getCell(0, 0));
        assertNull(field.getCell(-1, 0)); // За границей
        assertNull(field.getCell(10, 10)); // За границей
    }

    @Test
    void testFieldObjectMovement() {
        Hero hero = new HumanHero(new Position(0, 0), 1, castle, 1);
        field.getCell(0, 0).addObject(hero);

        field.moveObject(hero, 0, 0, 1, 1);
        assertNull(field.getHeroAt(new Position(0, 0)));
        assertNotNull(field.getHeroAt(new Position(1, 1)));
    }

    @Test
    void testFieldHeroDetection() {
        Hero hero = new HumanHero(new Position(2, 2),1, castle, 1);
        field.getCell(2, 2).addObject(hero);

        assertSame(hero, field.getHeroAt(new Position(2, 2)));
        assertTrue(field.getHumanHeroAt(new Position(2, 2)) instanceof HumanHero);
    }

    // ===== Тесты для DungeonField =====
    @Test
    void testDungeonZombieSpawning() {
        dungeon.AddZombies();
        int zombieCount = 0;

        // Подсчитываем зомби на карте
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                if (dungeon.getCell(x, y).getObjects().stream().anyMatch(o -> o instanceof Zombie)) {
                    zombieCount++;
                }
            }
        }

        assertEquals(3, zombieCount); // Должно быть ровно 3 зомби
    }

    @Test
    void testDungeonZombiePositions() {
        dungeon.AddZombies();

        // Проверяем, что зомби в пределах карты
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                int finalX = x;
                int finalY = y;
                dungeon.getCell(x, y).getObjects().forEach(obj -> {
                    if (obj instanceof Zombie) {
                        assertTrue(finalX >= 1 && finalX <= 4); // Проверка диапазона 1-4
                        assertTrue(finalY >= 1 && finalY <= 4);
                    }
                });
            }
        }
    }
}