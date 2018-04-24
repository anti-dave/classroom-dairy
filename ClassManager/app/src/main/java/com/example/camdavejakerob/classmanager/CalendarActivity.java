package com.example.camdavejakerob.classmanager;

import android.Manifest;
import android.accounts.Account;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.camdavejakerob.classmanager.R;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import pub.devrel.easypermissions.EasyPermissions;


/*
 *  @Cam:
 *  Class to handle calendar onclick listener and Calendar layout
 *  This will open the 'activity_calendar' layout
 *
 *  Features:
 *  - View/Change Google Account
 *  - Enable/Disable Calendar event adding
 *  - Launch Calendar Intent
 * */

public class CalendarActivity extends AppCompatActivity
{
    private static final String MY_PREFS_NAME = "CALENDAR_PREFS";
    private static final String CALENDAR_EVENT_PREF = "CALENDAR_EVENT_PREF";
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;
    private static boolean CALENDAR_EVENTS_ENABLED;

    private static final String TAG   = "CALENDAR_ACTIVITY";
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static GoogleAccountCredential accountCredential = null;
    private static final String[] SCOPES = { CalendarScopes.CALENDAR };
    private static Button changeAccountButton;

    private static Calendar service = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        if (!EasyPermissions.hasPermissions(getApplicationContext(), Manifest.permission.GET_ACCOUNTS))
        {
            // Request permission though a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    1003,
                    Manifest.permission.GET_ACCOUNTS);
        }

        prefs = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        CALENDAR_EVENTS_ENABLED = prefs.getBoolean(CALENDAR_EVENT_PREF, true);
        editor = prefs.edit();

        // Initialize Google account credential
        accountCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        if(accountCredential.getSelectedAccount() == null)
            setAccount();

        String accountName = getGoogleAccount(getApplicationContext());
        changeAccountButton = (Button) findViewById(R.id.cal_GoogleAccountButton);
        changeAccountButton.setText((accountName == null)? "Select Account" : accountName);

        Switch calendarToggle = (Switch) findViewById(R.id.cal_EnableSwitch);
        calendarToggle.setChecked(CALENDAR_EVENTS_ENABLED);
        calendarToggle.setSelected(CALENDAR_EVENTS_ENABLED);
        onClick_CalendarToggle(calendarToggle.isChecked());

        calendarToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                onClick_CalendarToggle(isChecked);
            }
        });

    }

    public void onClick_CalendarToggle(boolean enabled)
    {
        editor.putBoolean(CALENDAR_EVENT_PREF, enabled);
        editor.apply();
        CALENDAR_EVENTS_ENABLED = enabled;
        Toast.makeText(this, "Calendar Events " + (enabled? "En" : "Dis") + "abled", Toast.LENGTH_LONG).show();
        // assign to a static member of relevant class for global use?
        // we could have a general 'Google' class to store account information and settings
    }

    /* Change Google Calendar button click */
    public void onClick_ChangeGoogleAccount(View view)
    {
        changeGoogleAccount(CalendarActivity.this);
    }

    public static void changeGoogleAccount(Context context)
    {
        if (EasyPermissions.hasPermissions(context, Manifest.permission.GET_ACCOUNTS))
        {
            context.startActivity(accountCredential.newChooseAccountIntent());
        }
    }

    /* ViewGoogleCalendar button click */
    public void onClick_ViewCalendar(View view)
    {
        long startMillis = System.currentTimeMillis();
        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        ContentUris.appendId(builder, startMillis);
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setData(builder.build());
        startActivity(intent);
        // example_code();
        // Toast.makeText(this, accountCredential.getSelectedAccount().name, Toast.LENGTH_LONG).show();
    }

    public void setAccount(){
        Account[] accounts = accountCredential.getAllAccounts();
        if (accounts == null || accounts.length < 1)
        {
            Toast.makeText(this, "You must select a Google Account", Toast.LENGTH_LONG).show();
            changeGoogleAccount(CalendarActivity.this);
            return;
        }

        accountCredential.setSelectedAccount(accounts[0]);
    }


    public static String getGoogleAccount(Context context)
    {
        if (accountCredential == null)
        {
            accountCredential = GoogleAccountCredential.usingOAuth2(
                context, Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        }
        if (accountCredential == null)
            return null;

        Account[] accounts = accountCredential.getAllAccounts();
        if (accounts == null || accounts.length < 1)
        {
            Toast.makeText(context, "You must select a Google Account", Toast.LENGTH_LONG).show();
            changeGoogleAccount(context);
            return null;
        }

        return accounts[0].name;
    }


    void example_code()
    {
        // Used for testing. Call this method to prompt a calendar event
        Class course = new Class("Mobile App II", "M W F", "3:30pm", "4:30pm", "Olsen 104", "classid12345");

        addCalendarEvent(getApplicationContext(), course);

    }


    public static void addCalendarEvent(Context context, Class course)
    {
        if (accountCredential == null)
        {
            accountCredential = GoogleAccountCredential.usingOAuth2(
                    context, Arrays.asList(SCOPES))
                    .setBackOff(new ExponentialBackOff());
        }

        prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        CALENDAR_EVENTS_ENABLED = prefs.getBoolean(CALENDAR_EVENT_PREF, true);
        editor = prefs.edit();

        if (!CALENDAR_EVENTS_ENABLED)
            return;

        if (course == null)
            return;

        String userEmail = getGoogleAccount(context);
        if (userEmail == null || userEmail.isEmpty())
            userEmail = "example@gmail.com";

        TimeStruct time = parseClassTime(course);
        if (time == null)
        {
            Log.e(TAG, "Bad time parse while adding Calendar Event");
            return;
        }

        java.util.Calendar beginTime = java.util.Calendar.getInstance();
        int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        int month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
        int date = java.util.Calendar.getInstance().get(java.util.Calendar.DATE);
        int dayOffset = daysFromNextClass(course);
        Log.d(TAG, "dayOffset: " + dayOffset);
        Log.d(TAG, "email: " + userEmail);

        beginTime.set(year, month, 23, time.startHour, time.startMin);
        long startTime = beginTime.getTimeInMillis();

        java.util.Calendar finishTime = java.util.Calendar.getInstance();
        finishTime.set(year, month, date + dayOffset, time.endHour, time.endMin);
        long endTime = finishTime.getTimeInMillis();

        String days = parseClassDays(course);

        java.util.Calendar cal = new GregorianCalendar();

        cal.setTimeZone(TimeZone.getDefault());
        cal.setTime(new Date());
        cal.add(java.util.Calendar.MONTH, 2);
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.Events.TITLE, course.getName());
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, course.getRoom());
        intent.putExtra(CalendarContract.Events.RRULE, "FREQ=WEEKLY;BYDAY=" + days);

        intent.putExtra(
                CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                startTime);
        intent.putExtra(
                CalendarContract.EXTRA_EVENT_END_TIME,
                endTime);
        /*intent.putExtra(
                Intent.EXTRA_EMAIL,
                userEmail);*/
        context.startActivity(intent);
    }

    static String parseClassDays(Class course)
    {
        if (course == null)
            return null;

        String days = course.getDaysOfClass();
        String fmtDays = "";
        if (days.contains("M"))
            fmtDays = "MO";
        if (days.contains("Tu"))
            fmtDays += (fmtDays == null || fmtDays.isEmpty())? "TU" : ",TU";
        if (days.contains("W"))
            fmtDays += (fmtDays == null || fmtDays.isEmpty())? "WE" : ",WE";

        String tmp = days.replaceAll("Tu", "");
        if (tmp.contains("T"))
            fmtDays += (fmtDays == null || fmtDays.isEmpty())? "TH" : ",TH";
        if (days.contains("F"))
            fmtDays += (fmtDays == null || fmtDays.isEmpty())? "FR" : ",FR";

        fmtDays += ";";

        return fmtDays;
    }

    /* Get the amount of days until the next class */
    static int daysFromNextClass(Class course)
    {
        if (course == null)
            return 0;

        int day = java.util.Calendar.DAY_OF_WEEK;
        String classDays = course.getDaysOfClass();
        ArrayList<Integer> dayList = new ArrayList<Integer>();
        if (classDays.contains("M"))
            dayList.add(java.util.Calendar.MONDAY);

        if (classDays.contains("Tu"))
            dayList.add(java.util.Calendar.TUESDAY);
        if (classDays.contains("W"))
            dayList.add(java.util.Calendar.WEDNESDAY);
        String tmp = classDays.replaceAll("Tu", "");
        if (tmp.contains("T"))
            dayList.add(java.util.Calendar.THURSDAY);
        if (classDays.contains("F"))
            dayList.add(java.util.Calendar.FRIDAY);

        boolean found = false;
        int nearest = 0;
        for (int i = 0; i < dayList.size(); i++)
        {
            if (dayList.get(i) > day) {
                nearest = dayList.get(i);
                found = true;
                break;
            }
        }
        if (!found && dayList.size() > 0)
        {
            nearest = dayList.get(0);
        }

        int total;
        if (nearest > day)
            total = nearest - day;
        else
            total = day - nearest;

        if (total == 0)
            total = 14;

        return (7 - total);
    }

    /* place to store start/end times */
    static TimeStruct parseClassTime(Class course)
    {
        if (course == null) {
            Log.e(TAG, "Null course being parsed in class time.");
            return null;
        }

        TimeStruct time = new TimeStruct();

        String sTime = course.getStartTime();
        String eTime = course.getEndTime();
        boolean isStartPM = sTime.toLowerCase().contains("pm");
        boolean isEndPM = eTime.toLowerCase().contains("pm");

        String[] sDiv = sTime.split(":");
        if (sDiv.length < 2) {
            Log.e(TAG, "Error parsing class start hour:minute.");
            return null;
        }
        String[] eDiv = eTime.split(":");
        if (eDiv.length < 2) {
            Log.e(TAG, "Error parsing class end hour:minute.");
            return null;
        }
        /* Remove anything that is not a number */
        eDiv[0] = eDiv[0].replaceAll("[^\\d.]", "");
        eDiv[1] = eDiv[1].replaceAll("[^\\d.]", "");
        sDiv[0] = sDiv[0].replaceAll("[^\\d.]", "");
        sDiv[1] = sDiv[1].replaceAll("[^\\d.]", "");

        /* Parse the time to integers */
        try
        {
            time.startHour = Integer.parseInt(sDiv[0]);
            time.startMin  = Integer.parseInt(sDiv[1]);
            time.endHour   = Integer.parseInt(eDiv[0]);
            time.endMin    = Integer.parseInt(eDiv[1]);
        }
        catch (NumberFormatException nfe)
        {
            Log.e(TAG, "Error parsing class time for event.");
            nfe.printStackTrace();
            return null;
        }

        if (isStartPM && time.startHour < 13)
            time.startHour += 12;

        if (isEndPM && time.endHour < 13)
            time.endHour += 12;

        return time;
    }

    private static class TimeStruct
    {
        int startHour, endHour, startMin, endMin;

        public TimeStruct()
        {

        }
    }
}
