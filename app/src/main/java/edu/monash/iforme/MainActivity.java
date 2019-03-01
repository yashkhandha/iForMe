package edu.monash.iforme;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * MainActivity class is loaded first after the user signin in the application
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //UI references
    FloatingActionButton fab;
    //Drawer view
    TextView mDrawerNameTextView;
    //status to capture if the user has signed out or loaded the tour again
    boolean status;

    /**
     * onCreate to load the layout linked to the file i.e. fragment in this case and set the UI elements
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Get the toolbar reference from UI
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //initialize status to true as user is still logged in.
        status = true;

        //if the SDK version is grater than 19, make the app bar transparent
        if(Build.VERSION.SDK_INT >19){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else{
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        //fetch UI reference for the floating action button
        fab = (FloatingActionButton) findViewById(R.id.fabButton);

        //set the toolbar title
        getSupportActionBar().setTitle("iForMe");
        //load the main fragment on loggin on to home screen
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new
                MainFragment()).commit();

        //Drawer Layout to manage the navigation drawer in the layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //load the navigation view in the layout
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    /**
     * method used by all fragments to set the title for the toolbar
     * @param title
     */
    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    /**
     * Manage click to show navigation drawer on the screen
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * method called first when the menu is created for the navigation drawer
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Method to identify which option is chosen form the navigation drawer and navigate to appropriate views
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment nextFragment = null;

        //if the item clicked is for the Main Fragment
        if (id == R.id.main_fragment) {
            fab.setVisibility(View.GONE);
                nextFragment = new MainFragment();
        }
        //Emergency calls Fragment
        else if (id == R.id.nav_emergency) {
            nextFragment = new EmergencyCalls();
            fab.setVisibility(View.GONE);
        }
        //Medical Fragment
        else if (id == R.id.nav_medical) {
            nextFragment = new MedicalFragment();
            fab.setVisibility(View.VISIBLE);
        }
        //Park and find car Fragment
        else if (id == R.id.nav_maps) {
            fab.setVisibility(View.GONE);
            nextFragment = new MapsFragment();
        }
        //Game fragment
        else if (id == R.id.nav_game) {
            fab.setVisibility(View.GONE);
            nextFragment = new GameFragment();
        }
        //Take a tour of functionalities of hte app
        else if (id == R.id.takeTour) {
            fab.setVisibility(View.GONE);
            new edu.monash.iforme.PreferenceManager(this).clearPreference();
            startActivity(new Intent(this,WelcomeActivity.class));
            //set status false to leave main activity and finish
            status = false;
            finish();
        }
        //Sign out
        else if (id == R.id.sign_out) {
            fab.setVisibility(View.GONE);
            //set status false to leave main activity and finish
            status = false;
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this,LoginActivity.class));

        }

        //if one of the fragment is clicked. i.e. MainActivity is still active
        if(status) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame,
                    nextFragment).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
