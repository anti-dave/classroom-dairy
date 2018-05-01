package com.example.camdavejakerob.classmanager;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import pub.devrel.easypermissions.EasyPermissions;

public class AttendanceActivity extends AppCompatActivity {
    private final String TAG = "ATTENDANCE_ACTIVITY";
    private final String CLASS_ID = "CLASS_ID";
    private FirebaseUser curUser;
    private String classId;
    private ListView mListView;
    private DatabaseHelper mDatabase;
    private User user;
    private Class currentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        curUser = FirebaseAuth.getInstance().getCurrentUser();
        classId = getIntent().getStringExtra(CLASS_ID);
        mDatabase = new DatabaseHelper();
        user = ((ClassManagerApp) AttendanceActivity.this.getApplication()).getCurUser();
        Intent intent = getIntent();
        currentClass = (Class) intent.getParcelableExtra("CURRENT_CLASS");



        TextView checkinLabel = (TextView) findViewById(R.id.at_CheckinLabel);
        Button checkinButton = (Button) findViewById(R.id.at_CheckinButton);

        TextView classText = (TextView) findViewById(R.id.at_ClassText);
        TextView timeText = (TextView) findViewById(R.id.at_TimeText);
        TextView daysText = (TextView) findViewById(R.id.at_DaysText);

        /* If they're an instructor, they won't be checking in,
        they will poll for students who have already checked in */
        if (user.isInstructor()) {
            checkinLabel.setText("Press to poll for attendance");
            checkinButton.setText("Poll Attendance");
        }

        /* Display basic Class information */
        classText.setText(currentClass.getName());
        timeText.setText(currentClass.getClassTime());
        daysText.setText(currentClass.getDaysOfClass());

