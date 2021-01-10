package com.example.prematurebabymonitoringapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.prematurebabymonitoringapp.exceptions.invalidGenderException;
import com.example.prematurebabymonitoringapp.network.ClientInstance;
import com.example.prematurebabymonitoringapp.network.GetDataService;
import com.example.prematurebabymonitoringapp.network.PostDataService;
import com.github.mikephil.charting.charts.LineChart;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.tabs.TabLayout;
import java.util.Calendar;

import com.google.firebase.storage.FirebaseStorage;
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

import android.app.DownloadManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    // Initialise graph plotting parameters
    LineChart mpLineChart;
    LineChart lactate_mpLineChart;
    TextFileProcessor txtFileProcessor = new TextFileProcessor();
    GraphPlotter graphPlot;

    //Initialising reference to file in firebase storage for download
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference fileRef = storageRef.child("Monitoring_20190731_135114.txt");

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
    EditText enterFilename;
    Button downloadData;

    EditText patientWeight;
    EditText patientTOB;
    EditText patientMotherName;
    EditText patientFatherName;
    EditText patientContact;
    EditText patientConditions;
    Button saveName;
    Button saveHospID;
    Button saveGender;
    Button saveDOB;
    Button saveWeight;
    Button saveTOB;
    Button saveMotherName;
    Button saveFatherName;
    Button saveContact;
    Button saveCondition;

    // Initialise temporary Strings to retrieve and store inputted patient info
    String patientNameStr = "Name";
    String patientHospIDStr = "0";
    String patientGenderStr = "Male";
    String patientDOBStr = "1990-01-01";
    String patientWeightStr;
    String patientTOBStr;
    String patientMotherNameStr;
    String patientFatherNameStr;
    String patientContactStr;
    String patientConditionsStr;
    String saveNameStr;
    String saveHospIDStr;
    String saveGenderStr;
    String saveDOBStr;
    String saveWeightStr;
    String saveTOBStr;
    String saveMotherNameStr;
    String saveFatherNameStr;
    String saveContactStr;
    String saveConditionStr;

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
        downloadData = findViewById(R.id.downloadData);

        // Add/Import existing patients from here onwards
        prematureBabies.addPatient("Martin Holloway",27863,"2020-12-12","male");
        spinnerArray.add(Integer.toString(prematureBabies.lastPatient().getHospID()));
        prematureBabies.addPatient("James Choi",52839,"2020-12-27","male");
        spinnerArray.add(Integer.toString(prematureBabies.lastPatient().getHospID()));
        prematureBabies.addPatient("John Smith", 33678, "2020-11-27", "male");
        patientNameStr = prematureBabies.lastPatient().getName();
        patientHospIDStr = Integer.toString(prematureBabies.lastPatient().getHospID());
        spinnerArray.add(Integer.toString(prematureBabies.lastPatient().getHospID()));

        //TODO Add meaningful logs for when the request fails
        //Fetch Patient List from remote Database
        /* Reference 3 - taken from https://medium.com/@prakash_pun/retrofit-a-simple-android-tutorial-48437e4e5a23 */
        GetDataService service = ClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Patient>> call = service.getPatientsList();
        call.enqueue(new Callback<List<Patient>>() {
            @Override
            public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
                List<Patient> patientList=response.body();
                for (Patient newPat:patientList) {
                    if (!prematureBabies.patientExists(newPat.getHospID())) {
                        prematureBabies.addPatient(newPat);
                        spinnerArray.add(Integer.toString(prematureBabies.lastPatient().getHospID()));
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

        lactate_mpLineChart = findViewById(R.id.lactate_line_chart);
        currentLactateLevel = findViewById(R.id.lactateText);
        commentSpace = findViewById(R.id.commentSpace);
        commentsMade = findViewById(R.id.commentsMade);
        saveCommentButton = findViewById(R.id.saveCommentButton);

        downloadGlucose = findViewById(R.id.downloadData);
        downloadLactate = findViewById(R.id.downloadLactate);
        enterFilename = findViewById(R.id.enterFilename);

        // Add Details Page
        patientWeight = findViewById(R.id.editWeight);
        patientTOB = findViewById(R.id.editTOB);
        patientMotherName = findViewById(R.id.editMotherName);
        patientFatherName = findViewById(R.id.editFatherName);
        patientContact = findViewById(R.id.editContact);
        patientConditions = findViewById(R.id.editConditions);
        saveName = findViewById(R.id.saveName);
        saveHospID = findViewById(R.id.saveHospID);
        saveGender = findViewById(R.id.saveGender);
        saveDOB = findViewById(R.id.saveDOB);
        saveWeight = findViewById(R.id.saveWeight);
        saveTOB = findViewById(R.id.saveTOB);
        saveMotherName = findViewById(R.id.saveMotherName);
        saveFatherName = findViewById(R.id.saveFatherName);
        saveContact = findViewById(R.id.saveContact);
        saveCondition = findViewById(R.id.saveCondition);
    }

    public void callWelcomePage(String printText){
        // Ensure other components aren't on the page
        clearPage();

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
                        prematureBabies.lastPatient().setCondition(" ");
                        prematureBabies.lastPatient().setContactNum(" ");
                        prematureBabies.lastPatient().setFatherName(" ");
                        prematureBabies.lastPatient().setMotherName(" ");
                        prematureBabies.lastPatient().setTimeOfBirth(" ");
                        patientWeightStr = "";

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
                        spinnerArray.add(patientHospIDStr);   // add Patient to drop-down (spinner) list
                        spinnerPatientList.setSelection(prematureBabies.getDBSize()); // set drop-down list selection to new Patient
                        saveButton.setVisibility(View.GONE);
                        tabLayout.setVisibility(View.VISIBLE);
                        tabLayout.getTabAt(0).select();
                        patientIcon.setVisibility(View.VISIBLE);
                        msg.setVisibility(View.VISIBLE);
                        msg.setText(String.format(" Name: " + patientNameStr + "%n Hospital ID: " + patientHospIDStr + "%n Gender: " + patientGenderStr + "%n Date of Birth: " + patientDOBStr));
                        msg.setTextSize(14);
                    }
                    else{
                        createDuplicateHospIDAlert();
                    }

                }
                catch (NumberFormatException ex) {  // catch invalid hospID
                    System.out.println("Invalid input! HospID must be a number.");
                    createAlertDialog();    // error warning (pop-up)
                }
                catch (invalidGenderException e) {  // catch invalid gender
                    createAlertDialog();    // error warning (pop-up)
                    System.out.println("Invalid input! Gender must be Male or Female.");
                }
                catch (ParseException e) {  // catch invalid date
                    createAlertDialog();    // error warning (pop-up)
                    System.out.println("Invalid date format! Date must be in the form yyyy-mm-dd");
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

                            callPatientTab(prematureBabies.findPatient(currentChosenSpinner-1));
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
                        callPatientTab(prematureBabies.findPatient(currentChosenSpinner-1));
                    }
                    case 1: {
                        patientIcon.setVisibility(View.INVISIBLE);
                        msg.setVisibility(View.VISIBLE);
                        msg.setTextSize(14);
                        msg.setGravity(Gravity.FILL_HORIZONTAL);

                        if (tab.getPosition() == 0) {
                            // Open Basic Information Tab
                            callPatientTab(prematureBabies.findPatient(currentChosenSpinner-1));
                        } else if (tab.getPosition() == 1) {
                            // Open Health Tab
                            callHealthTab(prematureBabies.findPatient(currentChosenSpinner-1));
                        } else if (tab.getPosition() == 2) {
                            callEditDetailsPage(prematureBabies.findPatient(currentChosenSpinner-1));
                        } else if (tab.getPosition() == 3) {
                            removePatientConfirmation(prematureBabies.findPatient(currentChosenSpinner-1));
                        }
                    }
                    case 2: {
                        if (tab.getPosition() == 0) {
                            // Open Basic Information Tab
                            callPatientTab(prematureBabies.findPatient(currentChosenSpinner-1));
                        } else if (tab.getPosition() == 1) {
                            // Open Health Tab
                            callHealthTab(prematureBabies.findPatient(currentChosenSpinner-1));
                        } else if (tab.getPosition() == 2) {
                            callEditDetailsPage(prematureBabies.findPatient(currentChosenSpinner-1));
                        } else if (tab.getPosition() == 3) {
                            removePatientConfirmation(prematureBabies.findPatient(currentChosenSpinner-1));
                        }
                    }
                    case 3: {
                        if (tab.getPosition() == 0) {
                            // Open Basic Information Tab
                            callPatientTab(prematureBabies.findPatient(currentChosenSpinner-1));
                        } else if (tab.getPosition() == 1) {
                            // Open Health Tab
                            callHealthTab(prematureBabies.findPatient(currentChosenSpinner-1));
                        } else if (tab.getPosition() == 2) {
                            callEditDetailsPage(prematureBabies.findPatient(currentChosenSpinner-1));
                        } else if (tab.getPosition() == 3) {
                            removePatientConfirmation(prematureBabies.findPatient(currentChosenSpinner-1));
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
        clearEditDetailsPage();
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
        enterFilename.setVisibility(View.INVISIBLE);

        callWelcomePage("Refer to dropdown list above for other patients or add patient below.");
    }

    public void callPatientTab(Patient inputPatient){
        clearEditDetailsPage();
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
        enterFilename.setVisibility(View.INVISIBLE);
        msg.setVisibility(View.VISIBLE);
        msg.setTextSize(14);
        msg.setGravity(Gravity.FILL_HORIZONTAL);

        int index = prematureBabies.getDBSize();
        printPatientDetails(inputPatient);
    }

    public void callHealthTab(final Patient inputPatient){
        final Date currentTime = Calendar.getInstance().getTime();
        final String[] commentsToPrint = {""};

        clearEditDetailsPage();
        tabLayout.setVisibility(View.VISIBLE);
        patientIcon.setVisibility(View.INVISIBLE);
        mpLineChart.setVisibility(View.VISIBLE);
        lactate_mpLineChart.setVisibility(View.VISIBLE);
        downloadGlucose.setVisibility(View.VISIBLE);
        downloadLactate.setVisibility(View.INVISIBLE);
        enterFilename.setVisibility(View.VISIBLE);
        msg.setVisibility(View.VISIBLE);

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

        downloadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = enterFilename.getText().toString();
                try {
                    downloadFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        downloadLactate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String filename = enterFilename.getText().toString();
            }
        });
        downloadGlucose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String filename = enterFilename.getText().toString();
            }
        });

    }

    public void callEditDetailsPage(final Patient inputPatient){
        clearPage();
        tabLayout.setVisibility(View.VISIBLE);
        spinnerPatientList.setVisibility(View.VISIBLE);

        patientName.setVisibility(View.GONE);
        patientHospID.setVisibility(View.GONE);
        patientGender.setVisibility(View.GONE);
        patientDOB.setVisibility(View.GONE);

        patientWeight.setVisibility(View.VISIBLE);
        patientTOB.setVisibility(View.VISIBLE);
        patientMotherName.setVisibility(View.VISIBLE);
        patientFatherName.setVisibility(View.VISIBLE);
        patientContact.setVisibility(View.VISIBLE);
        patientConditions.setVisibility(View.VISIBLE);

        saveName.setVisibility(View.GONE);
        saveHospID.setVisibility(View.GONE);
        saveGender.setVisibility(View.GONE);
        saveDOB.setVisibility(View.GONE);

        saveWeight.setVisibility(View.VISIBLE);
        saveTOB.setVisibility(View.VISIBLE);
        saveMotherName.setVisibility(View.VISIBLE);
        saveFatherName.setVisibility(View.VISIBLE);
        saveContact.setVisibility(View.VISIBLE);
        saveCondition.setVisibility(View.VISIBLE);

        msg.setVisibility(View.GONE);
//        patientName.setText("Name");
//        patientHospID.setText("Hospital ID");
//        patientDOB.setText("yyyy-mm-dd");
//        patientGender.setText("Gender");

        saveWeight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                patientWeightStr = patientWeight.getText().toString();
                inputPatient.setWeight(Double.parseDouble(patientWeightStr));
            }
        });
        saveTOB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                patientTOBStr = patientTOB.getText().toString();
                inputPatient.setTimeOfBirth(patientTOBStr);
            }
        });
        saveMotherName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                patientMotherNameStr = patientMotherName.getText().toString();
                inputPatient.setMotherName(patientMotherNameStr);
            }
        });
        saveFatherName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                patientFatherNameStr = patientFatherName.getText().toString();
                inputPatient.setFatherName(patientFatherNameStr);
            }
        });
        saveContact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                patientContactStr = patientContact.getText().toString();
                inputPatient.setContactNum(patientContactStr);
            }
        });
        saveCondition.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                patientConditionsStr = patientConditions.getText().toString();
                inputPatient.setCondition(patientConditionsStr);
            }
        });
    }

    public void printPatientDetails(Patient inputPatient){
        String basicInfoStr = "";
        basicInfoStr = basicInfoStr + "ID: " + Integer.toString(inputPatient.getHospID()) + "\n";
        basicInfoStr = basicInfoStr + "Name: " + inputPatient.getName() + "\n";
        basicInfoStr = basicInfoStr + "Gender: " + inputPatient.getGender() + "\n";
        basicInfoStr = basicInfoStr + "Date of Birth: " + inputPatient.getDOB().toString() + "\n";
        basicInfoStr = basicInfoStr + "Weight: " + Double.toString(inputPatient.getWeight()) + "\n";
        basicInfoStr = basicInfoStr + "Time of Birth: " + inputPatient.getTimeOfBirth() + "\n";
        basicInfoStr = basicInfoStr + "Mother's Name: " + inputPatient.getMotherName() + "\n";
        basicInfoStr = basicInfoStr + "Father's Name: " + inputPatient.getFatherName() + "\n";
        basicInfoStr = basicInfoStr + "Emergency Contact: " + inputPatient.getContactNum() + "\n";
        basicInfoStr = basicInfoStr + "Conditions: " + inputPatient.getCondition() + "\n";

        msg.setVisibility(View.VISIBLE);
        msg.setTextSize(14);
        msg.setText(basicInfoStr);
    }

    public void clearPage(){
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
        enterFilename.setVisibility(View.GONE);
        patientWeight.setVisibility(View.GONE);
        patientTOB.setVisibility(View.GONE);
        patientMotherName.setVisibility(View.GONE);
        patientFatherName.setVisibility(View.GONE);
        patientContact.setVisibility(View.GONE);
        patientConditions.setVisibility(View.GONE);
        saveName.setVisibility(View.GONE);
        saveHospID.setVisibility(View.GONE);
        saveGender.setVisibility(View.GONE);
        saveDOB.setVisibility(View.GONE);
        saveWeight.setVisibility(View.GONE);
        saveTOB.setVisibility(View.GONE);
        saveMotherName.setVisibility(View.GONE);
        saveFatherName.setVisibility(View.GONE);
        saveContact.setVisibility(View.GONE);
        saveCondition.setVisibility(View.GONE);
    }

    public void clearEditDetailsPage(){
        patientName.setVisibility(View.GONE);
        patientHospID.setVisibility(View.GONE);
        patientGender.setVisibility(View.GONE);
        patientDOB.setVisibility(View.GONE);
        patientWeight.setVisibility(View.GONE);
        patientTOB.setVisibility(View.GONE);
        patientMotherName.setVisibility(View.GONE);
        patientFatherName.setVisibility(View.GONE);
        patientContact.setVisibility(View.GONE);
        patientConditions.setVisibility(View.GONE);
        saveName.setVisibility(View.GONE);
        saveHospID.setVisibility(View.GONE);
        saveGender.setVisibility(View.GONE);
        saveDOB.setVisibility(View.GONE);
        saveWeight.setVisibility(View.GONE);
        saveTOB.setVisibility(View.GONE);
        saveMotherName.setVisibility(View.GONE);
        saveFatherName.setVisibility(View.GONE);
        saveContact.setVisibility(View.GONE);
        saveCondition.setVisibility(View.GONE);
    }

    //Downloading data text files from firebase storage for parsing
    public void downloadFile() throws IOException {
        File localFile = File.createTempFile("Monitoring_20190731_135114","txt");
        fileRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                //downloaded file is stored in default android storage and can be opened with localFile variable
                Toast.makeText(MainActivity.this, "File Download Success",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(MainActivity.this, "File Download Failed",Toast.LENGTH_SHORT).show();
                //Errors will be handled here
            }
        });

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
        alertDialog.setTitle("Invalid Data Input\n"); // alert title
        alertDialog.setMessage("\nHospID must be a number. \nGender must be Male or Female. \nDate must be in the form yyyy-mm-dd.");    // alert message
        // text on alert button, which will close the alert when clicked
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Close",
                new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
        });
        alertDialog.show();
    }

    // create an alert for duplicate hospID entered
    public void createDuplicateHospIDAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Patient already exists in database\n"); // alert title
        alertDialog.setMessage("\nPlease check that HospID is not duplicated.");    // alert message
        // text on alert button, which will close the alert when clicked
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    // create an alert to confirm the removal of patient from list/database
    public void removePatientConfirmation(final Patient inputPatient) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Patient Removal Confirmation\n"); // alert title
        alertDialog.setMessage("\n Are you sure you want to remove patient " + Integer.toString(inputPatient.getHospID()) + " ?");    // alert message
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Confirm",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Remove Patient
                        spinnerPatientList.setSelection(0);
                        callNoPatientsTab();
                        spinnerArray.remove(Integer.toString(inputPatient.getHospID()));
                        prematureBabies.removePatient(inputPatient);
                    }
                });
        // text on alert button, which will close the alert when clicked
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    // create an alert to warn clinicians when parameters go below normal limits
    public void createBelowLimitAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Parameter is below normal range!\n"); // alert title
        alertDialog.setMessage("\nPlease check on patient. To restore parameter, try the following:\n(Ways to increase parameter)");    // alert message
        // text on alert button, which will close the alert when clicked
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    // create an alert to warn clinicians when parameters go above normal limits
    public void createAboveLimitAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Parameter is above normal range!\n"); // alert title
        alertDialog.setMessage("\nPlease check on patient. To restore parameter, try the following:\n(Ways to decrease parameter)");    // alert message
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