package Lab4.Zad_2;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Filip Misztal, Stanisław Moska
 * Zadanie okazało się być dosyć wymagającym, dlatego zdecydowaliśmy się połączyć siły i zrobić to wspólnie.
 * Mamy nadzieję, że nie ma Pan nic przeciwko temu. :)
 */

public class Main {
    public static int numOfPassengers = 0;
    public static void main(String[] args) throws InterruptedException {

        if (args.length != 3) {
            System.out.println("Wrong number of arguments!");
            return;
        }

        try {
            int F = Integer.parseInt(args[0]);
            int T = Integer.parseInt(args[1]) * 1000;
            double P = Double.parseDouble(args[2]);
            int N = 20; //maksymalna liczba pasażerów

            if (P > 1 || P < 0) {
                System.out.println("Probability out of range! (0;1)");
                return;
            }

            Elevator elevator = new Elevator(T);
            Thread ele = new Thread(elevator);
            ele.start();

            // Tworzę pierwszego pasażera żeby nie czekać
            Thread pas = new Thread(new Passenger("Passenger: " + 0, elevator, F));
            pas.start();

            int i = 0;
            while (true) {
                double p = ThreadLocalRandom.current().nextDouble(0, 1);
                if (p < P && numOfPassengers < N) {
                    i++;
                    Thread t = new Thread(new Passenger("Passenger: " + i, elevator, F));
                    t.start();
                }
                Thread.sleep(1000);
            }
        } catch (Exception ex) {
            System.out.println("Wrong type of arguments!");
            return;
        }

    }
}