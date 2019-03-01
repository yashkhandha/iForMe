package edu.monash.iforme;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yashkhandha on 14/05/2018.
 */

public class Reminder implements Parcelable {

    public String medicationName;
    public String reminderName;
    //public String[] days;
    public String startDate;
    public String endDate;
    public String time;
    public boolean alarm;


    public Reminder() {
    }


    public Reminder(String medicationName,String reminderName, String startDate, String endDate, String time, boolean alarm) {

        this.medicationName = medicationName;
        this.reminderName = reminderName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.time = time;
        this.alarm = alarm;
    }

    protected Reminder(Parcel in) {
        medicationName = in.readString();
        reminderName = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        time = in.readString();
        alarm = in.readByte() != 0;
    }

    public static final Creator<Reminder> CREATOR = new Creator<Reminder>() {
        @Override
        public Reminder createFromParcel(Parcel in) {
            return new Reminder(in);
        }

        @Override
        public Reminder[] newArray(int size) {
            return new Reminder[size];
        }
    };

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReminderName() {
        return reminderName;
    }

    public void setReminderName(String reminderName) {
        this.reminderName = reminderName;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(medicationName);
        parcel.writeString(reminderName);
        parcel.writeString(startDate);
        parcel.writeString(endDate);
        parcel.writeString(time);
        parcel.writeByte((byte) (alarm ? 1 : 0));
    }
}
