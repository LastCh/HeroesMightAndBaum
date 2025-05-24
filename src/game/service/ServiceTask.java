package game.service;

import game.model.building.onmap.Enterprise;
import game.model.hero.ServiceVisitor;

public class ServiceTask implements Runnable {
    private final ServiceVisitor serviceVisitor;
    private final ServiceType service;
    private final Enterprise building;

    public ServiceTask(ServiceVisitor serviceVis, ServiceType service, Enterprise building) {
        this.serviceVisitor = serviceVis;
        this.service = service;
        this.building = building;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(service.getDurationMillis());
            service.getEffect().apply(serviceVisitor);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            building.onServiceFinished(serviceVisitor);   // ← уведомляем здание
        }
    }

}
