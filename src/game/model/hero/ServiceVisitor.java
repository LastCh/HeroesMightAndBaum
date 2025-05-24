package game.model.hero;

import game.service.ServiceType;

public interface ServiceVisitor {
    String getName();
    int getGold();
    void addGold(int amount);
    void applyBonus(ServiceType service);
    void spendMoney(int cost);
    void healUnits(int heal);
    void boostMovement(int movementPoints);
    void reduceCastleCaptureTime();
}

