package com.example.camdavejakerob.classmanager;

import android.Manifest;
import android.accounts.Account;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Arrays;

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
    private static final String API_KEY   = "AIzaSyBAFZFCI5QWMjwAyTpzv51MU9395ZJl1jE";
    private static final String CLIENT_ID = "421332567684-8jr80fpvcr5v4feegmt4en5qrsdgt4bk.apps.googleusercontent.com";
    private static final String APP_NAME  = "ClassManager";
    private static final String PREF_ACCOUNT_NAME = "accountName";
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

        // Initialize Google account credential
        accountCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        if(accountCredential.getSelectedAccount() == null)
            setAccount();

        // Initialize Google Calendar service
        service = new Calendar.Builder(
            HTTP_TRANSPORT, JSON_FACTORY, accountCredential)
            .setApplicationName(APP_NAME)
            .build();

        String accountName = getGoogleAccount();
        changeAccountButton = (Button) findViewById(R.id.cal_GoogleAccountButton);
        changeAccountButton.setText((accountName == null)? "Select Account" : accountName);

        Switch calendarToggle = (Switch) findViewById(R.id.cal_EnableSwitch);
        calendarToggle.setChecked(true);
        onClick_CalendarToggle(calendarToggle.isChecked());

        calendarToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                onClick_CalendarToggle(isChecked);
            }
        });

    }

    /* TODO: Requires implementation */
    public void onClick_CalendarToggle(boolean enabled)
    {
        // assign to a static member of relevant class for global use?
        // we could have a general 'Google' class to store account information and settings
    }

    /* Change Google Calendar button click */
    public void onClick_ChangeGoogleAccount(View view)
    {
        changeGoogleAccount();
    }

    public void changeGoogleAccount()
    {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.GET_ACCOUNTS))
        {
            startActivityForResult(
                    accountCredential.newChooseAccountIntent(),
                    1000);
        }
        else {
            // Request permission though a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    1003,
                    Manifest.permission.GET_ACCOUNTS);
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
        //example_code();
        // Toast.makeText(this, accountCredential.getSelectedAccount().name, Toast.LENGTH_LONG).show();
    }

    public void setAccount(){
        Account[] accounts = accountCredential.getAllAccounts();
        if (accounts == null || accounts.length < 1)
        {
            Toast.makeText(this, "You must select a Google Account", Toast.LENGTH_LONG).show();
            changeGoogleAccount();
            return;
        }

        accountCredential.setSelectedAccount(accounts[0]);
    }


    public String getGoogleAccount()
    {
        String accountName = null;

        if (EasyPermissions.hasPermissions(this, Manifest.permission.GET_ACCOUNTS))
        {
            accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString("accountName", null);
        }
        else {
            // Request permission though a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    1003,
                    Manifest.permission.GET_ACCOUNTS);
        }

        return accountName;
    }

    public Event buildEvent(String summary, String location, String description)
    {
        return new Event()
                .setSummary(summary)
                .setLocation(location)
                .setDescription(description);
    }

    public GeneralTime buildTime(DateTime startTime, DateTime endTime, String timeZone)
    {
        return new GeneralTime(startTime, endTime, timeZone);
    }

    void example_code()
    {
        Event event = buildEvent(
                "Mobile App Programming Class",
                "Olsen 411 North Campus",
                "Go to class");

        GeneralTime time = buildTime(
                new DateTime("2015-05-28T09:00:00-07:00"),
                new DateTime("2015-05-28T17:00:00-07:00"),
                "America/New_York"
        );

        try
        {
            addCalendarEvent(CalendarActivity.this, event, time, "person@gmail.com");

        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    /*public GeneralTime buildGeneralTime(
            int year, int month,  int day,
            int hour, int minute, int second, int msec)
    {
        DateTime t;
        String startTimeStr =
                year + "-"
                + ((month>9)? month : "0" + month) + "-"
                + ((day>9)? day : "0" + day) + "T"
                + ((hour>9)? hour : "0" + hour) + ":";
    }*/

    public void addCalendarEvent(Context context, Event event, GeneralTime time, String userEmail) throws IOException
    {
            accountCredential = GoogleAccountCredential.usingOAuth2(
                    context, Arrays.asList(SCOPES))
                    .setBackOff(new ExponentialBackOff());

        if(accountCredential.getSelectedAccount() == null)
            setAccount();

        if (service == null)
        {
            service = new Calendar.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, accountCredential)
                    .setApplicationName(APP_NAME)
                    .build();
        }

        event.setStart(time.getStart());
        event.setEnd(time.getEnd());

        String[] recurrence = new String[] { "RRULE:FREQ=DAILT;COUNT=2" };
        event.setRecurrence(Arrays.asList(recurrence));

        EventAttendee[] attendees = new EventAttendee[] {
                new EventAttendee().setEmail(userEmail)
        };

        EventReminder[] reminderOverrides = new EventReminder[] {
                new EventReminder().setMethod("email").setMinutes(24 * 60), // one day
                new EventReminder().setMethod("popup").setMinutes(10)       // 10 min
        };

        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));

        String calendarID = "primary";
        // The potential IOException
        event = service.events().insert(calendarID, event).execute();
    }

    void initCredential(GoogleAccountCredential credential)
    {
        credential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
    }

    void initCalendarService(Calendar srvc)
    {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        srvc = new Calendar.Builder(
                transport, jsonFactory, accountCredential)
                .setApplicationName("Google Calendar API Android Quickstart")
                .build();
    }
}
