package com.example.prematurebabymonitoringapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.prematurebabymonitoringapp.exceptions.invalidGenderException;
import com.example.prematurebabymonitoringapp.network.ClientInstance;
import com.example.prematurebabymonitoringapp.network.GetDataService;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.sql.Date;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Initialise graph plotting parameters
    LineChart mpLineChart;
    TextFileProcessor txtFileProcessor = new TextFileProcessor();
    GraphPlotter graphPlot;

    // Initialise UI components from activity_main.xml
    TabLayout tabLayout;
    ViewPager viewPager;
    pagerAdapter adapter;
    TextView msg;
    TextView list;
    EditText patientName;
    EditText patientHospID;
    EditText patientGender;
    EditText patientDOB;
    Button addPatientButton;
    Button viewCurrentPatientButton;
    Button saveButton;
    Spinner spinnerPatientList;
    ImageView patientIcon;

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
        prematureBabies.addPatient("Martin Holloway",27863,"2020-12-12","Male");
        spinnerArray.add(prematureBabies.lastPatient().getName());
        prematureBabies.addPatient("James Choi",52839,"2020-12-27","Male");
        spinnerArray.add(prematureBabies.lastPatient().getName());
        prematureBabies.addPatient("John Smith", 01, "2020-11-27", "Male");
        patientNameStr = prematureBabies.lastPatient().getName();
        spinnerArray.add(String.format(prematureBabies.lastPatient().getName()));

        //Fetch Patient List from remote Database (server)
        //TODO Add meaningful logs for when the request fails
        GetDataService service = ClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Patient>> call = service.getPatientsList();
        call.enqueue(new Callback<List<Patient>>() {
            @Override
            public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
                List<Patient> patientList=response.body();
                System.out.println("Good");
                for (Patient newPat:patientList){
                    prematureBabies.addPatient(newPat);
                    spinnerArray.add(prematureBabies.lastPatient().getName());
                    System.out.println(newPat.getName());
                }
            }

            @Override
            public void onFailure(Call<List<Patient>> call, Throwable t) {
                //Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                System.out.println("Bad");
            }
        });

        // Welcome page
        callWelcomePage("Welcome to the Premature Baby Monitoring App./n Click button below to add patient.");

        viewCurrentPatientButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Redirect to Add Patient Details Page
                callWelcomePage("Refer to dropdown list above for other patients or add patient below.");

                // Remove Add Patient button
                viewCurrentPatientButton.setVisibility(View.GONE);
            }
        });

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

        list = findViewById(R.id.patientList);
        list.setVisibility(View.GONE);

        viewCurrentPatientButton = findViewById(R.id.button2); // Welcome page

        // Components in 'Add Patient Details' Page
        patientName = findViewById(R.id.typeName); // Add Patient Name text
        patientHospID = findViewById(R.id.typeHospID); // Add Patient HospID text
        patientGender = findViewById(R.id.editGender); // Add Patient Gender text
        patientDOB = findViewById(R.id.editTextDate); // Add Patient DOB text

        spinnerPatientList = findViewById(R.id.spinnerPatient);
        tabLayout = findViewById(R.id.tabLayout);
        patientIcon = findViewById(R.id.icon);

        // View Individual Pages
        spinnerPatientList = findViewById(R.id.spinnerPatient); // View individual pages
        tabLayout = findViewById(R.id.tabLayout); // View individual pages
        patientIcon = findViewById(R.id.icon); // View individual pages - basic info tab
        mpLineChart = findViewById(R.id.line_chart); // View individual pages - health tab
        saveButton = findViewById(R.id.saveButton); // View individual pages - health tab
    }

    public void callWelcomePage(String printText){
        // Ensure other components aren't on the page
        patientName.setVisibility(View.GONE);
        patientHospID.setVisibility(View.GONE);
        patientGender.setVisibility(View.GONE);
        patientDOB.setVisibility(View.GONE);

        //spinnerPatientList.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.GONE);
        patientIcon.setVisibility(View.GONE);
        mpLineChart.setVisibility(View.GONE);
        saveButton.setVisibility(View.GONE);

        msg.setVisibility(View.VISIBLE);
        addPatientButton.setVisibility(View.VISIBLE);
        viewCurrentPatientButton.setVisibility(View.VISIBLE);

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
                System.out.println(patientGenderStr);
                patientDOBStr = patientDOB.getText().toString();

                /* need to check if hospID is an integer, if not need to issue warning to screen
                Reference: https://stackoverflow.com/questions/51231169/java-how-to-i-ensure-an-exception-is-thrown-if-user-input-is-not-an-integer-a
                Check if gender is male or female
                Check if date is in correct format
                */
                try {
                    int hospID = Integer.parseInt(patientHospIDStr); // convert hospID from String input to integer, which throws exception

                    // throws exception if gender is not male or female
                    // Reference: https://stackoverflow.com/questions/11027190/custom-made-exception
                    if (!"Male".equals(patientGenderStr) && !"Female".equals(patientGenderStr))
                    {
                        throw new invalidGenderException("Invalid gender. Gender can only be Male or Female");
                    }
                    // Create an instance of Patient and add to database (if all data is input correctly)
                    prematureBabies.addPatient(patientNameStr, hospID, patientDOBStr, patientGenderStr);

                    // Remove current page
                    patientName.setVisibility(View.GONE);
                    patientHospID.setVisibility(View.GONE);
                    patientGender.setVisibility(View.GONE);
                    patientDOB.setVisibility(View.GONE);
                    saveButton.setVisibility(View.GONE);
                    viewCurrentPatientButton.setVisibility(View.GONE);
                    // Redirect to next page, which is the new Patient's page
                    spinnerPatientList.setVisibility(View.VISIBLE);
                    spinnerArray.add(patientNameStr);   // add Patient to drop-down list
                    spinnerPatientList.setSelection(prematureBabies.getDBSize());
                    saveButton.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.VISIBLE);
                    tabLayout.getTabAt(0).select();
                    patientIcon.setVisibility(View.VISIBLE);

                    // Update spinner with patient database
                    // spinnerArray.add(String.format("Patient %d " + patientNameStr, prematureBabies.getDBSize()));
                    spinnerPatientList.setSelection(prematureBabies.getDBSize());
                    msg.setText(String.format(" Name: " + patientNameStr + "%n Hospital ID: " + patientHospIDStr + "%n Gender: " + patientGenderStr + "%n Date of Birth: " + patientDOBStr));
                    msg.setTextSize(14);
                }
                catch (NumberFormatException ex) // If exception, issue warning and try again.
                {
                    System.out.println("Invalid input! You have to enter a number");
                    // error warning (pop-up)
                    callNewPatientPage();   // retry entering patient details
                }
                catch (invalidGenderException e)
                {
                    // error warning (pop-up)
                    System.out.println("Invalid input! Gender must be Male or Female");
                    callNewPatientPage();   // retry entering patient details
                }

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
                            callHealthTab();
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

        callWelcomePage("Refer to dropdown list above for other patients or add patient below.");
    }

    public void callPatientTab(Patient inputPatient){
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.getTabAt(0).select();
        patientIcon.setVisibility(View.VISIBLE);
        mpLineChart.setVisibility(View.INVISIBLE);
        msg.setTextSize(14);
        msg.setGravity(Gravity.FILL_HORIZONTAL);

        int index = prematureBabies.getDBSize();
        msg.setText(String.format("%n Name: " + inputPatient.getName() + "%n Hospital ID: " + inputPatient.getHospID() + "%n Gender: " + inputPatient.getGender() + "%n Date of Birth: " + inputPatient.getDOB()));
    }

    public void callHealthTab(){
        patientIcon.setVisibility(View.INVISIBLE);
        msg.setText(String.format("Current glucose level: "));
        mpLineChart.setVisibility(View.VISIBLE);
        mpLineChart.setData(graphPlot.getData());
        mpLineChart.invalidate();
    }
}
