package game.service;

import game.model.building.onmap.WanderingCircus;
import game.model.hero.Hero;
import game.model.hero.HumanHero;
import game.model.unit.GameUnits;
import game.model.unit.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static game.api.LogConfig.NPC_LOGGER;

public class CircusBattleTask implements Runnable {
    private final HumanHero hero;
    private final WanderingCircus circus;

    public CircusBattleTask(HumanHero hero, WanderingCircus circus) {
        this.hero = hero;
        this.circus = circus;
    }

    @Override
    public void run() {
        List<Unit> mobs = generateRandomTroupe();
        int power = mobs.stream().mapToInt(Unit::getPower).sum();

        hero.takeDamage(power);
        System.out.println("🤡 Цирковые артисты поколотили вас на " + power + " урона!");
        NPC_LOGGER.info("[CIRCUS] " + hero.getName() + " был избит труппой на " + power + " урона");
    }


    private List<Unit> generateRandomTroupe() {
        Unit[] pool = { GameUnits.SPEARMAN, GameUnits.CROSSBOWMAN,
                GameUnits.SWORDSMAN, GameUnits.CAVALRYMAN };
        int n = ThreadLocalRandom.current().nextInt(1, 4);
        List<Unit> list = new ArrayList<>();
        for (int i = 0; i < n; i++) list.add(pool[ThreadLocalRandom.current().nextInt(pool.length)].cloneUnit());
        return list;
    }
}
