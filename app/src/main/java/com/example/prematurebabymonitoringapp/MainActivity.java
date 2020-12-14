package com.example.prematurebabymonitoringapp;

import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Initialise graph plotting parameters
    LineChart mpLineChart;
    TextFileProcessor txtFileProcessor = new TextFileProcessor();
    GraphPlotter graphPlot;
    PatientDB patientDB;
    Patient currentPatient;

    // Initialise UI components from activity_main.xml
    TabLayout tabLayout;
    ViewPager viewPager;
    pagerAdapter adapter;
    TextView msg;
    TextView list;
    EditText patientName;
    EditText patientGender;
    EditText patientDOB;
    Button addPatientButton;
    Button saveButton;
    Spinner spinnerPatientList;
    ImageView patientIcon;

    // Initialise temporary Strings to retrieve and store inputted patient info
    String patientNameStr = "Name";
    String patientGenderStr = "Male";
    String patientDOBStr = "01/01/1990";

    //To populate spinner
    List<String> spinnerArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Welcome page
        msg = findViewById(R.id.textView);
        msg.setText(String.format("Welcome to Premature Baby Monitoring App. Click button below to add patient."));
        msg.setGravity(Gravity.CENTER_HORIZONTAL);

        list = findViewById(R.id.patientList);
        list.setVisibility(View.GONE);

        patientName = findViewById(R.id.typeName);
        patientGender = findViewById(R.id.editGender);
        patientDOB = findViewById(R.id.editTextDate);

        spinnerPatientList = findViewById(R.id.spinnerPatient);
        tabLayout = findViewById(R.id.tabLayout);
        patientIcon = findViewById(R.id.icon);

        // Instantiating the patient database and adding patients
        patientDB = new PatientDB();
        patientDB.addPatient("Martin Holloway","19/11/2020","Male");
        patientDB.addPatient("James Choi","25/10/2020","Male");

        // Parse the text file to get data
        txtFileProcessor.parseFile();
        graphPlot = new GraphPlotter(txtFileProcessor.getTimeValues(), txtFileProcessor.getVoltageValues());

        mpLineChart = findViewById(R.id.line_chart);

        saveButton = findViewById(R.id.saveButton);
        addPatientButton = findViewById(R.id.button);

        addPatientButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                msg.setText(String.format("Enter patient details below:"));
                patientName.setVisibility(View.VISIBLE);
                patientGender.setVisibility(View.VISIBLE);
                patientDOB.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.VISIBLE);

                // Remove Add Patient button
                addPatientButton.setVisibility(View.GONE);
            }
        });

        // Add Patient Details Page
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Upon clicking, save inputted information
                patientNameStr = patientName.getText().toString();
                patientName.setVisibility(View.GONE);
                patientGenderStr = patientGender.getText().toString();
                patientGender.setVisibility(View.GONE);
                patientDOBStr = patientDOB.getText().toString();
                patientDOB.setVisibility(View.GONE);
                patientDB.addPatient(patientNameStr,patientDOBStr,patientGenderStr);
                msg.setText(String.format("Name: " + patientNameStr + "%n Gender: " + patientGenderStr + "%n Date of Birth: " + patientDOBStr));
                msg.setTextSize(14);
                saveButton.setVisibility(View.GONE);

                list.setVisibility(View.VISIBLE);
                list.setText("Patients in database:\n");
                for (int i=0; i<patientDB.getDBSize(); i++) {
                    list.append("Name: "+patientDB.patients.get(i).getName()+"\n");
                }

                // Redirect to next page
                spinnerPatientList.setVisibility(View.VISIBLE);
                spinnerArray.add("Patient 3 " + patientNameStr);
                spinnerPatientList.setSelection(1);
                saveButton.setVisibility(View.GONE);
                tabLayout.setVisibility(View.VISIBLE);
                patientIcon.setVisibility(View.VISIBLE);
            }
        });

        /* Display all patients in Database on screen
        TextView myText = new TextView(this);
        LinearLayout lView = new LinearLayout(this);
        for (Patient p:patientDB.patients) {
            myText.setText("Name: "+p.getName());
            lView.addView(myText);
        } */

        spinnerArray.add("None");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPatientList.setAdapter(adapter);

        // View Individual Pages
        spinnerPatientList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    Toast.makeText(MainActivity.this, item.toString(),
                            Toast.LENGTH_SHORT).show();
                    if (spinnerPatientList.getVisibility() == View.VISIBLE) {
                        // Redirect to 'No Patient Selected Page'
                        if (item.toString() == "None") {
                            tabLayout.setVisibility(View.INVISIBLE);
                            patientIcon.setVisibility(View.INVISIBLE);
                            mpLineChart.setVisibility(View.INVISIBLE);

                            msg.setText("No patient selected.");
                            msg.setGravity(Gravity.CENTER);
                            msg.setTextSize(28); }
                        // Redirect to Individual Patient Page
                        else {
                            tabLayout.setVisibility(View.VISIBLE);
                            patientIcon.setVisibility(View.VISIBLE);
                            mpLineChart.setVisibility(View.INVISIBLE);
                            msg.setTextSize(14);
                            msg.setGravity(Gravity.FILL_HORIZONTAL);

                            int index = patientDB.getDBSize();
                            Patient currentPatient = patientDB.findPatient(patientNameStr);
                            msg.setText(String.format("%n Name: " + currentPatient.getName() + "%n Gender: " + currentPatient.getGender() + "%n Date of Birth: " + currentPatient.getDOB()));
                        }
                    }
                }
                Toast.makeText(MainActivity.this, "Selected",
                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
            }
        });

        // View different tabs - Basic Information and Health
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0: {
                        // Open Basic Information Tab
                        tabLayout.setVisibility(View.VISIBLE);
                        patientIcon.setVisibility(View.VISIBLE);

                        msg.setTextSize(14);
                        msg.setGravity(Gravity.FILL_HORIZONTAL);
                        msg.setText(String.format("%n Name: " + patientNameStr + "%n Gender: " + patientGenderStr + "%n Date of Birth: " + patientDOBStr));
                        //msg.setText(String.format("%d", tab.getPosition()));
                    }
                    case 1: {
                        patientIcon.setVisibility(View.INVISIBLE);
                        msg.setTextSize(14);
                        msg.setGravity(Gravity.FILL_HORIZONTAL);
                        //msg.setText(String.format("%d", tab.getPosition()));

                        if (tab.getPosition() == 0) {
                            // Open Basic Information Tab
                            mpLineChart.setVisibility(View.INVISIBLE);
                            patientIcon.setVisibility(View.VISIBLE);
                            msg.setText(String.format("%n Name: " + patientNameStr + "%n Gender: " + patientGenderStr + "%n Date of Birth: " + patientDOBStr));

                        } else if (tab.getPosition() == 1) {
                            // Open Health Tab
                            patientIcon.setVisibility(View.INVISIBLE);
                            msg.setText(String.format("Current glucose level: "));
                            mpLineChart.setVisibility(View.VISIBLE);
                            mpLineChart.setData(graphPlot.getData());
                            mpLineChart.invalidate();
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

//        int currentPage = R.layout.activity_main;
//        int tempInt = 0;

//        setUpTabLayout(currentPage);
//        setUpTabLayout(R.layout.fragment_basic_info);
//        setUpTabLayout(R.layout.fragment_health);
//        setUpTabLayout(R.layout.fragment_others);
//        setUpViewPager();

//        setUpAdapter(setUpTabLayout(currentPage),setUpViewPager());
//        tempInt = tabbingFunction(setUpTabLayout(currentPage));
//        currentPage = tempInt;
//
//        switch(currentPage)
//        {
//            case R.layout.fragment_basic_info:
//                setUpAdapter(setUpTabLayout(R.layout.fragment_basic_info),setUpViewPager());
//                tempInt = tabbingFunction(setUpTabLayout(R.layout.fragment_basic_info));
//            case R.layout.fragment_health:
//                setUpAdapter(setUpTabLayout(R.layout.fragment_health),setUpViewPager());
//                tempInt = tabbingFunction(setUpTabLayout(R.layout.fragment_health));
//            case R.layout.fragment_others:
//                setUpAdapter(setUpTabLayout(R.layout.fragment_others),setUpViewPager());
//                tempInt = tabbingFunction(setUpTabLayout(R.layout.fragment_others));
//        }

//        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.patients_array, android.R.layout.simple_spinner_item);
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // Apply the adapter to the spinner
//        spinnerPatientList.setAdapter(adapter);

    //tabLayout = (TabLayout) findViewById(R.id.tabLayout);
//        tabLayout.addTab(tabLayout.newTab().setText("Basic Information"));
//        tabLayout.addTab(tabLayout.newTab().setText("Health"));
//        tabLayout.addTab(tabLayout.newTab().setText("Others"));
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

    //msg = findViewById(R.id.textView);
    //patientName = findViewById(R.id.typeName);
    //saveButton = findViewById(R.id.button);

//        saveButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // Code here executes on main thread after user presses button
//                patientNameStr = patientName.getText().toString();
//                patientName.setVisibility(View.GONE);
//                msg.setText(patientNameStr);
//            }
//        });

//        viewPager = (ViewPager) findViewById(R.id.viewPager);
//        viewPager.setVisibility(View.VISIBLE);
//        adapter = new pagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
//        viewPager.setAdapter(adapter);
//
//        // Create an initial view to display; must be a subclass of FrameLayout.
////        LayoutInflater inflater = getLayoutInflater();
////        final FrameLayout v0 = (FrameLayout) inflater.inflate (R.layout.fragment_basic_info, null);
////        final FrameLayout v1 = (FrameLayout) inflater.inflate (R.layout.fragment_health, null);
////        final FrameLayout v2 = (FrameLayout) inflater.inflate (R.layout.fragment_others, null);
////        adapter.addView (v0, 0);
////        adapter.addView (v1, 0);
////        adapter.addView (v2, 2);
////        adapter.notifyDataSetChanged();
//
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                //viewPager.setCurrentItem(tab.getPosition());
//                switch(tab.getPosition()){
//                    case 0:
//                        //msg.setVisibility(View.VISIBLE);
//                        msg.setText(String.format(patientNameStr + " %d",tab.getPosition()));
//                        //patientName.setVisibility(View.VISIBLE);
//                        //clickBasicInfoTab();
//                        //textView.setText('0');
//                        //setContentView(R.layout.fragment_basic_info);
//
//                    case 1:
//                        //msg.setVisibility(View.VISIBLE);
//                        msg.setText(String.format(patientNameStr + "%d",tab.getPosition()));
//                        //clickHealthTab();
//                        //textView.setText('1');
//                        //setContentView(R.layout.fragment_health);
////                        TabLayout tabLayout1 = (TabLayout) findViewById(R.id.tabLayout2);
////                        tabLayout1.addTab(tabLayout.newTab().setText("Basic Information"));
////                        tabLayout1.addTab(tabLayout.newTab().setText("Health"));
////                        tabLayout1.addTab(tabLayout.newTab().setText("Others"));
////                        tabLayout1.setTabGravity(TabLayout.GRAVITY_FILL);
//                    case 2:
//                        msg.setText(String.format(patientNameStr + "%d",tab.getPosition()));
//                        //msg.setVisibility(View.INVISIBLE);
//                        //patientName.setVisibility(View.INVISIBLE);
//                        //clickOthersTab();
//                        //textView.setText('2');
//                        //setContentView(R.layout.fragment_basic_info);
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });


