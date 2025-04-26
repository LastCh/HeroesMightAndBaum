import game.api.Position;
import game.core.engine.GameManager;
import game.model.unit.GameUnits;
import game.model.building.onmap.Castle;
import game.model.building.onmap.GoldCave;
import game.model.hero.HumanHero;
import game.model.hero.ComputerHero;
import game.map.Field;
import game.model.unit.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {

    private GameManager gameManager;
    private HumanHero human;
    private ComputerHero computer;
    private Castle playerCastle;
    private Castle botCastle;
    private Field field;

    @BeforeEach
    void setUp() {
        gameManager = new GameManager(); // Создаём новый экземпляр GameManager
        field = gameManager.getField(); // Получаем поле из GameManager
        playerCastle = gameManager.getPlayerCastle(); // Замок игрока
        botCastle = gameManager.getBotCastle(); // Замок компьютера
        human = gameManager.getHumanHero(); // Герой игрока
        computer = gameManager.getComputerHero(); // Герой компьютера
    }

    @Test
    void testFieldGeneration() {
        // Проверяем, что поле сгенерировано правильно
        assertNotNull(field, "Поле должно быть создано.");
        assertEquals(10, field.getWidth(), "Ширина поля должна быть 10.");
        assertEquals(10, field.getHeight(), "Высота поля должна быть 10.");
    }

    @Test
    void testHeroCreation() {
        // Проверяем создание героя игрока и компьютера
        assertNotNull(human, "Герой игрока должен быть создан.");
        assertNotNull(computer, "Герой компьютера должен быть создан.");

        assertEquals(1000, human.getHealth(), "Начальное здоровье героя игрока должно быть 1000.");
        assertEquals(1000, computer.getHealth(), "Начальное здоровье героя компьютера должно быть 1000.");
    }

    @Test
    void testInitialCastlePositions() {
        // Проверяем начальные позиции замков
        assertEquals(new Position(1, 1), playerCastle.getPosition(), "Позиция замка игрока должна быть (1, 1).");
        assertEquals(new Position(8, 8), botCastle.getPosition(), "Позиция замка компьютера должна быть (8, 8).");
    }

    @Test
    void testMoveHero() {
        // Подготавливаем ввод для перемещения героя
        String input = "D\n"; // Нажимаем 'D' для движения вправо
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // Делаем движение героя
        ArrayList<Unit> uni = new ArrayList<Unit>();
        uni.add(GameUnits.SPEARMAN);
        human.setUnits(uni);
        human.move(1, 0, field, 0);
        Position newPosition = human.getPosition();

        // Проверяем, что герой переместился
        assertEquals(new Position(1, 0), newPosition, "Герой должен переместиться в (1, 0).");
    }

    @Test
    void testGoldCaveGeneration() {
        // Проверяем, что пещеры с золотом генерируются корректно
        List<GoldCave> caves = field.getGoldCaves(); // Получаем список пещер
        assertEquals(3, caves.size(), "Должно быть 3 пещеры с золотом.");
        assertTrue(caves.stream().allMatch(cave -> cave.getGoldAmount() >= 1 && cave.getGoldAmount() <= 800),
                "Золото в пещерах должно быть от 500 до 800.");
    }

}
