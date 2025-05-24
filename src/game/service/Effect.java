package game.service;

import game.model.hero.ServiceVisitor;

@FunctionalInterface
public interface Effect {
    void apply(ServiceVisitor serviceVisitor);
}
