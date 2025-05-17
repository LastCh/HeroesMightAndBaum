package game.model.building.onmap;

import game.api.FieldObject;
import game.api.Immovable;
import game.api.Position;
import game.interf.CaveInterface;
import game.map.DungeonField;
import game.model.hero.HumanHero;
import game.model.monster.Zombie;
import game.model.hero.Hero;

public class GoldCave extends FieldObject implements Immovable {
    private final int goldAmount;
    private boolean inCave = false;
    private final DungeonField dungeonField;
    private final Position caveEntry = new Position(0, 0);
    private int artifactUses;
    private final CaveInterface ui = new CaveInterface();

    public GoldCave(Position position, int goldAmount) {
        super(position, "\u001B[33m\u001B[41m💰\u001B[0m", 4);
        this.goldAmount = goldAmount;
        this.dungeonField = new DungeonField(5, 5);
        int zombiesCount = dungeonField.addZombies();
        artifactUses = zombiesCount - 1;
    }

    public void interact(HumanHero player) {
        inCave = true;
        ui.print("\nИгрок входит в Золотую пещеру!");
        player.setPosition(caveEntry);
        dungeonField.getCell(caveEntry.x(), caveEntry.y()).addObject(player);

        while (true) {
            dungeonField.render();
            ui.print("Вы находитесь в клетке: (" + player.getPosition().x() + ";" + player.getPosition().y() + ")");
            ui.showPrompt();

            String input = ui.readAction();
            int dx = 0, dy = 0;

            switch (input) {
                case "W" -> dy = -1;
                case "S" -> dy = 1;
                case "A" -> dx = -1;
                case "D" -> dx = 1;
                case "L" -> {
                    removePlayerFromCave(player);
                    return;
                }
                default -> {
                    ui.print("Неизвестная команда.");
                    continue;
                }
            }

            player.moveInCave(dx, dy, dungeonField);
            dungeonField.zombiesTurn(player);
            ui.waitMillis(1000);

            ui.clearConsole();
            dungeonField.render();

            if (!player.isAlive()) {
                ui.print("Вы погибли в пещере!");
                ui.waitMillis(1000);
                removePlayerFromCave(player);
                return;
            }

            if (areAllZombiesDead()) {
                ui.clearConsole();
                ui.print("Вы победили всех зомби и получаете золото и артефакты!");
                ui.waitMillis(1000);
                player.receiveArtifact(artifactUses);
                player.addGold(getGoldAmount());
                removePlayerFromCave(player);
                return;
            }

            ui.clearConsole();
        }
    }

    private boolean areAllZombiesDead() {
        return dungeonField.getZombies().stream().allMatch(Zombie::isDead);
    }

    private void removePlayerFromCave(Hero player) {
        dungeonField.getCell(player.getPosition().x(), player.getPosition().y()).removeObject(player);
        player.setPosition(this.getPosition());
    }

    public int getGoldAmount() {
        return goldAmount;
    }

    public boolean isInCave() {
        return inCave;
    }

    @Override
    public String getClassName() {
        return "GoldCave";
    }

    @Override
    public String serialize() {
        return position.x() + "," + position.y() + ";" + getGoldAmount();
    }

    public static GoldCave deserialize(String data) {
        String[] parts = data.split(";");
        String[] coords = parts[0].split(",");
        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);
        int gold = Integer.parseInt(parts[1]);

        return new GoldCave(new Position(x, y), gold);
    }

}
