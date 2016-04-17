/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Object;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Dave van Rijn, Student 500714558, Klas IS202
 */
public class Meeting implements Serializable, Comparable<Meeting> {

    private static final long serialVersionUID = 1L;

    private final int id;
    private String title, description, location;
    private Date start, end, notify;
    private boolean notified;

    public Meeting(int id, String title, String description, String location,
            Date start, Date end, Date notify) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.start = start;
        this.end = end;
        this.notify = notify;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getNotify() {
        return notify;
    }

    public void setNotify(Date notify) {
        this.notify = notify;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public int getYear() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        return cal.get(Calendar.YEAR);
    }

    public String getTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        String hourString = (hour < 10) ? "0" + hour : Integer.toString(hour);
        String minuteString = (minute < 10) ? "0" + minute : Integer.toString(minute);
        return hourString + ":" + minuteString + ":00";
    }

    @Override
    public int compareTo(Meeting o) {
        return start.compareTo(o.getStart());
    }

}
