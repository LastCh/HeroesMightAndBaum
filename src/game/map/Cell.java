package game.map;

import game.api.FieldObject;
import game.model.building.onmap.Castle;
import game.model.building.onmap.GoldCave;
import game.model.hero.*;

import java.util.List;
import java.util.ArrayList;

public class Cell {
    private final List<FieldObject> objects = new ArrayList<>();
    private TerrainType terrainType = TerrainType.GRASS;

    public boolean contains(FieldObject obj) {
        return objects.contains(obj);
    }

    public void addObject(FieldObject obj) {
        objects.add(obj);
        objects.sort((a, b) -> Integer.compare(b.getPriority(), a.getPriority()));
    }

    public void setTerrainType(TerrainType terrainType) {
        this.terrainType = terrainType;
    }

    public boolean isEmpty() {
        return objects.isEmpty();
    }

    public TerrainType getTerrainType() {
        return terrainType;
    }

    public void removeObject(FieldObject obj) {
        objects.remove(obj);
    }

    public List<FieldObject> getObjects() {
        return new ArrayList<>(objects);
    }

    public String getDisplaySymbol() {
        String symbol;
        if (!objects.isEmpty()) {
            symbol = objects.getFirst().getColoredSymbol();
        } else {
            symbol = terrainType != null
                    ? terrainType.getColoredBlock()
                    : "·"; // Символ пустой клетки
        }
        return String.format("%-3s", symbol); // Фиксированная ширина в 3 символа
    }

    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append(terrainType.name());

        for (FieldObject obj : objects) {
            sb.append("|").append(obj.getClassName()).append("#").append(obj.serialize());
        }

        return sb.toString();
    }

    public static Cell deserialize(
            String data,
            Field field,
            Castle castlePlayer,
            Castle castleComputer,
            Hero computerHero,
            Hero humanHero
    ) {
        Cell cell = new Cell();
        String[] parts = data.split("\\|");

        cell.setTerrainType(TerrainType.valueOf(parts[0]));

        for (int i = 1; i < parts.length; i++) {
            String[] objParts = parts[i].split("#", 2);
            String type = objParts[0];
            String objData = objParts[1];

            FieldObject obj = switch (type) {
                case "Castle" -> null;
                case "GoldCave" -> GoldCave.deserialize(objData);
                case "HumanHero" -> null;
                case "ComputerHero" -> null;
                case "ElfHero" -> ElfHero.deserialize(objData, field,castlePlayer, castleComputer, humanHero, computerHero);
                case "OrcHero" -> OrcHero.deserialize(objData, field,castlePlayer, castleComputer, humanHero, computerHero);
                case "DwarfHero" -> DwarfHero.deserialize(objData, field,castlePlayer, castleComputer, humanHero, computerHero);
                default -> null;
            };

            if (obj != null) {
                cell.addObject(obj);
            }
        }

        return cell;
    }
}
