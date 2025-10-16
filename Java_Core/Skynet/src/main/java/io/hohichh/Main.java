package io.hohichh;

import io.hohichh.holywar.Detail;
import io.hohichh.holywar.Faction;
import io.hohichh.holywar.Factory;
import io.hohichh.logging.HolyLoggerConfig;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        try {
            HolyLoggerConfig.setup();
        } catch (IOException e) {
            System.err.println("Can't configure logger: " + e.getMessage());
            e.printStackTrace();
        }

        Logger.getLogger(Main.class.getName())
                .log(Level.INFO, "И восстали машины из пепла ядерного огня," +
                        " и пошла война на уничтожения человечества. " +
                        "И шла она десятилетия, но последнее сражение состоится не в будущем," +
                        " оно состоится здесь, в наше время, сегодня ночью.");

        Logger.getLogger(Main.class.getName())
                .log(Level.INFO, "=======================================================================\n");


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

        String winnerName;
        int worldRobots = factionWorld.getRobotCount();
        int wednesdayRobots = factionWednesday.getRobotCount();

        if (worldRobots > wednesdayRobots) {
            winnerName = factionWorld.getName();
        } else if (wednesdayRobots > worldRobots) {
            winnerName = factionWednesday.getName();
        } else {
            winnerName = "НИЧЬЯ";
        }
        Logger.getLogger(Main.class.getName())
                .log(Level.INFO, "=======================================================================");

        String finalVerdict;
        if (winnerName.equals("НИЧЬЯ")) {
            finalVerdict = "И так закончилась стодневная война. Никто не смог одержать верх, и над пеплом фабрик воцарилось хрупкое равновесие стали.";
        } else {
            finalVerdict = String.format(
                    "И так гласит хроника этой эпохи машин. В битве за технологическое господство победу одержала фракция '%s', чья воля и сталь оказались крепче.",
                    winnerName
            );
        }

        String finalReport = String.format(
                        "ИТОГИ ВЕЛИКОГО ПРОТИВОСТОЯНИЯ:\n\n" +
                        "   Легионы фракции 'World' восстали числом в %d стальных воинов.\n" +
                        "   Технокульт 'Wednesday' породил армию из %d несокрушимых конструктов.\n\n" +
                        "%s\n",
                worldRobots,
                wednesdayRobots,
                finalVerdict
        );

        Logger.getLogger(Main.class.getName()).log(Level.INFO, finalReport);
    }
}