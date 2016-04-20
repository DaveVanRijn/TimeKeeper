/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Dave van Rijn, Student 500714558, Klas IS202
 */
public class Meeting implements Serializable, Comparable<Meeting> {

    private static final long serialVersionUID = 1L;

    private final int id;
    private String title, description, location;
    private Date start, end;
    private final List<Date> notifies;

    public Meeting(int id, String title, String description, String location,
            Date start, Date end) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.start = start;
        this.end = end;
        this.notifies = new ArrayList<>();
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

    public List<Date> getNotifies() {
        return notifies;
    }

    public void addNotify(Date notify) {
        notifies.add(notify);
        Collections.sort(notifies);
    }

    public Date peekNotify() {
        if (notifies.isEmpty()) {
            return null;
        }
        return notifies.get(0);
    }

    public Date getNotify() {
        return notifies.remove(0);
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
        return hourString + ":" + minuteString;
    }

    public String getMeetingTime() {
        StringBuilder builder = new StringBuilder();
        Calendar cal = Calendar.getInstance();

        //Start time
        cal.setTime(start);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        String hourString = (hour < 10) ? "0" + hour : Integer.toString(hour);
        String minuteString = (minute < 10) ? "0" + minute : Integer.toString(minute);
        builder.append(hourString);
        builder.append(":");
        builder.append(minuteString);

        builder.append(" - ");

        //End time
        cal.setTime(end);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);
        hourString = (hour < 10) ? "0" + hour : Integer.toString(hour);
        minuteString = (minute < 10) ? "0" + minute : Integer.toString(minute);
        builder.append(hourString);
        builder.append(":");
        builder.append(minuteString);

        return builder.toString();
    }

    @Override
    public int compareTo(Meeting o) {
        return start.compareTo(o.getStart());
    }
}
