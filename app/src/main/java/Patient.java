//import java.time.LocalDate;
//import java.time.LocalTime;

/** Patient is a class which contains all the data for one patient */

public class Patient {
    // The following fields list patient's profile information
    private String name;
    private String hospID;
    private String DOB; // String for now, will figure out how to set it as a date
    private String timeOfBirth;
    private double weight;
    private String gender;
    private String motherName;
    private String fatherName;
    private String contactNum;
    private double healthIndex;
    private MonitoredParams param;

    // constructor
    public Patient(String name,String hospID, String DOB, String gender) {
        this.name = name;
        this.hospID = hospID;
        this.DOB = DOB;
        this.gender = gender;
        param = new MonitoredParams();
    }

    // getters and setters
    public String getName() {
        return name;
    }

    public String getHospID() {
        return hospID;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public String getTimeOfBirth() {
        return timeOfBirth;
    }

    public void setTimeOfBirth(String timeOfBirth) {
        this.timeOfBirth = timeOfBirth;
    }

    public void setHealthIndex(double perc) {
        // code to calculate and store health index
        healthIndex = perc;
    }

    public double getHealthIndex() {
        return healthIndex;
    }

    // Main functions
    // public void graphPlotter()
}
