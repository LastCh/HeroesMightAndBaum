package game.model.building.onmap;

import game.api.Position;
import game.map.Field;
import game.model.hero.HumanHero;
import game.model.hero.ServiceVisitor;
import game.service.CircusBattleTask;
import game.service.ServiceType;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class WanderingCircus extends Enterprise {

    private final int ttlSeconds = ThreadLocalRandom.current().nextInt(120, 241);
    private long bornTime  = System.currentTimeMillis();
    private static final Scanner SC = new Scanner(System.in);


    public static final ServiceType WORK = new ServiceType(
            "Работать в цирке", 5_000, 0,
            hero -> {
                int gold = ThreadLocalRandom.current().nextInt(10, 1000);
                hero.addGold(gold);
                System.out.println("💰 Вы заработали " + gold + " золота!");
            }
    );



    public WanderingCircus(Position p, Field field) {
        super(p, "\uD83C\uDFAB"  , 3, field,
                0, 0, 60, 0
        );
    }

    @Override protected ServiceType randomService() { return WORK; }

    @Override public String getClassName() { return "WanderingCircus"; }


    @Override
    public synchronized void enter(ServiceVisitor v, ServiceType unused) {
        if (!(v instanceof HumanHero hero)) return;

        double fame = hero.getAllKarma();
        boolean highFame = fame > 1;

        if (highFame) {
            hero.spendMoney(200);
            System.out.println("🎪 С вас взяли 200 золота за \"безобразное поведение\"!");
            System.out.println("🎪 Артисты разозлились и набросились на вас!");

            new Thread(new CircusBattleTask(hero, this)).start();
            return;
        }


        // низкая слава — обычная работа
        super.enter(hero, WORK);                   // 5 сек, затем начисляем золото
    }

    public boolean isExpired() {
        long alive = (System.currentTimeMillis() - bornTime) / 1000;
        return alive >= ttlSeconds && getCurrentVisitors().isEmpty();
    }

    @Override
    public int displayMenu() {
        System.out.println("\n🎪  Вы у бродячего цирка!");
        System.out.println("1) Работать (5 сек)");
        System.out.println("2) Уйти");
        System.out.print  ("Ваш выбор: ");
        while (true) {
            String s = SC.nextLine().trim();
            if ("1".equals(s) || "2".equals(s)) return Integer.parseInt(s);
            System.out.print("Введите 1 или 2: ");
        }
    }

}

