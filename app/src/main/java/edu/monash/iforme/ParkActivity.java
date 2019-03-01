package edu.monash.iforme;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ParkActivity extends AppCompatActivity implements View.OnClickListener{

    LatLng mCurrentLocation;
    Spinner mTimeLimit;
    Spinner mReminderTime;
    EditText mExtraDetails;
    Button mParkConfirmButton;

    int timeGap;

    private static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    private static final String ACTION_NOTIFY = "edu.monash.iforme.ACTION_NOTIFY_ME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_park);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("Park");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle bundle = getIntent().getParcelableExtra("locationdetails");
        mCurrentLocation = new LatLng(bundle.getDouble("latitude"),bundle.getDouble("longitude"));

        mTimeLimit = findViewById(R.id.timeLimitSpinner);
        mReminderTime = findViewById(R.id.reminderTimeSpinner);
        mExtraDetails = findViewById(R.id.detailsEditText);
        mParkConfirmButton = findViewById(R.id.parkConfirmButton);

        mParkConfirmButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.parkConfirmButton:
                saveParkingDetails();
                break;
        }
    }

    private void saveParkingDetails() {

        String timeLimit = mTimeLimit.getSelectedItem().toString();
        String reminderTime = mReminderTime.getSelectedItem().toString();
        String extraDetails = mExtraDetails.getText().toString();



        if(timeLimit.equals("Select time limit") || reminderTime.equals("Select time") || extraDetails.equals("")){
            Toast.makeText(this,"Please enter details for all fields",
                    Toast.LENGTH_LONG).show();
        }
        else {
            int timeLimitInt = Integer.parseInt(timeLimit.replaceAll("[^0-9]", ""));
            int reminderTimeInt = Integer.parseInt(reminderTime.replaceAll("[^0-9]", ""));

            timeGap = timeLimitInt - reminderTimeInt;
            ParkingDetails parkingDetails = new ParkingDetails(timeLimit, reminderTime, extraDetails, mCurrentLocation.latitude, mCurrentLocation.longitude);

            FirebaseDatabase.getInstance().getReference("ParkingDetails")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(parkingDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "You have parked your car",
                                Toast.LENGTH_LONG).show();

                        mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                        Intent notifyIntent = new Intent(ParkActivity.this, AlarmReceiverParking.class);
                        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast(ParkActivity.this,
                                NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        final AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

                        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                SystemClock.elapsedRealtime() +
                                        timeGap * 60 * 1000, notifyPendingIntent);

                        //to cancel alarm
                        //alarmManager.cancel(notiifyPendingIntent);
                        //mNotificationManager.cancelAll();

                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Parking data adding Failed",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });






        }

    }
}
