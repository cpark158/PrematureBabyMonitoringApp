package com.example.prematurebabymonitoringapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.prematurebabymonitoringapp.exceptions.invalidGenderException;
import com.example.prematurebabymonitoringapp.network.ClientInstance;
import com.example.prematurebabymonitoringapp.network.GetDataService;
import com.example.prematurebabymonitoringapp.network.PostDataService;
import com.github.mikephil.charting.charts.LineChart;
import android.text.Editable;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.tabs.TabLayout;
import java.util.Calendar;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    // Initialise graph plotting parameters
    LineChart mpLineChart;
    LineChart lactate_mpLineChart;
    TextFileProcessor txtFileProcessor = new TextFileProcessor();
    GraphPlotter graphPlot;

    // Initialise UI components from activity_main.xml
    TabLayout tabLayout;
    TextView msg;
    TextView currentLactateLevel;
    TextView commentsMade;
    EditText patientName;
    EditText patientHospID;
    EditText patientGender;
    EditText patientDOB;
    EditText commentSpace;
    Button addPatientButton;
    Button viewCurrentPatientButton;
    Button saveButton;
    Button saveCommentButton;
    Spinner spinnerPatientList;
    ImageView patientIcon;

    Button downloadGlucose;
    Button downloadLactate;

    // Initialise temporary Strings to retrieve and store inputted patient info
    String patientNameStr = "Name";
    String patientHospIDStr = "0";
    String patientGenderStr = "Male";
    String patientDOBStr = "1990-01-01";

    int currentChosenSpinner = 1;
    String currentChosenItem = " ";

    // Instantiate database to store patients
    PatientDB prematureBabies = new PatientDB();

    //To populate spinner (dropdown patient list)
    List<String> spinnerArray = new ArrayList<String>();

    public MainActivity() throws IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Parse .txt file and plot graph, to be displayed later
        txtFileProcessor.parseFile();
        graphPlot = new GraphPlotter(txtFileProcessor.getTimeValues(), txtFileProcessor.getVolt1());

        // Retrieve XML components
        retrieveXMLComponents();

        // Set up spinner files and adapter
        spinnerArray.add("Add Patient");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPatientList.setAdapter(adapter);

        mpLineChart = findViewById(R.id.line_chart);
        saveButton = findViewById(R.id.saveButton);
        addPatientButton = findViewById(R.id.button);

        // Add/Import existing patients from here onwards
        prematureBabies.addPatient("Martin Holloway",27863,"2020-12-12","male");
        spinnerArray.add(prematureBabies.lastPatient().getName());
        prematureBabies.addPatient("James Choi",52839,"2020-12-27","male");
        spinnerArray.add(prematureBabies.lastPatient().getName());
        prematureBabies.addPatient("John Smith", 01, "2020-11-27", "male");
        patientNameStr = prematureBabies.lastPatient().getName();
        spinnerArray.add(String.format(prematureBabies.lastPatient().getName()));

        //TODO Add meaningful logs for when the request fails
        //Fetch Patient List from remote Database
        /* Reference 3 - taken from https://medium.com/@prakash_pun/retrofit-a-simple-android-tutorial-48437e4e5a23 */
        GetDataService service = ClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Patient>> call = service.getPatientsList();
        call.enqueue(new Callback<List<Patient>>() {
            @Override
            public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
                List<Patient> patientList=response.body();
                for (Patient newPat:patientList){
                    if(!prematureBabies.patientExists(newPat.getHospID())){
                        prematureBabies.addPatient(newPat);
                        spinnerArray.add(prematureBabies.lastPatient().getName());
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Patient with this Hospital id exists!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Patient>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Remote Patients list wasn't retrieved!", Toast.LENGTH_SHORT).show();

            }
        });
        /* end of reference 1 */

        // Welcome page
        callWelcomePage("Welcome to Premature Baby Monitoring App. Click button below to add patient.");

        // Add Patient Details Page
        callNewPatientPage();

        // View Individual Pages
        callSpinner();

        // View different tabs - Basic Information and Health
        callDiffTabs();

    }

    public void retrieveXMLComponents(){
        msg = findViewById(R.id.textView); // Welcome page, all pages
        addPatientButton = findViewById(R.id.button); // Welcome page

        viewCurrentPatientButton = findViewById(R.id.button2); // Welcome page

        patientName = findViewById(R.id.typeName); // Add Patient Details Page
        patientHospID = findViewById(R.id.typeHospID); // Add Patient Details Page
        patientGender = findViewById(R.id.editGender); // Add Patient Details Page
        patientDOB = findViewById(R.id.editTextDate); // Add Patient Details Page
        spinnerPatientList = findViewById(R.id.spinnerPatient); // View individual pages
        tabLayout = findViewById(R.id.tabLayout); // View individual pages
        patientIcon = findViewById(R.id.icon); // View individual pages - basic info tab
        mpLineChart = findViewById(R.id.line_chart); // View individual pages - health tab
        saveButton = findViewById(R.id.saveButton); // View individual pages - health tab

        lactate_mpLineChart = findViewById(R.id.lactate_line_chart);
        currentLactateLevel = findViewById(R.id.lactateText);
        commentSpace = findViewById(R.id.commentSpace);
        commentsMade = findViewById(R.id.commentsMade);
        saveCommentButton = findViewById(R.id.saveCommentButton);

        downloadGlucose = findViewById(R.id.downloadGlucose);
        downloadLactate = findViewById(R.id.downloadLactate);
    }

    public void callWelcomePage(String printText){
        // Ensure other components aren't on the page
        patientName.setVisibility(View.GONE);
        patientHospID.setVisibility(View.GONE);
        patientGender.setVisibility(View.GONE);
        patientDOB.setVisibility(View.GONE);
        tabLayout.setVisibility(View.GONE);
        patientIcon.setVisibility(View.GONE);
        mpLineChart.setVisibility(View.GONE);
        saveButton.setVisibility(View.GONE);
        lactate_mpLineChart.setVisibility(View.GONE);
        currentLactateLevel.setVisibility(View.GONE);
        commentSpace.setVisibility(View.GONE);
        commentsMade.setVisibility(View.GONE);
        saveCommentButton.setVisibility(View.GONE);
        downloadLactate.setVisibility(View.GONE);
        downloadGlucose.setVisibility(View.GONE);

        // Set appropriate components are visible
        msg.setVisibility(View.VISIBLE);
        addPatientButton.setVisibility(View.VISIBLE);
        viewCurrentPatientButton.setVisibility(View.VISIBLE);
        spinnerPatientList.setVisibility(View.VISIBLE);

        // Set what to print
        msg.setTextSize(20);
        msg.setText(String.format(printText));
        msg.setGravity(Gravity.CENTER_HORIZONTAL);

        if (printText == "Welcome to Premature Baby Monitoring App. Click button below to add patient.")
        {
            spinnerPatientList.setVisibility(View.GONE);
            viewCurrentPatientButton.setVisibility(View.VISIBLE);
        }
        else
        {
            spinnerPatientList.setVisibility(View.VISIBLE);
            viewCurrentPatientButton.setVisibility(View.GONE);
        }

        addPatientButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Redirect to Add Patient Details Page
                msg.setText(String.format("Enter patient details below:"));
                patientName.setText("Name");
                patientHospID.setText("Hospital ID");
                patientDOB.setText("yyyy-mm-dd");
                patientGender.setText("Gender");
                patientName.setVisibility(View.VISIBLE);
                patientHospID.setVisibility(View.VISIBLE);
                patientGender.setVisibility(View.VISIBLE);
                patientDOB.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.VISIBLE);

                // Remove Add Patient button
                addPatientButton.setVisibility(View.GONE);
                viewCurrentPatientButton.setVisibility(View.GONE);
            }
        });

        viewCurrentPatientButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Redirect to Add Patient Details Page
                callWelcomePage("Refer to dropdown list above for other patients or add patient below.");

                // Remove Add Patient button
                viewCurrentPatientButton.setVisibility(View.GONE);
            }
        });
    }

    public void callNewPatientPage(){

        patientName.setText("Name");
        patientHospID.setText("Hospital ID");
        patientDOB.setText("yyyy-mm-dd");
        patientGender.setText("Gender");

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Upon clicking, save inputted information as patient details
                patientNameStr = patientName.getText().toString();
                patientHospIDStr = patientHospID.getText().toString();
                patientGenderStr = patientGender.getText().toString();
                patientGenderStr = patientGenderStr.toLowerCase();  // changes all letters in string to lowercase for uniformity
                patientDOBStr = patientDOB.getText().toString();

                /* need to check if hospID is an integer, if not need to issue warning to screen
                Reference: https://stackoverflow.com/questions/51231169/java-how-to-i-ensure-an-exception-is-thrown-if-user-input-is-not-an-integer-a
                Check if gender is male or female
                Check if date is in correct format
                If there are invalid inputs, a warning is printed to console and user needs to input data again
                If all inputs are valid, a Patient object is created and added to Patient Database
                */
                try {
                    int hospID = Integer.parseInt(patientHospIDStr); // convert hospID from String input to integer, which throws exception

                    // throws exception if gender is not male or female
                    // Reference: https://stackoverflow.com/questions/11027190/custom-made-exception
                    if (!"male".equals(patientGenderStr) && !"female".equals(patientGenderStr)) {
                        throw new invalidGenderException("Invalid gender. Gender can only be Male or Female");
                    }

                    // Check if patientDOBstr is in the right format and convert to Date
                    // Reference: https://stackoverflow.com/questions/36867756/unparsable-date-exception-string-to-java-sql-date
                    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                    java.util.Date d = dateFormat.parse(patientDOBStr);

                    // If all patient details are valid, check for duplicate hospID
                    if(!prematureBabies.patientExists(hospID)){
                        // Create an instance of Patient and add to local database
                        prematureBabies.addPatient(patientNameStr, hospID, patientDOBStr, patientGenderStr);

                        //Create Patient and add it to remote Database
                        Patient newPat=new Patient(hospID);
                        newPat.setName(patientNameStr);
                        newPat.setDOB(java.sql.Date.valueOf(patientDOBStr));    //patientDOBStr is converted to a Date object because setDOB method does not take in a string
                        newPat.setGender(patientGenderStr);
                        PostDataService service = ClientInstance.getRetrofitInstance().create(PostDataService.class);
                        Call<JsonObject> postCall = service.sendPatient(newPat);
                        postCall.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                JsonObject jsonObject= response.body();
                                String serverMsg=jsonObject.get("message").toString();
                                Toast.makeText(MainActivity.this, serverMsg, Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                Toast.makeText(MainActivity.this, "Patient couldn't be added to remote Database", Toast.LENGTH_SHORT).show();
                            }
                        });

                        // Remove current page
                        patientName.setVisibility(View.GONE);
                        patientHospID.setVisibility(View.GONE);
                        patientGender.setVisibility(View.GONE);
                        patientDOB.setVisibility(View.GONE);
                        saveButton.setVisibility(View.GONE);
                        viewCurrentPatientButton.setVisibility(View.GONE);

                        // Redirect to next page, which is the new Patient's page
                        spinnerPatientList.setVisibility(View.VISIBLE);
                        spinnerArray.add(patientNameStr);   // add Patient to drop-down (spinner) list
                        spinnerPatientList.setSelection(prematureBabies.getDBSize()); // set drop-down list selection to new Patient
                        saveButton.setVisibility(View.GONE);
                        tabLayout.setVisibility(View.VISIBLE);
                        tabLayout.getTabAt(0).select();
                        patientIcon.setVisibility(View.VISIBLE);
                        msg.setText(String.format(" Name: " + patientNameStr + "%n Hospital ID: " + patientHospIDStr + "%n Gender: " + patientGenderStr + "%n Date of Birth: " + patientDOBStr));
                        msg.setTextSize(14);
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Patient with this Hospital id exists!", Toast.LENGTH_SHORT).show();
                    }

                }
                catch (NumberFormatException ex) {  // catch invalid hospID
                    System.out.println("Invalid input! HospID must be a number.");
                    // error warning (pop-up)
                }
                catch (invalidGenderException e) {  // catch invalid gender
                    // error warning (pop-up)
                    System.out.println("Invalid input! Gender must be Male or Female.");
                }
                catch (ParseException e) {  // catch invalid date
                    e.printStackTrace();
                    System.out.println("Invalid date format! Date must be in the form yyyy-mm-dd");
                }

                /* else{
                    //TODO stop the line below from constantly appearing
                    //Toast.makeText(MainActivity.this, "Patient with this Hospital id exists!", Toast.LENGTH_SHORT).show();
                } */
            }

        });
    }

    public void callSpinner(){
        spinnerPatientList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    Toast.makeText(MainActivity.this, item.toString(),
                            Toast.LENGTH_SHORT).show();
                    if (spinnerPatientList.getVisibility() == View.VISIBLE) {
                        // Redirect to 'Add Patient Selected Page'
                        if (item.toString() == "Add Patient") { callNoPatientsTab();
                             }
                        // Redirect to Individual Patient Page
                        else {
                            // Remove current page
                            addPatientButton.setVisibility(View.GONE);
                            patientName.setVisibility(View.GONE);
                            patientHospID.setVisibility(View.GONE);
                            patientGender.setVisibility(View.GONE);
                            patientDOB.setVisibility(View.GONE);
                            saveButton.setVisibility(View.GONE);

                            // callPatientTab(patientDB.findPatIdx(position));

                            currentChosenSpinner = adapterView.getSelectedItemPosition();
                            currentChosenItem = adapterView.getSelectedItem().toString();

                            callPatientTab(prematureBabies.findPatientByIndex(currentChosenSpinner-1));
                        }
                    }
                }
                Toast.makeText(MainActivity.this, "Selected",
                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void callDiffTabs(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0: {
                        // Open Basic Information Tab
                        callPatientTab(prematureBabies.findPatientByIndex(currentChosenSpinner-1));
                    }
                    case 1: {
                        patientIcon.setVisibility(View.INVISIBLE);
                        msg.setTextSize(14);
                        msg.setGravity(Gravity.FILL_HORIZONTAL);

                        if (tab.getPosition() == 0) {
                            // Open Basic Information Tab
                            callPatientTab(prematureBabies.findPatientByIndex(currentChosenSpinner-1));
                        } else if (tab.getPosition() == 1) {
                            // Open Health Tab
                            callHealthTab(prematureBabies.findPatientByIndex(currentChosenSpinner-1));
                        }

                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public void callNoPatientsTab(){
        tabLayout.setVisibility(View.INVISIBLE);
        patientIcon.setVisibility(View.INVISIBLE);
        mpLineChart.setVisibility(View.INVISIBLE);
        lactate_mpLineChart.setVisibility(View.INVISIBLE);
        currentLactateLevel.setVisibility(View.INVISIBLE);
        commentSpace.setVisibility(View.INVISIBLE);
        commentsMade.setVisibility(View.INVISIBLE);
        saveCommentButton.setVisibility(View.INVISIBLE);
        downloadGlucose.setVisibility(View.INVISIBLE);
        downloadLactate.setVisibility(View.INVISIBLE);

        callWelcomePage("Refer to dropdown list above for other patients or add patient below.");
    }

    public void callPatientTab(Patient inputPatient){
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.getTabAt(0).select();
        patientIcon.setVisibility(View.VISIBLE);
        mpLineChart.setVisibility(View.INVISIBLE);
        lactate_mpLineChart.setVisibility(View.GONE);
        currentLactateLevel.setVisibility(View.GONE);
        commentSpace.setVisibility(View.GONE);
        commentsMade.setVisibility(View.GONE);
        saveCommentButton.setVisibility(View.GONE);
        downloadGlucose.setVisibility(View.INVISIBLE);
        downloadLactate.setVisibility(View.INVISIBLE);
        msg.setTextSize(14);
        msg.setGravity(Gravity.FILL_HORIZONTAL);

        int index = prematureBabies.getDBSize();
        msg.setText(String.format("%n Name: " + inputPatient.getName() + "%n Hospital ID: " + inputPatient.getHospID() + "%n Gender: " + inputPatient.getGender() + "%n Date of Birth: " + inputPatient.getDOB()));
    }

    public void callHealthTab(final Patient inputPatient){
        final Date currentTime = Calendar.getInstance().getTime();
        final String[] commentsToPrint = {""};

        patientIcon.setVisibility(View.INVISIBLE);

        mpLineChart.setVisibility(View.VISIBLE);
        lactate_mpLineChart.setVisibility(View.VISIBLE);
        downloadGlucose.setVisibility(View.VISIBLE);
        downloadLactate.setVisibility(View.VISIBLE);

        msg.setText(String.format("Current glucose level: "));
        currentLactateLevel.setVisibility(View.VISIBLE);
        currentLactateLevel.setTextSize(14);
        currentLactateLevel.setText("Current lactate level: ");

        commentsMade.setVisibility(View.VISIBLE);
        commentsMade.setTextSize(14);
        if(inputPatient.getNumberOfComment() == 0){
            commentsMade.setText("");
        }
        else {
            commentsToPrint[0] = "";
            for(int i=0; i<inputPatient.getNumberOfComment();i++){
                commentsToPrint[0] = commentsToPrint[0] + "\n" + inputPatient.getCommentByIndex(i);
            }
            commentsMade.setText(commentsToPrint[0]);
        }

        commentSpace.setVisibility(View.VISIBLE);
        saveCommentButton.setVisibility(View.VISIBLE);
        saveCommentButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inputPatient.addComment(parseDateTime(currentTime) + " " + commentSpace.getText().toString());
                if(inputPatient.getNumberOfComment() == 0){
                    commentsMade.setText("Comments: ");
                }
                else{
                    commentsMade.setVisibility(View.VISIBLE);
                    commentsMade.setTextSize(14);
                    commentsToPrint[0] = "";
                    for(int i=0; i<inputPatient.getNumberOfComment();i++){
                        commentsToPrint[0] = commentsToPrint[0] + "\n" + inputPatient.getCommentByIndex(i);
                    }
                    commentsMade.setText(commentsToPrint[0]);
                }
            }
        });

        commentsMade.setVisibility(View.VISIBLE);

        mpLineChart.setData(graphPlot.getData());
        mpLineChart.invalidate();
        lactate_mpLineChart.setData(graphPlot.getData());
        lactate_mpLineChart.invalidate();

    }

    public String parseDateTime(Date currentDateTime){
        String[] parsed = currentDateTime.toString().split(" ");
        String updated = parsed[1] + " " + parsed[2] + " " + parsed[5] + " " + parsed[3];
        return updated;
    }

    // Method to create an alert/warning on screen if invalid details are entered
    // Reference: https://stackoverflow.com/questions/45177044/alertdialog-cannot-resolve-symbol
    public void createAlertDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Invalid Data Input"); // alert title
        alertDialog.setMessage("Alert message to be shown");    // alert message
        // text on alert button, which will close the alert when clicked
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Close",
                new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
        });
        alertDialog.show();
        }
}

