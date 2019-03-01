package edu.monash.iforme;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 */
public class MedicalTabs2 extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    //UI references
    private static final String TAG = MedicalTabs2.class.getSimpleName();
    private EditText mReminderName;
    private EditText mStartDate;
    private EditText mEndDate;
    private EditText mTime;
    //private Spinner mDays;
    private EditText mMedicationName;
    private Button saveButton;
    private Button cancelButton;

    //to store the current day details
    Calendar mCurrentDate, calendar;
    int day, month, year, hour, minute;

    //To store alarm toggle button status
    private ToggleButton mAlarmToggle;
    //To set notification id to identify the notification
    private static final int NOTIFICATION_ID = 0;
    // NotificationManager to manage the alarm
    private NotificationManager mNotificationManager;
    private static final String ACTION_NOTIFY = "edu.monash.iforme.ACTION_NOTIFY";
    //firebase references
    DatabaseReference databaseReminders;
    DatabaseReference databaseMedications;
    //Objects to store respective details
    Medication med;
    Reminder reminder;
    //to store the medication list
    private ArrayList<Medication> mMedicationList;
    //Pending intent major implementation for Alarm Service
    PendingIntent notiifyPendingIntent;
    long diff;

    public MedicalTabs2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = (View) inflater.inflate(R.layout.tab2_fragment, container, false);

        //Get medication name form shared preferecnes set in MedicalTabs1
        Intent in = getActivity().getIntent();
        med = in.getParcelableExtra("selectedMedication");

        //Set references form xml
        mReminderName = (EditText) view.findViewById(R.id.reminderEditText);
        mStartDate = (EditText) view.findViewById(R.id.startDateText);
        mEndDate = (EditText) view.findViewById(R.id.endDateText);
        mTime = (EditText) view.findViewById(R.id.timeText);
        //mDays = (Spinner) view.findViewById(R.id.daysSpinner);
        saveButton = (Button) view.findViewById(R.id.saveReminderButton);
        cancelButton = (Button) view.findViewById(R.id.clearReminderButton);
        mMedicationName = view.findViewById(R.id.medicationName);

        //To use with date picker and time picker dialog for formatting
        mCurrentDate = Calendar.getInstance();
        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);
        hour = mCurrentDate.get(Calendar.HOUR_OF_DAY);
        minute = mCurrentDate.get(Calendar.MINUTE);

        //for notifications
        mAlarmToggle = (ToggleButton) view.findViewById(R.id.alarmToggle);
        mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        //Get reference to saved medifications from firebase
        databaseMedications = FirebaseDatabase.getInstance().getReference("Medications").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mMedicationList =  new ArrayList<>();

        //Event listeners for buttons and date time picker fields
        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        mStartDate.setOnClickListener(this);
        mEndDate.setOnClickListener(this);
        mTime.setOnClickListener(this);
        //Event listener to detect alarm status
        mAlarmToggle.setOnCheckedChangeListener(this);

        //if the medication is not null i.e. medication has been clicked in the list to load the view
        if (med != null) {
            //load the remidner details for the medication selected
            fetchReminder();
        }
        return view;
    }

    private void fetchReminder() {
        //to fetch reminder based on medication
        databaseReminders = FirebaseDatabase.getInstance().getReference("Reminders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReminders.orderByChild("medicationName").equalTo(med.getMedicationName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot reminderSnapshot : dataSnapshot.getChildren()) {
                    reminder = reminderSnapshot.getValue(Reminder.class);
                }
                if (reminder != null) {
                    mReminderName.setText(reminder.getReminderName());
                    mStartDate.setText(reminder.getStartDate());
                    mEndDate.setText(reminder.getEndDate());
                    mTime.setText(reminder.getTime());
                    boolean t = reminder.isAlarm();
                    mAlarmToggle.setChecked(reminder.isAlarm());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void startDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                monthOfYear += 1;
                String formattedMonth = "" + monthOfYear;
                String formattedDayOfMonth = "" + dayOfMonth;

                //formatting the correct date value
                if (monthOfYear < 10) {
                    formattedMonth = "0" + monthOfYear;
                }
                if (dayOfMonth < 10) {
                    formattedDayOfMonth = "0" + dayOfMonth;
                }
                mStartDate.setText(formattedDayOfMonth + "-" + (formattedMonth) + "-" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void endDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                monthOfYear += 1;
                String formattedMonth = "" + monthOfYear;
                String formattedDayOfMonth = "" + dayOfMonth;

                //formatting the correct date value
                if (monthOfYear < 10) {
                    formattedMonth = "0" + monthOfYear;
                }
                if (dayOfMonth < 10) {
                    formattedDayOfMonth = "0" + dayOfMonth;
                }
                mEndDate.setText(formattedDayOfMonth + "-" + (formattedMonth) + "-" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void setTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String formattedHour = "" + hourOfDay;
                        String formattedMinute = "" + minute;
                        if (hourOfDay < 10) {
                            formattedHour = "0" + hourOfDay;
                        }
                        if (minute < 10) {
                            formattedMinute = "0" + minute;
                        }

                        mTime.setText(formattedHour + ":" + formattedMinute);
                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.saveReminderButton:
                attemptReminderSave();
                break;
            case R.id.clearReminderButton:
                clearReminder();
                break;
            case R.id.startDateText:
                startDate();
                break;
            case R.id.endDateText:
                endDate();
                break;
            case R.id.timeText:
                setTime();
        }
    }

    private void clearReminder() {

        YoYo.with(Techniques.Wobble).playOn(mReminderName);
        YoYo.with(Techniques.Wobble).playOn(mStartDate);
        YoYo.with(Techniques.Wobble).playOn(mEndDate);
        YoYo.with(Techniques.Wobble).playOn(mTime);
        mReminderName.setText("");
        mStartDate.setText("");
        mEndDate.setText("");
        mTime.setText("");
    }

    private void attemptReminderSave() {
        //FragmentTransaction ft = getFragmentManager().beginTransaction();
        //ft.detach(this).attach(this).commit();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String medicationName = preferences.getString("medicationName", "");

        String reminderName = mReminderName.getText().toString();
        String startDate = mStartDate.getText().toString();
        String endDate = mEndDate.getText().toString();
        String time = mTime.getText().toString();

        if (reminderName.equals("") || startDate.equals("") || endDate.equals("") || time.equals("")) {
            Snackbar snackbar = Snackbar
                    .make(getView(), "Please enter all the fields", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            textView.setTextSize((float) 20.0);
            snackbar.show();
        } else if (medicationName.equals("")) {
            Snackbar snackbar = Snackbar
                    .make(getView(), "Please enter medication in first tab", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize((float) 20.0);
            snackbar.show();
        }
        //if medication is not added in db
        else if (checkMedicationExists(medicationName)) {
            Snackbar snackbar = Snackbar
                    .make(getView(), "Please save medication first", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize((float) 20.0);
            snackbar.show();
        } else {
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(mTime.getText().toString().substring(0, 2)));
            calendar.set(Calendar.MINUTE, Integer.parseInt(mTime.getText().toString().substring(3, 5)));
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(startDate.substring(0,2)));
            calendar.set(Calendar.MONTH,Integer.parseInt(startDate.substring(3,5))-1);
            calendar.set(Calendar.YEAR,Integer.parseInt(startDate.substring(6,10)));

            SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");

            try {
                Date date = new Date();
                String currentDate = myFormat.format(date);
                Date start = myFormat.parse(currentDate);
                Date end = myFormat.parse(endDate);
                diff = end.getTime() - start.getTime();
                System.out.println("Days : " + TimeUnit.DAYS.convert(diff,TimeUnit.MILLISECONDS));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Intent notifyIntent = new Intent(getActivity(), AlarmReceiver.class);

            notiifyPendingIntent = PendingIntent.getBroadcast(getActivity(),
                    NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            final AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

            boolean alarm = mAlarmToggle.isChecked();
            if (alarm) {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, notiifyPendingIntent);
            } else {
                alarmManager.cancel(notiifyPendingIntent);
                mNotificationManager.cancelAll();
            }

            //cancel the set alarm
            new Handler().postDelayed(new Runnable(){
                public void run(){
                    alarmManager.cancel(notiifyPendingIntent);
                }
            },diff);

            Reminder reminder = new Reminder(medicationName, reminderName, startDate, endDate, time, alarm);

            FirebaseDatabase.getInstance().getReference("Reminders")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(medicationName)
                    .setValue(reminder).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Reminder updated successfully",
                                Toast.LENGTH_LONG).show();
                        getActivity().finish();
                    }
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseMedications.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMedicationList.clear();
                for (DataSnapshot medicationSnapshot : dataSnapshot.getChildren()) {
                    Medication medication = medicationSnapshot.getValue(Medication.class);
                    mMedicationList.add(medication);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private boolean checkMedicationExists(final String medicationName) {
        for (int i = 0; i < mMedicationList.size(); i++) {
            if (mMedicationList.get(i).getMedicationName().equals(medicationName)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked)
            buttonView.setBackgroundColor(Color.GREEN);
        else buttonView.setBackgroundColor(Color.RED);
    }
}