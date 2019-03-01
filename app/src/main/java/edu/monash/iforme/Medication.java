package edu.monash.iforme;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yashkhandha on 13/05/2018.
 */

public class Medication implements Parcelable {

    //Declare variables
    public String medicationName;
    public String medicationType;
    public String medicationDosage;
    public String medicationInstruction;
    public String medicationShape;
    public String medicationColour;


    //Parametrized Constructor
    public Medication(String medicationName, String medicationType, String medicationDosage, String medicationInstruction, String medicationShape, String medicationColour) {
        this.medicationName = medicationName;
        this.medicationType = medicationType;
        this.medicationDosage = medicationDosage;
        this.medicationInstruction = medicationInstruction;
        this.medicationShape = medicationShape;
        this.medicationColour = medicationColour;
    }

    //Default Constructor
    public Medication() {
    }

    protected Medication(Parcel in) {
        medicationName = in.readString();
        medicationType = in.readString();
        medicationDosage = in.readString();
        medicationInstruction = in.readString();
        medicationShape = in.readString();
        medicationColour = in.readString();
    }

    //Creator object created to manage parcelable data
    public static final Creator<Medication> CREATOR = new Creator<Medication>() {
        @Override
        public Medication createFromParcel(Parcel in) {
            return new Medication(in);
        }

        @Override
        public Medication[] newArray(int size) {
            return new Medication[size];
        }
    };

    //Getters and setters for all fields

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getMedicationType() {
        return medicationType;
    }

    public void setMedicationType(String medicationType) {
        this.medicationType = medicationType;
    }

    public String getMedicationDosage() {
        return medicationDosage;
    }

    public void setMedicationDosage(String medicationDosage) {
        this.medicationDosage = medicationDosage;
    }

    public String getMedicationInstruction() {
        return medicationInstruction;
    }

    public void setMedicationInstruction(String medicationInstruction) {
        this.medicationInstruction = medicationInstruction;
    }

    public String getMedicationShape() {
        return medicationShape;
    }

    public void setMedicationShape(String medicationShape) {
        this.medicationShape = medicationShape;
    }

    public String getMedicationColour() {
        return medicationColour;
    }

    public void setMedicationColour(String medicationColour) {
        this.medicationColour = medicationColour;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * write data to parcel
     * @param parcel
     * @param i
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.medicationName);
        parcel.writeString(this.medicationType);
        parcel.writeString(this.medicationDosage);
        parcel.writeString(this.medicationInstruction);
        parcel.writeString(this.medicationShape);
        parcel.writeString(this.medicationColour);

    }


}
