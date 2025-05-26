package game.service;

import game.model.hero.ServiceVisitor;

class DummyVisitor implements ServiceVisitor {
    int healed = 0;                               // <> 0 → эффект применён

    @Override public String getName()            { return "Test"; }
    @Override public int    getGold()            { return 0; }
    @Override public void   addGold(int a)       { }
    @Override public void   spendMoney(int c)    { }
    @Override public void   healUnits(int hp)    { healed += hp; }
    @Override public void   boostMovement(int p) { }
    @Override public void   reduceCastleCaptureTime() { }
    @Override public void   applyBonus(ServiceType t) { }
}