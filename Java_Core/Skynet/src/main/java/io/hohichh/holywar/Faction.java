package io.hohichh.holywar;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Faction implements Runnable{
    private final Queue<Detail> storage;
    private final Map<Detail, Integer> robotBodyParts;
    private int robotCount;
    private final String name;

    private final CyclicBarrier barrier;
    private final int DAYS;

    private final int HEADS_REF_COUNT = 1;
    private final int TORSO_REF_COUNT = 1;
    private final int ARM_REF_COUNT = 2;
    private final int LEG_REF_COUNT = 2;

    public Faction(Queue<Detail> storage, String name, CyclicBarrier barrier, int days){
        this.storage = storage;
        this.robotBodyParts = new HashMap<Detail, Integer>();
        this.robotCount = 0;
        this.name = name;
        this.barrier = barrier;
        this.DAYS = days;
    }

    @Override
    public void run() {
        for(int day = 1 ; day <= DAYS ; day++){
            try {
                barrier.await();
                entryStorage();
                buildRobots();
                barrier.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int getRobotCount(){
        return robotCount;
    }

    private void entryStorage(){
        for(int i = 0; i < 5; i++){
            Detail detail = storage.poll();
            if(detail == null){
                break;
            }
            robotBodyParts.merge(detail, 1, Integer::sum);
        }
    }

    private void buildRobots(){
         while(robotBodyParts.getOrDefault(Detail.HEAD, 0) >= HEADS_REF_COUNT
                && robotBodyParts.getOrDefault(Detail.TORSO, 0) >= TORSO_REF_COUNT
                && robotBodyParts.getOrDefault(Detail.ARM, 0) >= ARM_REF_COUNT
                && robotBodyParts.getOrDefault(Detail.LEG, 0) >= LEG_REF_COUNT
        ){
             robotBodyParts.compute(Detail.HEAD, (key, val) -> val - HEADS_REF_COUNT);
             robotBodyParts.compute(Detail.TORSO, (key, val) -> val - TORSO_REF_COUNT);
             robotBodyParts.compute(Detail.ARM, (key, val) -> val - ARM_REF_COUNT);
             robotBodyParts.compute(Detail.LEG, (key, val) -> val - LEG_REF_COUNT);
            robotCount++;
        }
    }
}
