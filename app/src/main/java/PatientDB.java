import java.util.ArrayList;

//PatientDB will have a list of all the patients in the unit

public class PatientDB {
    ArrayList<Patient> patients = new ArrayList<Patient>();

    // Constructor
    public PatientDB() {
    }

    public void addPatient(String name,String hospID, String DOB, String gender) {
        Patient baby = new Patient(name,hospID,DOB,gender);
        patients.add(baby);
    }

    // probably need a method to add more patient details

    public String findPatient(String name) {
        String str = new String();
        int patientNo = 0;
        for(int i=0; i < patients.size(); i++) {
            if (patients.get(i).getName() == name) patientNo = i+1;
        }
        if (patientNo == 0) str = "Not a registered patient";
        else str = (name + " is Patient Number " + patientNo);
        return str;
    }

    // method to display all patients in the database
     public void patientMenu() {
        for (Patient p:patients) {
            System.out.println("Name: "+p.getName()+" Hospital ID: "+p.getHospID());
        }
     }

}
