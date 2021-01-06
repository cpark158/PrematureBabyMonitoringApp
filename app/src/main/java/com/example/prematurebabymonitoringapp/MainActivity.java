package com.example.prematurebabymonitoringapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.mikephil.charting.charts.LineChart;
import android.text.Editable;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import com.google.android.material.resources.TextAppearance;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import java.util.Calendar;
import java.io.*;
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
    ViewPager viewPager;
    pagerAdapter adapter;
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
    String patientDOBStr = "01/01/1990";

    int currentChosenSpinner = 1;
    String currentChosenItem = " ";

    // To store patients
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


        // Instantiating the patient database and adding existing patients
        prematureBabies.addPatient("Martin Holloway","27682","19/11/2020","Male");
        spinnerArray.add("Patient "+prematureBabies.getDBSize()+": "+ prematureBabies.lastPatient().getHospID());
        prematureBabies.addPatient("James Choi","52839","25/10/2020","Male");
        spinnerArray.add("Patient "+prematureBabies.getDBSize()+": "+ prematureBabies.lastPatient().getHospID());

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
                patientDOB.setText("Date of Birth");
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
        patientDOB.setText("Date of Birth");
        patientGender.setText("Gender");

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Upon clicking, save inputted information
                patientNameStr = patientName.getText().toString();
                patientHospIDStr = patientHospID.getText().toString();
                patientGenderStr = patientGender.getText().toString();
                patientDOBStr = patientDOB.getText().toString();

                // Create an instance of Patient and add to database
                prematureBabies.addPatient(patientNameStr,patientHospIDStr,patientDOBStr,patientGenderStr);

                // Remove current page
                patientName.setVisibility(View.GONE);
                patientHospID.setVisibility(View.GONE);
                patientGender.setVisibility(View.GONE);
                patientDOB.setVisibility(View.GONE);
                saveButton.setVisibility(View.GONE);
                viewCurrentPatientButton.setVisibility(View.GONE);

                // Update spinner with patient database
                spinnerArray.add("Patient "+prematureBabies.getDBSize()+": "+ patientHospIDStr);
                spinnerPatientList.setSelection(prematureBabies.getDBSize());

                // Redirect to next page, which is the new Patient's page
                spinnerPatientList.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);
                tabLayout.getTabAt(0).select();
                patientIcon.setVisibility(View.VISIBLE);
                msg.setText(String.format(" Name: " + patientNameStr + "%n Hospital ID: " + patientHospIDStr + "%n Gender: " + patientGenderStr + "%n Date of Birth: " + patientDOBStr));
                msg.setTextSize(14);
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
}
