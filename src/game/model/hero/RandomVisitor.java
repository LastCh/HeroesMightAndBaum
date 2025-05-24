package game.model.hero;

import game.service.ServiceType;

import static game.api.LogConfig.LOGGER;

public class RandomVisitor implements ServiceVisitor {
    private final String name;
    private int gold;

    public RandomVisitor(String name, int gold) {
        this.name = name;
        this.gold = gold;
    }

    @Override public String getName() { return name; }
    @Override public int getGold() { return gold; }

    @Override public void addGold(int amount)  { gold += amount; }
    @Override public void spendMoney(int cost) { gold -= cost;  }

    @Override
    public void healUnits(int heal) {
    }

    @Override
    public void boostMovement(int movementPoints) {
    }

    @Override
    public void reduceCastleCaptureTime() {
    }

    @Override
    public void applyBonus(ServiceType service) {
    }
}
