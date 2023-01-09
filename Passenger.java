package Lab4.Zad_2;

import java.util.concurrent.ThreadLocalRandom;

public class Passenger implements Runnable{
    private String name;
    private Elevator elevator;
    int destinationFloor;
    int spawnFloor;

    Passenger(String name, Elevator elevator, int numberOfFloors) {
        Main.numOfPassengers ++;
        this.name = name;
        this.elevator = elevator;
        this.spawnFloor = ThreadLocalRandom.current().nextInt(0,numberOfFloors);
        while (true) {
            this.destinationFloor = ThreadLocalRandom.current().nextInt(0, numberOfFloors);
            if (this.destinationFloor != this.spawnFloor) { // check if spawn floor is not destination floor
                break;
            }
        }
        System.out.println("\u001B[33m" + this.name + " spawn on floor: " + this.spawnFloor + ". Destination: " +
                this.destinationFloor + "\u001B[0m");
    }

    @Override
    public void run() {
        synchronized (elevator.floorToStop) {
            if (spawnFloor != elevator.currentFloor)
                elevator.floorToStop.add(this.spawnFloor);
        }
        try {
            Boolean hasWrittenMessage = false;
            while (true) {
                if (elevator.currentFloor == this.spawnFloor) {
                    if (elevator.ave.tryAcquire()) {    //check if elevator is full
                        System.out.println(this.name + " enters the elevator on " + elevator.currentFloor +
                                " floor. Destination: " + this.destinationFloor);
                        synchronized (elevator.floorToStop) {
                            elevator.floorToStop.add(this.destinationFloor);
                            break;
                        }
                    } else {
                        if (!hasWrittenMessage) {
                            System.out.println(	"\u001B[34m" +"Oops, elevator is full. " + this.name +
                                    " cannot enter." + "\u001B[0m");
                            hasWrittenMessage = true;
                        }
                        synchronized (elevator.floorToStop) {
                            elevator.floorToStop.add(this.spawnFloor);
                        }
                    }
                }
                else {
                    hasWrittenMessage = false;
                }
                Thread.sleep(0);
            }
            Thread.sleep(0);

            while (true) {
                if (elevator.currentFloor == this.destinationFloor) {
                    System.out.println(this.name + " left the elevator.");
                    elevator.ave.release();
                    Main.numOfPassengers --;
                    break;
                }
                Thread.sleep(0);
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
