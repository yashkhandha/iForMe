package edu.monash.iforme;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/** MainFragmet to load the home screen
 * Created by yashkhandha on 2/05/2018.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    //UI references
    View vMain;
    //For storing Firebase authentication
    private FirebaseAuth mAuth;
    //TextViews for showing logged in user and weatehr of current address
    TextView mUserName,weather;
    //TextView Labels
    TextView mWelcome,mTempTextView;
    //progress bar to show the progress of elements loaded on gthe screen
    ProgressBar mProgressView;
    //ImageView to show the four images on home screen
    ImageView emergencyImage, medicineImage, parkingImage, gameImage;
    //boolean status to track whether user has logged out
    boolean status;
    //To store the address and temperature value received form client
    String address,temp;
    //To store the coordinates of the address location of the user
    String[] coordinates;
    //Get reference of home screen layout to show animations on click of screen
    RelativeLayout mRelLayout;
    //Flaoting action button
    FloatingActionButton fab;
    /**
     * onCreateView method to load basic elements on the screen and load the layout fragment_main
     * @param inflater Inflater instance
     * @param container container to hold the elements
     * @param savedInstanceState instance state
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        //set the title for the toolbar
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("iForMe");
        //Inflate the layout
        vMain = inflater.inflate(R.layout.fragment_main, container, false);
        //Get References for UI elements
        mUserName = vMain.findViewById(R.id.userNameText);
        weather = vMain.findViewById(R.id.temp);
        mWelcome = (TextView)vMain.findViewById(R.id.welcome);
        //setting it to invisible at start, wil be visible once value is fetched and ready to display
        mWelcome.setVisibility(View.GONE);
        mTempTextView = (TextView)vMain.findViewById(R.id.tempTextView);
        //setting it to invisible at start, wil be visible once value is fetched and ready to display
        mTempTextView.setVisibility(View.GONE);
        //mProgressView = vMain.findViewById(R.id.progress_bar_main_fragment);
        mRelLayout = (RelativeLayout)vMain.findViewById(R.id.relLayout);
        emergencyImage = vMain.findViewById(R.id.emergencyImage);
        medicineImage = vMain.findViewById(R.id.medicineImage);
        parkingImage = vMain.findViewById(R.id.parkImage);
        gameImage = vMain.findViewById(R.id.gameImage);
        fab = vMain.findViewById(R.id.fabButton);

        //Using github library to run the aniamtions of zoom in on all the images when loaded
        YoYo.with(Techniques.ZoomInDown).playOn(emergencyImage);
        YoYo.with(Techniques.ZoomInDown).playOn(medicineImage);
        YoYo.with(Techniques.ZoomInDown).playOn(parkingImage);
        YoYo.with(Techniques.ZoomInDown).playOn(gameImage);

        mAuth = FirebaseAuth.getInstance();
        //mProgressView.setVisibility(View.VISIBLE);
        //Load the basic information of the logged in user
        loadUserInformation();

        //Set event listeners on all the images and layout
        emergencyImage.setOnClickListener(this);
        medicineImage.setOnClickListener(this);
        parkingImage.setOnClickListener(this);
        gameImage.setOnClickListener(this);
        mRelLayout.setOnClickListener(this);
        status =true;

        return vMain;
    }

    /**
     * Method to load the logged in users information and set on the home screen
     */
    private void loadUserInformation() {
        //Get the current users instance from firebase
        FirebaseUser user = mAuth.getCurrentUser();
        //Search under Users node created in Firebase
        DatabaseReference mFirebaseDbReferenceUserChild = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference mFirebaseDbReferenceCurrentUser= mFirebaseDbReferenceUserChild.child(user.getUid());
        mFirebaseDbReferenceCurrentUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // TO GET USERDETAILS FOR CURRENT USER
                User user = dataSnapshot.getValue(User.class);
                mWelcome.setVisibility(View.VISIBLE);
                //animations to show the welcome message on home screen
                YoYo.with(Techniques.StandUp).playOn(mWelcome);
                //animations to show the username on home screen
                YoYo.with(Techniques.StandUp).playOn(mUserName);
                //Set the user name on home screen
                mUserName.setText(user.userName);

                address = user.address;

                //this method is called to load the Weather information of the users address
                //setWeather();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Method to laod the weather information on home screen
     */
//    public void setWeather(){
//        //use async task to run it in background thread
//        new AsyncTask<String,Void,String>(){
//
//            @Override
//            protected String doInBackground(String... strings) {
//                String[] urlParams = {"address"};
//                String[] urlValues = {address};
//                //call the Client class method to fetch the coordinates based on the address using google api
//                return Client.getCoOrdinates(urlParams,urlValues);
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                //receive the coordinates
//                coordinates = Client.getCoOrdinatesFetch(s);
//            }
//
//        }.execute();
//
//        //Another async task to fetch the weather based on the coordinates using OpenWeatherApi, with delay of 2secs
//        new Handler().postDelayed(new Runnable(){
//            public void run(){
//                new AsyncTask<String,Void,String>(){
//                    @Override
//                    protected String doInBackground(String... strings) {
//                        String[] urlParams = {"lat","lon","units"};
//                        String[] urlValues = {coordinates[0],coordinates[1],"metric"};
//                        return SearchWeatherAPI.search(urlParams,urlValues);
//                    }
//
//                    @Override
//                    protected void onPostExecute(String s) {
//                        //Set the animations and temperature value
//                        temp = SearchWeatherAPI.getSnippet(s);
//                        mTempTextView.setVisibility(View.VISIBLE);
//                        YoYo.with(Techniques.BounceInLeft).playOn(mTempTextView);
//                        YoYo.with(Techniques.DropOut).playOn(weather);
//                        weather.setText(temp + "\u00b0");
//                        mProgressView.setVisibility(View.GONE);
//                    }
//
//                }.execute();
//            }
//        },2000);
//    }

    /**
     * Method to identify the onClick event based on the element clicked
     * @param view
     */
    @Override
    public void onClick(View view) {

        Fragment nextFragment = null;
        switch (view.getId()){
            //Laod Emergency Fragment
            case R.id.emergencyImage :
                nextFragment = new EmergencyCalls();
                fab.setVisibility(View.GONE);
                status=true;
                break;
            //Load medication Fragment
            case R.id.medicineImage :
                nextFragment = new MedicalFragment();
                fab.setVisibility(View.VISIBLE);
                status=true;
                break;
            //Load Map Fragment
            case R.id.parkImage :
                nextFragment = new MapsFragment();
                fab.setVisibility(View.GONE);
                status=true;
                break;
            //load Game Fragment
            case R.id.gameImage :
                nextFragment = new GameFragment();
                fab.setVisibility(View.GONE);
                status = true;
                break;
            //identify click on the home screen
            case R.id.relLayout :
                //Animate the images on click
                YoYo.with(Techniques.Flash).playOn(emergencyImage);
                YoYo.with(Techniques.Flash).playOn(medicineImage);
                YoYo.with(Techniques.Flash).playOn(parkingImage);
                YoYo.with(Techniques.Flash).playOn(gameImage);
                status = false;
                break;
        }

        //only if status is true ie. user is still logged in and needs to navigate to another fragment
        if(status) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame,
                    nextFragment).commit();
        }
    }

}
