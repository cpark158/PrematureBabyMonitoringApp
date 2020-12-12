// Glucose is a class for the concentrations (values) recorded from the device, may need to add calibration method

public class Glucose {
    private double channel1;
    private double channel2;

    public Glucose() {}


    public double getChannel1() {
        return channel1;
    }

    public void setChannel1(double channel1) {
        this.channel1 = channel1;
    }

    public double getChannel2() {
        return channel2;
    }

    public void setChannel2(double channel2) {
        this.channel2 = channel2;
    }
}
