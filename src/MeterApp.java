/*
 * Name:        Lesson 15: Documentation and Javadoc (MeterApp.java)
 * Description: The program that uses your ParkingMeter class. Don't change
 *              this file. It reads the meter's settings and a list of
 *              events, runs each one, and prints what happened.
 * Created by:  Mr Kowalczewski
 * Last edited: 2026-11-03
 */
import java.util.Scanner;

/**
 * Runs a parking meter through a list of payments and waits.
 *
 * @author Mr Kowalczewski
 */
public class MeterApp {

    /**
     * Reads the input, runs every event on one meter, and prints the results.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int minutesPerDollar = Integer.parseInt(input.nextLine().trim());
        int maxMinutes = Integer.parseInt(input.nextLine().trim());
        int count = Integer.parseInt(input.nextLine().trim());

        ParkingMeter meter = new ParkingMeter(minutesPerDollar, maxMinutes);

        for (int i = 0; i < count; i++) {
            String[] parts = input.nextLine().trim().split(" ");

            if (parts[0].equals("pay")) {
                double dollars = Double.parseDouble(parts[1]);
                // addPayment's precondition is dollars > 0, so the caller checks first
                if (dollars > 0) {
                    int added = meter.addPayment(dollars);
                    System.out.printf("Paid $%.2f: %d min added, %d min left%n",
                            dollars, added, meter.getMinutesLeft());
                } else {
                    System.out.printf("Paid $%.2f: refused%n", dollars);
                }
            } else {
                int minutes = Integer.parseInt(parts[1]);
                // same idea: timePasses needs minutes >= 0
                if (minutes >= 0) {
                    meter.timePasses(minutes);
                    System.out.printf("Waited %d min: %d min left%n", minutes, meter.getMinutesLeft());
                } else {
                    System.out.printf("Waited %d min: refused%n", minutes);
                }
            }
        }

        if (meter.isExpired()) {
            System.out.println("Expired: yes");
        } else {
            System.out.println("Expired: no");
        }
        System.out.printf("Cost to fill: $%.2f%n", meter.costToFill());
    }
}
