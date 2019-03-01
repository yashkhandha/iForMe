package edu.monash.iforme;


import android.*;
import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {

    //UI references
    MapView mMapView;
    private GoogleMap mGoogleMap;
    private LocationManager locationManager;
    View view;
    Button mParkButton;
    Button mFindButton;
    LatLng currentLocation;
    LatLng savedLocation;
    private LocationListener locationListener;
    //Database reference for saving and fetching parking details from firebase
    DatabaseReference databaseParking;
    ParkingDetails parkingDetails;

    public MapsFragment() {
        // Required empty public constructor
    }

    /**
     * To load the view
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //defining the location manager object to get the location of the device and store it in currentLocation
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            //to listen if the location of the device is changed and update the currentLocation
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Location : ", location.toString());
                currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };

        //If the user has not provided permisisons for accessing the location to the app in settings, then this will request for permission from the user
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        } else {
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        //Getting reference of Firebase where parking details are stored for the logged in user
        databaseParking = FirebaseDatabase.getInstance().getReference("ParkingDetails").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    /**
     * To show dialog box and ask user for location permission and act accordinglhy
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){

            case 1 : {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //run normal code, access given
                }
                else{
                    //no access given, functionality ont work
                }
                return;
            }
        }
    }

    /**
     * To load the layout and get UI references
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map_control, container, false);
        //set the toolbar title
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Park & Find");
        //Parking button
        mParkButton = (Button) view.findViewById(R.id.parkButton);
        //Finding car button
        mFindButton = (Button) view.findViewById(R.id.findButton);
        //Event listeners for the buttons
        mParkButton.setOnClickListener(this);
        mFindButton.setOnClickListener(this);

        return view;
    }

    /**
     * To choose appropriate method based on the button clicked
     * @param view
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //case when park button is clicked
            case R.id.parkButton:
                //call parkCar method
                parkCar();
                break;
                //Case when find car button is clicked
            case R.id.findButton:
                // call findCar method
                findCar();
                break;
            default:
                break;
        }

    }

    /**
     * Method to initiate ParkingActivity class which manages saving parking information of the user
     */
    private void parkCar() {
        Intent intent = new Intent(getActivity(), ParkActivity.class);
        //Create bundle and set location details of current location to store in database when the parking data is stored in ParkingAtivity.class
        Bundle bundle = new Bundle();
        bundle.putDouble("latitude", currentLocation.latitude);
        bundle.putDouble("longitude", currentLocation.longitude);
        intent.putExtra("locationdetails", bundle);
        //startthe activity
        startActivity(intent);
    }

    /**
     * Method to find the car where it was parked and show path to it
     */
    private void findCar() {
        //Check if app has permisisons to location, if not then request ot user for the same
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        currentLocation = new LatLng(loc.getLatitude(),loc.getLongitude());

        //if there is details of the parking information for the user in firebase are present
        if(parkingDetails != null) {
            //start google map intent and pass the source and destination coordinates for the Google map to identify the path
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?f=d&saddr=" + currentLocation.latitude + "," + currentLocation.longitude + "&daddr=" + parkingDetails.getLatitude() + "," + parkingDetails.getLongitude()));
            intent.setComponent(new ComponentName("com.google.android.apps.maps",
                    "com.google.android.maps.MapsActivity"));
            startActivity(intent);
        }
        //if no parking details stored in firebase for the logged in user
        else{
            //Show snackbar with appropriate messaage
            Snackbar snackbar = Snackbar
                    .make(getView(), "No parking information", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            textView.setTextSize((float) 20.0);
            snackbar.show();
        }

    }

    /**
     * to laod current location on start of the MapsFragment to show on map
     */
    @Override
    public void onStart() {
        super.onStart();
        databaseParking = FirebaseDatabase.getInstance().getReference("ParkingDetails").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseParking.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null) {
                    parkingDetails = dataSnapshot.getValue(ParkingDetails.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //if map created successfully, this code will be executed
    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView)view.findViewById(R.id.mapView);
        if(mMapView != null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.setClickable(false);
            mMapView.getMapAsync(this);

        }
    }

    /**
     * Method that will load the map and markers
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        //if the user has not granted permisisons to app to get it location, request for it
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //fetch curent location from last known location
        currentLocation = new LatLng(loc.getLatitude(),loc.getLongitude());

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Location : ", location.toString());
                currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };

        MapsInitializer.initialize(getActivity().getApplicationContext());

        mGoogleMap = googleMap;
        //setting map theme to normal view
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Hello").snippet("You are here !"));

        CameraPosition Uni = CameraPosition.builder().target(currentLocation).zoom(16).bearing(0).build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Uni));
    }
}
