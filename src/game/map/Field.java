package game.map;

import game.api.FieldObject;
import game.api.Position;
import game.model.building.onmap.Castle;
import game.model.building.onmap.GoldCave;
import game.model.hero.ComputerHero;
import game.model.hero.HumanHero;
import game.model.hero.Hero;
import game.model.hero.PurchasableHero;

import java.util.*;

public class Field {
    protected final int width;
    protected final int height;
    protected final Cell[][] grid;
    protected final List<PurchasableHero> allHeroes = new ArrayList<>();

    public Field(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Cell[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new Cell();
            }
        }
    }

    public List<PurchasableHero> getAllHeroes() {
        return allHeroes;
    }

    public void addHeroToAll(PurchasableHero newHero) {
        allHeroes.add(newHero);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Cell getCell(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return grid[x][y];
        }
        return null;
    }

    public void moveObject(FieldObject obj, int fromX, int fromY, int toX, int toY) {
        Cell source = getCell(fromX, fromY);
        Cell destination = getCell(toX, toY);

        if (source != null && destination != null) {
            source.removeObject(obj);
            destination.addObject(obj);
        }
    }

    public void render() {
        for (int y = 0; y < height; y++) {
            // Верхняя граница строки
            for (int x = 0; x < width; x++) {
                System.out.print("+----");
            }
            System.out.println("+");

            // Содержимое клеток
            for (int x = 0; x < width; x++) {
                System.out.print("| " + grid[x][y].getDisplaySymbol());
            }
            System.out.println("|");
        }

        // Нижняя граница
        for (int x = 0; x < width; x++) {
            System.out.print("+----");
        }
        System.out.println("+");
    }

    public ComputerHero getComputerHeroAt(Position pos) {
        Cell cell = getCell(pos.x(), pos.y());
        if (cell != null) {
            for (FieldObject obj : cell.getObjects()) {
                if (obj instanceof ComputerHero) {
                    return (ComputerHero) obj;
                }
            }
        }
        return null;
    }

    public Hero getHeroAt(Position pos) {
        Cell cell = getCell(pos.x(), pos.y());
        if (cell != null) {
            for (FieldObject obj : cell.getObjects()) {
                if (obj instanceof Hero) {
                    return (Hero) obj;
                }
            }
        }
        return null;
    }

    public Hero getEnemyHeroAt(Position pos) {
        Cell cell = getCell(pos.x(), pos.y());
        if (cell != null) {
            for (FieldObject obj : cell.getObjects()) {
                if (obj instanceof Hero) {
                    return (Hero) obj;
                }
            }
        }
        return null;
    }

    public HumanHero getHumanHeroAt(Position pos) {
        Cell cell = getCell(pos.x(), pos.y());
        if (cell != null) {
            for (FieldObject obj : cell.getObjects()) {
                if (obj instanceof HumanHero) {
                    return (HumanHero) obj;
                }
            }
        }
        return null;
    }

    public Castle getCastleAt(Position pos) {
        Cell cell = getCell(pos.x(), pos.y());
        if (cell != null) {
            for (FieldObject obj : cell.getObjects()) {
                if (obj instanceof Castle) {
                    return (Castle) obj;
                }
            }
        }
        return null;
    }



    public void removePlayer(Hero player) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell cell = grid[x][y];
                if (cell.contains(player)) {
                    cell.removeObject(player);
                    return;
                }
            }
        }
    }

    public void removeCave(GoldCave cave) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell cell = grid[x][y];
                if (cell.contains(cave)) {
                    cell.removeObject(cave);
                    return;
                }
            }
        }
    }

    public Hero getNearestEnemyHero(Hero hero) {
        Position start = hero.getPosition();
        boolean[][] visited = new boolean[width][height];
        Queue<Position> queue = new LinkedList<>();
        queue.add(start);
        visited[start.x()][start.y()] = true;

        while (!queue.isEmpty()) {
            Position pos = queue.poll();
            Hero other = getHeroAt(pos);
            if (other != null && !isAlly(hero, other)) { // Если это чужой герой
                return other;
            }

            for (Position neighbor : getNeighbors(pos)) {
                if (!visited[neighbor.x()][neighbor.y()]) {
                    visited[neighbor.x()][neighbor.y()] = true;
                    queue.add(neighbor);
                }
            }
        }
        return null;
    }

    // Вспомогательный метод — соседние клетки
    private List<Position> getNeighbors(Position pos) {
        List<Position> neighbors = new ArrayList<>();
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int nx = pos.x() + dx[i];
            int ny = pos.y() + dy[i];

            if (nx >= 0 && ny >= 0 && nx < width && ny < height) {
                neighbors.add(new Position(nx, ny));
            }
        }
        return neighbors;
    }

    // Проверка союзников
    public boolean isAlly(Hero a, Hero b) {
        if (a == b) return true;
        if (a instanceof PurchasableHero pHero && pHero.getOwner() == b) return true;
        if (b instanceof PurchasableHero pHero && pHero.getOwner() == a) return true;
        if (a instanceof PurchasableHero pA && b instanceof PurchasableHero pB && pA.getOwner() == pB.getOwner()) return true;
        return false;
    }

    public Position findFreeAdjacent(Position center) {
        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, -1, 0, 1};

        List<Position> candidates = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int nx = center.x() + dx[i];
            int ny = center.y() + dy[i];
            Cell cell = getCell(nx, ny);
            if (cell != null && cell.isEmpty()) {
                candidates.add(new Position(nx, ny));
            }
        }
        if (!candidates.isEmpty()) {
            return candidates.get(new Random().nextInt(candidates.size()));
        }
        return null;

    }

    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append(width).append(";").append(height).append(";");

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                sb.append(grid[x][y].getTerrainType()).append(",");
            }
        }
        return sb.toString();
    }

    public static Field deserialize(String data, Castle castlePlayer, Castle castleComputer,
                                    ComputerHero computerHero, HumanHero humanHero) {
        String[] parts = data.split(";");
        int width = Integer.parseInt(parts[0]);
        int height = Integer.parseInt(parts[1]);
        String[] cellsData = parts[2].split("@");

        Field field = new Field(width, height);

        int index = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell cell = Cell.deserialize(cellsData[index++], field,
                        castlePlayer, castleComputer, computerHero, humanHero);
                field.grid[x][y] = cell;
            }
        }

        return field;
    }


}