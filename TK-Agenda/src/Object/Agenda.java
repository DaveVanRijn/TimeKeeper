/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Object;

import Resource.Util;
import java.awt.TrayIcon;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dave van Rijn, Student 500714558, Klas IS202
 */
public class Agenda implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<Integer, List<Meeting>> meetings;

    public Agenda() {
        meetings = new HashMap<>();
    }

    public void addMeeting(Meeting m) {
        if (!meetings.containsKey(m.getYear())) {
            meetings.put(m.getYear(), new ArrayList<>());
        }
        meetings.get(m.getYear()).add(m);
    }

    public Map<Integer, List<Meeting>> getMeetings() {
        return meetings;
    }

    public List<Meeting> getTodayMeetings() {
        List<Meeting> meets = new ArrayList<>();
        try {
            Calendar cal = Calendar.getInstance();
            Date today = Util.getDate(cal);

            if (!meetings.isEmpty()) {
                for (Meeting m : meetings.get(cal.get(Calendar.YEAR))) {
                    if (Util.getDateString(m.getStart()).equals(Util.getDateString(today))) {
                        meets.add(m);
                    } else if (m.getStart().before(today) && m.getEnd().after(today)) {
                        meets.add(m);
                    }
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(Agenda.class.getName()).log(Level.SEVERE, null, ex);
        }
        return meets;
    }

}
