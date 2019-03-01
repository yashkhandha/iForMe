package edu.monash.iforme;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;

/**
 * MedicalTabs class to handle tabbed activity
 */
public class MedicalTabs extends AppCompatActivity {

    private static final String TAG = "MedicalTabs";
    //Using SectionsPageAdapter class to handle page changes and manage state
    private SectionsPageAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    /**
     * To load the layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //if the SDK version is grater than 19, make the app bar transparent
        if(Build.VERSION.SDK_INT >19){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else{
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.activity_medical_tabs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      /*  Intent in = getIntent();
        Intent in1 = getIntent();

        Medication med = in.getParcelableExtra("selectedMedication");
        Reminder reminder = in1.getParcelableExtra("reminder");*/
        //to listen to tab change click
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Initialise the adapter with the fragment manager
        mSectionsPagerAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.container);
        setupViewPager(mViewPager);
        //Get reference for the tabs in the activity
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * Method to add fragments to adapter and setting the adapter to the View pager.
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new MedicalTabs1(),"Medication");
        adapter.addFragment(new MedicalTabs2(),"Reminder");
        viewPager.setAdapter(adapter);
    }

}
