package edu.monash.iforme;

/**
 * Created by yashkhandha on 22/05/2018.
 */

public class ParkingDetails {

    //To store the time for which car will be parked
    public String timeLimit;
    //to notify before parking expiry to remind
    public String reminderTime;
    //to store extra details
    public String extraDetails;
    //To store the latitude of the parking location
    public double latitude;
    //To sotre the longitude of the parking location
    public double longitude;

    //Default Constructor
    public ParkingDetails() {
    }

    //Paramterizeed constructor
    public ParkingDetails(String timeLimit, String reminderTime, String extraDetails, double latitude, double longitude) {
        this.timeLimit = timeLimit;
        this.reminderTime = reminderTime;
        this.extraDetails = extraDetails;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //Accessors and modifiers for all the fields

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }

    public String getExtraDetails() {
        return extraDetails;
    }

    public void setExtraDetails(String extraDetails) {
        this.extraDetails = extraDetails;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
