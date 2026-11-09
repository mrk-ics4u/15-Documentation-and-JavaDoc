/*
 * Name:
 * Description:
 * Created by:
 * Last edited:
 */
public class ParkingMeter {

    private int minutesPerDollar;
    private int maxMinutes;
    private int minutesLeft;

    public ParkingMeter(int minutesPerDollar, int maxMinutes) {
        this.minutesPerDollar = minutesPerDollar;
        this.maxMinutes = maxMinutes;
        this.minutesLeft = 0;
    }

    public int getMinutesLeft() {
        return minutesLeft;
    }

    public int getMaxMinutes() {
        return maxMinutes;
    }

    /**
     * Adds time to the meter for a payment. Each dollar buys
     * minutesPerDollar minutes, and part of a minute doesn't count
     * (it rounds down). The meter can't hold more than its maximum, so
     * any minutes past the maximum are lost.
     *
     * Precondition: dollars > 0.
     * Postcondition: getMinutesLeft() is at most getMaxMinutes().
     *
     * @param dollars the amount paid
     * @return the number of minutes actually added to the meter
     */
    public int addPayment(double dollars) {
        return 0;    // replace this line
    }

    public void timePasses(int minutes) {
        minutesLeft = minutesLeft - minutes;
        if (minutesLeft < 0) {
            minutesLeft = 0;
        }
    }

    public boolean isExpired() {
        return minutesLeft == 0;
    }

    public double costToFill() {
        return (maxMinutes - minutesLeft) / (double) minutesPerDollar;
    }
}
