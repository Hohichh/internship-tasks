package io.hohichh.holywar;

import io.hohichh.Main;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Factory implements Runnable{
    private final Queue<Detail> storage;
    private final Random rand = new Random();
    private final CyclicBarrier barrier;
    private final int DAYS;

    public Factory(Queue<Detail> storage, CyclicBarrier barrier, int days){
        this.storage = storage;
        this.barrier = barrier;
        DAYS = days;
    }

    @Override
    public void run() {
        for(int day = 1; day <= DAYS; day++){
            Logger.getLogger(Factory.class.getName())
                    .log(Level.INFO, "ФАЗА ДНЯ", new Object[]{day});

            int detailCount = produce();
            logReport(day, detailCount);

            try {
                barrier.await(); //here all 3 threads are awaited, so cyclic barrier make then runnable
                Logger.getLogger(Factory.class.getName())
                        .log(Level.INFO, "Рабочий день " + day + " окончен", new Object[]{day});

                barrier.await(); //but the factory doesen't work at night and waits till other threads become awaited
                Logger.getLogger(Factory.class.getName())
                        .log(Level.INFO, "Фабрика засыпает, просыпаются фракции", new Object[]{day});
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int produce(){
        Detail[] detail = Detail.values();
        int detailCount = rand.nextInt(10) + 1; //fabric can produce up to 10 parts per day
        for (int i = 0; i < detailCount; i++) {
            storage.add(detail[rand.nextInt(detail.length)]);
        }
        return detailCount;
    }

    private void logReport(int day, int amount){
        String message = "ПРОИЗВЕДЕНО: " + amount + " ДЕТАЛЕЙ";
        Logger.getLogger(Factory.class.getName()).log(Level.INFO, message, new Object[]{day});
    }
}
