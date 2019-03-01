package edu.monash.iforme;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.kimkevin.cachepot.CachePot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;


/**
 * A simple {@link Fragment} subclass.
 * First tab manager for adding medications
 */
public class MedicalTabs1 extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    //UI references
    Button mSaveMedicineButton, mClearMedicineButton;
    EditText mMedicationName;
    Spinner mTypeOfMedication;
    Spinner mDosage;
    EditText mInstructions;
    Spinner mShapeOfMedicine;
    Spinner mColourOfMedicine;
    Medication medication;
    TextView shapeOfMedicineText;

    public MedicalTabs1() {
        // Required empty public constructor
    }

    /**
     * To load UI and manage references and event clicks
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = (View) inflater.inflate(R.layout.tab1_fragment, container, false);

        //to get the details stored in intent
        Intent in = getActivity().getIntent();

        //selected medication in the list view on MedicalFragment
        Medication med = in.getParcelableExtra("selectedMedication");

        //get reference from XMl
        mSaveMedicineButton = view.findViewById(R.id.saveMedicineButton);
        mClearMedicineButton = view.findViewById(R.id.clearMedicineButton);
        mMedicationName = view.findViewById(R.id.medicationName);
        mTypeOfMedication = view.findViewById(R.id.medicationTypeSpinner);
        mDosage = view.findViewById(R.id.dosageSpinner);
        mInstructions = view.findViewById(R.id.instructions);
        mShapeOfMedicine = view.findViewById(R.id.medicineShapeSpinner);
        mColourOfMedicine = view.findViewById(R.id.medicineColourSpinner);
        shapeOfMedicineText = view.findViewById(R.id.medicineShapeTextView);

        //if the medicine in the list has been clicked then load the UI with medicine and reminder details of the selected medicine
        if(med !=null){
            //Animate the medication name on view created
            YoYo.with(Techniques.DropOut).playOn(mMedicationName);
            //set the medication name in edittext
            mMedicationName.setText(med.getMedicationName());
            //To not allow to edit medication name
            mMedicationName.setFocusable(false);
            mMedicationName.setOnClickListener(this);
            //Store the medication name in shared preferences which will be then used in MedicalTabs2 for saving the reminder for this medicine
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("medicationName",med.getMedicationName());
            editor.apply();

            //Set the medication details on the view
            mTypeOfMedication.setSelection(getIndex(mTypeOfMedication,med.getMedicationType()));
            mDosage.setSelection(getIndex(mDosage,med.getMedicationDosage()));
            mInstructions.setText(med.getMedicationInstruction());
            mShapeOfMedicine.setSelection(getIndex(mShapeOfMedicine,med.getMedicationShape()));
            mColourOfMedicine.setSelection(getIndex(mColourOfMedicine,med.getMedicationColour()));
        }

        medication = new Medication();
        //if medication name is changed while saving new medicine then save the new name in shared preferences
        mMedicationName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                medication.setMedicationName(mMedicationName.getText().toString());
                //CachePot.getInstance().push(medication);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("medicationName",medication.getMedicationName());
                editor.apply();
            }
        });

        //Event listener for save medicine button
        mSaveMedicineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSaveMedicine();
            }
        });
        //Event listener for clear medicine button
        mClearMedicineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearMedicine();
            }
        });

        //Event listener on type to show and hide the Shape field in the form
        mTypeOfMedication.setOnItemSelectedListener(this);

        return view;
    }

    /**
     * method used to get index based on the selected item in spinner
     * @param spinner Spinner
     * @param myString selected text
     * @return index
     */
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }

    /**
     * Method called when Clear button is clicked
     */
    private void clearMedicine() {
        //Animate the fields and clear the fields
        YoYo.with(Techniques.Wobble).playOn(mMedicationName);
        YoYo.with(Techniques.Wobble).playOn(mTypeOfMedication);
        YoYo.with(Techniques.Wobble).playOn(mDosage);
        YoYo.with(Techniques.Wobble).playOn(mInstructions);
        YoYo.with(Techniques.Wobble).playOn(mClearMedicineButton);
        YoYo.with(Techniques.Wobble).playOn(mColourOfMedicine);
        YoYo.with(Techniques.Wobble).playOn(mShapeOfMedicine);
        mTypeOfMedication.setSelection(0);
        mDosage.setSelection(0);
        mInstructions.setText("");
        mColourOfMedicine.setSelection(0);
        mShapeOfMedicine.setSelection(0);
    }

    /**
     * Method called when save medicine button clicked
     */
    private void attemptSaveMedicine() {

        //Fetch all details from the entered information by user
        String medicationName = mMedicationName.getText().toString();
        String medicationType = mTypeOfMedication.getSelectedItem().toString();
        String dosage = mDosage.getSelectedItem().toString();
        String instructions = mInstructions.getText().toString();
        String shape = mShapeOfMedicine.getSelectedItem().toString();
        String colour = mColourOfMedicine.getSelectedItem().toString();

        //if any field is not selected or left blank
        if(medicationName.equals("") || medicationType.equals("Select medication") ||
                dosage.equals("Select dosage") || instructions.equals("") ||
                colour.equals("Select colour")){
            //Show ERROR
            Snackbar snackbar = Snackbar
                    .make(getView(), "Please enter all the fields", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            textView.setTextSize((float) 20.0);
            snackbar.show();
        }
        //All fields entered and selected correctly and save in Firebase database
        else {
            Medication medication = new Medication(medicationName, medicationType, dosage, instructions, shape, colour);

            FirebaseDatabase.getInstance().getReference("Medications")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(medicationName)
                    .setValue(medication).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Medication updated successfully",
                                Toast.LENGTH_LONG).show();
                        getActivity().finish();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view) {

    }

    /**
     * Method to show and hide the shapeOfMedicine Spinner based on seelcted element in typeOfMedication spinner
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(mTypeOfMedication.getSelectedItem().toString().equals("Pill")){
            mShapeOfMedicine.setVisibility(View.VISIBLE);
            shapeOfMedicineText.setVisibility(View.VISIBLE);
        }
        else if(mTypeOfMedication.getSelectedItem().toString().equals("Syrup") || mTypeOfMedication.getSelectedItem().toString().equals("Select Medication")){
            mShapeOfMedicine.setVisibility(View.GONE);
            shapeOfMedicineText.setVisibility(View.GONE);
            mShapeOfMedicine.setSelection(0);
        }
        else if(mTypeOfMedication.getSelectedItem().toString().equals("Powder") || mTypeOfMedication.getSelectedItem().toString().equals("Select Medication")){
            mShapeOfMedicine.setVisibility(View.GONE);
            shapeOfMedicineText.setVisibility(View.GONE);
            mShapeOfMedicine.setSelection(0);
        }
        else if(mTypeOfMedication.getSelectedItem().toString().equals("Other") || mTypeOfMedication.getSelectedItem().toString().equals("Select Medication")){
            mShapeOfMedicine.setVisibility(View.GONE);
            shapeOfMedicineText.setVisibility(View.GONE);
            mShapeOfMedicine.setSelection(0);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
