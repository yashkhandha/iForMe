package edu.monash.iforme;

import android.content.Context;
import android.content.SharedPreferences;

/** Class to manage the shared preferences for the WelcomeActivity to track user login
 * Created by ykha0002 on 8/6/18.
 */

public class PreferenceManager {

    private Context context;
    private SharedPreferences sharedPreferences;

    public PreferenceManager(Context context){
        this.context = context;
        getSharedPreference();
    }

    /**
     * Method to Getting SharedPreferences reference
     */

    public void getSharedPreference(){
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference),Context.MODE_PRIVATE);
    }

    /**
     * Method to write into shared preference to be accessed
     */
    public void writePreference(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.preference_key),"OK");
        editor.commit();
    }

    /**
     * Method to check the preference
     * @return checkStatus
     */
    public boolean checkPreference(){
        boolean checkStatus = false;
        //if the user has not logged in yet for the first time
        if(sharedPreferences.getString(context.getString(R.string.preference_key),"null").equals("null")){
            checkStatus = false;
        }
        //if user has logged in previosuly
        else
        {
            checkStatus = true;
        }
        return checkStatus;
    }

    /**
     * Method to reset the preferences
     */
    public void clearPreference(){
        sharedPreferences.edit().clear().commit();
    }



}