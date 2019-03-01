package edu.monash.iforme;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by yashkhandha on 9/05/2018.
 */

public class MedicalFragment extends Fragment implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener, View.OnClickListener {

    View vMed;
    private ListView mListView;
    private MedicationAdapter mAdapter;
    private ArrayList<Medication> mMedicationList;
    DatabaseReference databaseMedications;
    DatabaseReference databaseReminders;
    public SearchView mSearchView;
    public Reminder reminder;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle
            savedInstanceState) {

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Medication Reminders");
        vMed = inflater.inflate(R.layout.fragment_medical, container, false);

        Toolbar toolbar = (Toolbar) vMed.findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).setActionBar(toolbar);
        //for oncreateoptionsmenu
        setHasOptionsMenu(true);

        Toast.makeText(getActivity(),"Long press on a medicine to delete it",
                Toast.LENGTH_LONG).show();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(preferences.contains("medicationName")){
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("medicationName");
            editor.apply();
        }


        databaseMedications = FirebaseDatabase.getInstance().getReference("Medications").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mListView = (ListView)vMed.findViewById(R.id.medicationListView);
        mMedicationList =  new ArrayList<>();

        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);

        FloatingActionButton fab = getActivity().findViewById(R.id.fabButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MedicalTabs.class);
                startActivity(intent);

            }
        });
        return vMed;
    }

    // Create our menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater ) {
        //MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu,inflater);

        SearchManager manager = (SearchManager)
                getActivity().getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setSearchableInfo(manager.
                getSearchableInfo(getActivity().getComponentName()));
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(this);

        //return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseMedications.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMedicationList.clear();
                for(DataSnapshot medicationSnapshot : dataSnapshot.getChildren()){
                    System.out.println("test : "+ medicationSnapshot.getValue(Medication.class));
                    Medication medication = medicationSnapshot.getValue(Medication.class);
                    Log.d("TAG", medication.toString());
                    mMedicationList.add(medication);
                }
                int numMedications = mMedicationList.size();
                //((MainActivity)getActivity()).setActionBarTitle("All Medications : " + numMedications);
                mAdapter = new MedicationAdapter(getActivity(),mMedicationList);
                mListView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        final Intent intent = new Intent(getActivity(),MedicalTabs.class);

        Medication selectedMedication = (Medication) adapterView.getItemAtPosition(i);

        //to fetch reminder based on medication
        databaseReminders = FirebaseDatabase.getInstance().getReference("Reminders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReminders.orderByChild("medicationName").equalTo(selectedMedication.getMedicationName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot reminderSnapshot : dataSnapshot.getChildren()) {
                    reminder = reminderSnapshot.getValue(Reminder.class);

                }
                intent.putExtra("reminder",reminder);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        intent.putExtra("selectedMedication", selectedMedication);
        startActivity(intent);
        refreshListView();
    }

    private void refreshListView() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        mAdapter.getFilter().filter(s);
        return true;    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, final View view, final int
            position, long l) {
        // Build a dialog to delete item
        AlertDialog.Builder builder =
                new AlertDialog.Builder(view.getContext());
        builder.setTitle("Remove Medication?");
        builder.setMessage("Are you sure you wish to remove this medication?");
        builder.setPositiveButton("Remove",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // Remove person from list and database
                        Medication medication = mMedicationList.remove(position);
                        //remove this medication from firebase
                        deleteMedication(medication);

                        // Update ListView
                        refreshListView();
                        Snackbar snackbar = Snackbar.make(view, "Medication has been deleted", Snackbar.LENGTH_SHORT)
                              .setAction("Action", null);
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        textView.setTextSize((float) 20.0);
                        snackbar.show();

                    }
                });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
        return true;
    }

    private void deleteMedication(Medication medication) {

        databaseMedications = FirebaseDatabase.getInstance().getReference("Medications").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(medication.getMedicationName());
        databaseReminders = FirebaseDatabase.getInstance().getReference("Reminders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(medication.getMedicationName());

        databaseMedications.removeValue();
        databaseReminders.removeValue();
    }
}
