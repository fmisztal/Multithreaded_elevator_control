package Lab4.Zad_2;

import java.util.LinkedHashSet;
import java.util.concurrent.*;

public class Elevator implements Runnable{
    private static final int MAX = 5;
    public LinkedHashSet<Integer> floorToStop = new LinkedHashSet<>();
    public int currentFloor;
    final Semaphore ave = new Semaphore(MAX, true);
    public int speed;
    boolean isDirectionUp;

    public Elevator(int speed) {
        this.currentFloor = -1;
        this.speed = speed;
        this.isDirectionUp = true;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (!floorToStop.isEmpty()) {
                    isDirectionUp = (floorToStop.iterator().next() - currentFloor > 0);

                    while (true) {
                        System.out.println();
                        System.out.println("Elevator is on " + ((isDirectionUp) ? this.currentFloor + 1 : this.currentFloor - 1)
                                + " floor. People: " + (this.MAX - this.ave.availablePermits()));
                        this.currentFloor = (isDirectionUp) ? this.currentFloor + 1 : this.currentFloor - 1;
                        Thread.sleep(speed);

                        synchronized (this.floorToStop) {
                            if (this.currentFloor == this.floorToStop.iterator().next()) {
                                this.floorToStop.remove(this.floorToStop.iterator().next());
                                if (floorToStop.isEmpty()) {
                                    break;
                                }
                                isDirectionUp = (this.floorToStop.iterator().next() - this.currentFloor > 0);
                            }
                            if (this.floorToStop.contains(this.currentFloor)) {
                                this.floorToStop.remove(this.currentFloor);
                            }
                            isDirectionUp = (floorToStop.iterator().next() - currentFloor > 0);
                        }
                        System.out.println("The elevator is going " + ((isDirectionUp) ? "up" : "down"));
                    }
                }

                Thread.sleep(0);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}