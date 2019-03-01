package edu.monash.iforme;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by yashkhandha on 2/05/2018.
 */

/**
 * Fragment to handle the interactions with the fragment_emergency_call.xml
 */
public class EmergencyCalls extends Fragment {

    //Define view for the fragment
    View vEmergency;
    //UI references
    private Button buttonCall1,buttonCall2,buttonCall3,buttonCall4,buttonCall5;
    //Set id for identifying the call
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0;

    /**
     * onCreateView method to load the initial layout of the fragment and laod necessary data
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle
            savedInstanceState) {

        //Set title for the toolbar of fragment
        ((MainActivity)getActivity()).setActionBarTitle("Emergency Calls");

        //Inflate the xml layout file to link the Class and xml file
        vEmergency = inflater.inflate(R.layout.fragment_emergency_calls, container, false);

        //Fetch references from UI to act upon them
        buttonCall1 = (Button) vEmergency.findViewById(R.id.buttonCall1);
        buttonCall2 = (Button) vEmergency.findViewById(R.id.buttonCall2);
        buttonCall3 = (Button) vEmergency.findViewById(R.id.buttonCall3);
        buttonCall4 = (Button) vEmergency.findViewById(R.id.buttonCall4);
        buttonCall5 = (Button) vEmergency.findViewById(R.id.buttonCall5);

        // add PhoneStateListener
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) getActivity()
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);


        // add button1 listener
        buttonCall1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //Set the intent call
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                //set data in the intent
                callIntent.setData(Uri.parse("tel:000"));
                //Check if user has given permission to access the phone to make calls
                if (ContextCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    return;
                    // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                } else {
                    //if you already have permission
                    try {
                        startActivity(callIntent);
                    } catch(SecurityException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

        // add button2 listener
        buttonCall2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //Set the intent call
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                //set data in the intent
                callIntent.setData(Uri.parse("tel:132500"));
                if (ContextCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    return;
                    // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                } else {
                    //You already have permission
                    try {
                        startActivity(callIntent);
                    } catch(SecurityException e) {
                        e.printStackTrace();
                    }
                }

            }

        });

        // add button3 listener
        buttonCall3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //Set the intent call
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                //set data in the intent
                callIntent.setData(Uri.parse("tel:131444"));
                if (ContextCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    return;
                    // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                } else {
                    //You already have permission
                    try {
                        startActivity(callIntent);
                    } catch(SecurityException e) {
                        e.printStackTrace();
                    }
                }

            }

        });

        // add button4 listener
        buttonCall4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //Set the intent call
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                //set data in the intent
                callIntent.setData(Uri.parse("tel:0416711092"));
                if (ContextCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    return;
                    // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                } else {
                    //You already have permission
                    try {
                        startActivity(callIntent);
                    } catch(SecurityException e) {
                        e.printStackTrace();
                    }
                }

            }

        });

        // add button5 listener
        buttonCall5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //Set the intent call
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                //set data in the intent
                callIntent.setData(Uri.parse("tel:95911190"));
                if (ContextCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    return;
                    // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                } else {
                    //You already have permission
                    try {
                        startActivity(callIntent);
                    } catch(SecurityException e) {
                        e.printStackTrace();
                    }
                }

            }

        });
        //return fragment view to client
        return vEmergency;
    }

    /**
     * method to check if user accepted to share the Call features for the app
     * @param requestCode to identfiy the caller
     * @param permissions check permissions
     * @param grantResults final outcome
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted

                } else {
                    // permission denied
                }
                return;
            }
        }
    }

    //monitor phone call activities
    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;
        String LOG_TAG = "LOGGING 123";
        //to capture the state change during a call
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            //continue if phone state is ringing
            if (TelephonyManager.CALL_STATE_RINGING == state) {
            }
            //on hold
            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                isPhoneCalling = true;
            }
            //to check the idle state
            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                if (isPhoneCalling) {
                    // restart app
                    Intent i = getActivity().getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(
                                    getActivity().getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    isPhoneCalling = false;
                }

            }
        }
    }


}