//    }

//    public void clickHealthTab(){
//        String b = "You've clicked to view more health info!";
//        msg.setText(b);
//    }
//
//    public void clickBasicInfoTab(){
//        String a = "You've clicked for basic info!";
//        msg.setText(a);
//    }
//
//    public void clickOthersTab(){
//        String c = "You've clicked others!";
//        msg.setText(c);
//    }

//    protected TabLayout setUpTabLayout(int chosenLayout){
//        setContentView(chosenLayout);
//        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
//        tabLayout.addTab(tabLayout.newTab().setText("Basic Information"));
//        tabLayout.addTab(tabLayout.newTab().setText("Health"));
//        tabLayout.addTab(tabLayout.newTab().setText("Others"));
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//        return tabLayout;
//    }
//
//    protected ViewPager setUpViewPager(){
//        viewPager = (ViewPager) findViewById(R.id.viewPager);
//        viewPager.setVisibility(View.VISIBLE);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        return viewPager;
//    }
//
//    protected pagerAdapter setUpAdapter(TabLayout tabLayout, ViewPager viewPager){
//        adapter = new pagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
//        viewPager.setAdapter(adapter);
//        return adapter;
//    }
//
//    protected int tabbingFunction(TabLayout tabLayout){
//        final int[] tabPos = new int[1];
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                //viewPager.setCurrentItem(tab.getPosition());
//                tabPos[0] = tab.getPosition();
//                switch(tab.getPosition()){
//                    case 0:
//                        setContentView(R.layout.fragment_basic_info);
//                    case 1:
//                        setContentView(R.layout.fragment_health);
//                    case 2:
//                        setContentView(R.layout.fragment_others);
//                }
////                setUpTabLayout(tabPos[0]);
////                setUpViewPager();
////                setUpAdapter(setUpTabLayout(tabPos[0]),setUpViewPager());
////                tabbingFunction(setUpTabLayout(tabPos[0]));
//            }
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });
//
//        switch(tabPos[0]){
//            case 0:
//                return(R.layout.fragment_basic_info);
//            case 1:
//                return(R.layout.fragment_health);
//            case 2:
//                return(R.layout.fragment_others);
//            default:
//                return(R.layout.activity_main);
//        }
//    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to add a view to the ViewPager.
    public void addView(View newPage) {
        int pageIndex = adapter.addView(newPage);
        // You might want to make "newPage" the currently displayed page:
        viewPager.setCurrentItem(pageIndex, true);
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to remove a view from the ViewPager.
    public void removeView(View defunctPage) {
        int pageIndex = adapter.removeView(viewPager, defunctPage);
        // You might want to choose what page to display, if the current page was "defunctPage".
        if (pageIndex == adapter.getCount())
            pageIndex--;
        viewPager.setCurrentItem(pageIndex);
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to get the currently displayed page.
    public View getCurrentPage() {
        return adapter.getView(viewPager.getCurrentItem());
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to set the currently displayed page.  "pageToShow" must
    // currently be in the adapter, or this will crash.
    public void setCurrentPage(View pageToShow) {
        viewPager.setCurrentItem(adapter.getItemPosition(pageToShow), true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
