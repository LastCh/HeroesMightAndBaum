package game.service;

import game.model.building.onmap.Enterprise;
import game.model.hero.ServiceVisitor;

import java.util.concurrent.CountDownLatch;

class DummyEnterprise extends Enterprise {
    final java.util.concurrent.CountDownLatch finished = new CountDownLatch(1);

    DummyEnterprise() {
        super(new game.api.Position(0,0), "X", 1, null,
                0,0,0,0);                // параметры NPC здесь не важны
    }
    @Override public void onServiceFinished(ServiceVisitor v) { finished.countDown(); }

    // эти методы нам не нужны для теста
    @Override protected ServiceType randomService() { return null; }
    @Override public String getClassName()          { return "Dummy"; }
}