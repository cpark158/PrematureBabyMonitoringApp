public class Time {
    // The time axis will be displayed in time of day (days, hours, mins and secs) instead of an increasing time value
    private int day;
    private int h;
    private int m;
    private double s;

    // Constructor
    public Time() {
    }

    // Getters and setters
    public void getTime() {
        System.out.println("Day: " + day + ", " + h + ":" + m + ":" + s);
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setH(int h) {
        this.h = h;
    }

    public void setM(int m) {
        this.m = m;
    }

    public void setS(double s) {
        this.s = s;
    }
}
