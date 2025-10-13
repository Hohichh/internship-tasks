package io.hohichh;

import io.hohichh.holywar.Detail;
import io.hohichh.holywar.Faction;
import io.hohichh.holywar.Factory;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CyclicBarrier;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        final int DAYS = 100;
        final CyclicBarrier barrier = new CyclicBarrier(3);

        var storage = new ArrayBlockingQueue<Detail>(10);
        Factory factory = new Factory(storage, barrier, DAYS);
        Faction factionWorld = new Faction(storage, "World", barrier, DAYS);
        Faction factionWednesday = new Faction(storage, "Wednesday", barrier, DAYS);

        Thread factoryThread = new Thread(factory, "factory-thread");
        Thread worldThread = new Thread(factionWorld, "faction-World-thread");
        Thread wednesdayThread = new Thread(factionWednesday, "faction-Wednesday-thread");

        factoryThread.start();
        worldThread.start();
        wednesdayThread.start();

        factoryThread.join();
        worldThread.join();
        wednesdayThread.join();

        System.out.println("======================================");
        System.out.println("Симуляция на протяжении " + DAYS + " дней завершена!");
        System.out.println("Армия фракции 'World': " + factionWorld.getRobotCount() + " роботов.");
        System.out.println("Армия фракции 'Wednesday': " + factionWednesday.getRobotCount() + " роботов.");


    }
}