        if (!EasyPermissions.hasPermissions(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your location.",
                    1003,
                    Manifest.permission.ACCESS_FINE_LOCATION);
        }

    }

    // M Tu W T F / TBA
    public void onClick_AttendanceCheckin(View view) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (!hasDay(day)) {
            Toast.makeText(this, "You don't have this class today", Toast.LENGTH_LONG).show();
            return;
        }

        String timeStr;
        timeStr = currentClass.getClassTime();
        //timeStr = "12:00am - 7:00pm";
        if (!verifyTime(timeStr)) {
            //Toast.makeText(this, "Class is not active at this time", Toast.LENGTH_LONG).show();
            return;
        }

        if (user.isInstructor())
            instructorPollClass();
        else
        {
            studentCheckIn();
        }
    }

    void instructorPollClass()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
        String currentDate = sdf.format(new Date());
        ListView checkinList = (ListView) findViewById(R.id.at_checkinList);

        mDatabase.getPresentStudents(AttendanceActivity.this, checkinList,
        currentClass.getCourseID(), currentDate);
    }

    void studentCheckIn()
    {

        /*
        // commented out for testing at home
        if (inNorthCampus())
        {
        */
            Toast.makeText(this, "Checked In", Toast.LENGTH_LONG).show();
            SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
            String currentDate = sdf.format(new Date());

            mDatabase.setTodayStudentAttendance(user.getUserId(), currentClass.getCourseID(), true, currentDate);
        /*
        }
        else
            Toast.makeText(this, "Not on Campus", Toast.LENGTH_LONG).show();
        */

    }

    boolean inNorthCampus()
    {
        GPSTracker gps = new GPSTracker(AttendanceActivity.this);
        if (gps.canGetLocation)
            return false;

        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();

        if (latitude < 42.649564 || latitude > 42.658253)
            return false;

        if (longitude < -71.332367 || longitude > -71.332367)
            return false;

        return true;
    }

    boolean hasDay(int day) {
        System.out.print("Day: " + day);
        Log.d("TAG", "Day: " + day);
        String classDays = currentClass.getDaysOfClass();
        if (classDays.contains("TBA"))
            return false;

        switch (day) {
            case Calendar.MONDAY:
                return classDays.contains("M");

            case Calendar.TUESDAY:
                return classDays.contains("Tu");

            case Calendar.WEDNESDAY:
                return classDays.contains("W");

            case Calendar.THURSDAY:
                String tmp = classDays.replaceAll("Tu", "");
                return tmp.contains("T");

            case Calendar.FRIDAY:
                return classDays.contains("F");
        }

        return false;
    }

    /*
    * Validates the current classes time with the current system time
    * */
    boolean verifyTime(String time) {
        /* Sanitize input */
        if (!time.contains("-")) {
            Toast.makeText(this, "ERROR: no '-'", Toast.LENGTH_LONG).show();
            return false;
        }

        String[] interval = time.split("-");
        if (interval.length < 2) {
            Toast.makeText(this, "No start end", Toast.LENGTH_LONG).show();
            return false;
        }

        Calendar calendar = Calendar.getInstance();
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTimeZone(TimeZone.getTimeZone("EST"));
        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY) + 1;
        String currentMin = (new SimpleDateFormat("mm")).format(calendar.getTime());

        int startHour;
        int startMin;
        int endHour;
        int endMin;

        int cMin;

        boolean
                startAM = interval[0].toLowerCase().contains("am"),
                startPM = interval[0].toLowerCase().contains("pm"),
                endAM = interval[1].toLowerCase().contains("am"),
                endPM = interval[1].toLowerCase().contains("pm");

        if ((startAM || startPM) && (endAM || endPM)) {
            /* Cleanup the 'am' and 'pm' from the string */
            interval[0] = interval[0].substring(0, interval[0].indexOf(interval[0].contains("am") ? "am" : "pm"));
            interval[1] = interval[1].substring(0, interval[1].indexOf(interval[1].contains("am") ? "am" : "pm"));
        }

        /* Remove any whitespaces that may be there */
        interval[0] = interval[0].replaceAll(" ", "");
        interval[1] = interval[1].replaceAll(" ", "");

        /* Seperate the hours and minutes */
        String[] startHHmm = interval[0].split(":");
        String[] endHHmm = interval[1].split(":");

        /* Guarantee an hour and a minute was parsed from each */
        if (startHHmm.length < 1 || endHHmm.length < 1) {
            Log.e(TAG, "Error occurred when parsing the current class time.\n" +
                    "No semicolon was found.");
            Toast.makeText(this, "Semicolon parsing", Toast.LENGTH_LONG).show();
            return false;
        }
        try {
            startHour = Integer.parseInt(startHHmm[0]);
            startMin = Integer.parseInt(startHHmm[1]);
            endHour = Integer.parseInt(endHHmm[0]);
            endMin = Integer.parseInt(endHHmm[1]);

            cMin = Integer.parseInt(currentMin);
        } catch (NumberFormatException nfe) {
            Log.e(TAG, "Error occurred when parsing the current class time.");
            Toast.makeText(this, "String to Int", Toast.LENGTH_LONG).show();
            nfe.printStackTrace();
            return false;
        }

        if (startAM && startHour == 12)
            startHour = 0;

        if (endAM && endHour == 12)
            endHour = 0;

        if (startPM && startHour < 13)
            startHour += 12;

        if (endPM && endHour < 13)
            endHour += 12;

        if (currentHour < startHour || currentHour > endHour)
        {
            // Toast.makeText(this, "Class is not active yet.", Toast.LENGTH_LONG).show();
            Toast.makeText(this,
                    "Class is between " + startHour + ":" + startMin + " and " + endHour + ":" + endMin,
                    Toast.LENGTH_LONG).show();
            return false;
        }

        // I think current hour is really all that is necessary
        /*if (currentHour == startHour && cMin < startMin) {
            Toast.makeText(this, "TWO", Toast.LENGTH_LONG).show();
            return false;
        }

        if (currentHour == endHour && cMin > endMin) {
            Toast.makeText(this, "THREE", Toast.LENGTH_LONG).show();
            return false;
        }*/

        return true;
    }
}

class MyLocationListener implements LocationListener {

    @Override
    public void onLocationChanged(Location location) {

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
}

class GPSTracker extends Service implements LocationListener {

    private final Context mContext;

    // Flag for GPS status
    boolean isGPSEnabled = false;

    // Flag for network status
    boolean isNetworkEnabled = false;

    // Flag for GPS status
    boolean canGetLocation = false;

    Location location; // Location
    double latitude; // Latitude
    double longitude; // Longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // Getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // Getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // No network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return location;
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // If GPS enabled, get latitude/longitude using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app.
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSTracker.this);
        }
    }


    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }


    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/Wi-Fi enabled
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }


    /**
     * Function to show settings alert dialog.
     * On pressing the Settings button it will launch Settings Options.
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing the Settings button.
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // On pressing the cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    @Override
    public void onLocationChanged(Location location) {
    }


    @Override
    public void onProviderDisabled(String provider) {
    }


    @Override
    public void onProviderEnabled(String provider) {
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}