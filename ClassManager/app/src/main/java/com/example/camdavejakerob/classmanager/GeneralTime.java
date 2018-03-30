package com.example.camdavejakerob.classmanager;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.EventDateTime;

public class GeneralTime
{
    private EventDateTime start, end;
    /*
    * DateTime example:
    * DateTime startDateTime = new DateTime("2015-05-28T09:00:00-07:00");
    *
    * Time Zones:
    * America/New_York
    * America/Chicago
    * America/Denver
    * America/Los_Angeles
    *
    * */
    public GeneralTime(DateTime startTime, DateTime endTime, String timeZone)
    {
        setStart(startTime, timeZone);
        setEnd(endTime, timeZone);
    }

    public EventDateTime getStart()
    {
        return start;
    }

    public EventDateTime getEnd()
    {
        return end;
    }

    public void setStart(DateTime startTime, String timeZone)
    {
        start = new EventDateTime()
                .setDateTime(startTime)
                .setTimeZone(timeZone);
    }

    public void setEnd(DateTime endTime, String timeZone)
    {
        end = new EventDateTime()
                .setDateTime(endTime)
                .setTimeZone(timeZone);
    }
}
