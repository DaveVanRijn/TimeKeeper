/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shared.Object;

import Shared.Resource.Util;
import Keeper.View.Keeper;
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

    public void doChecks() {
        checkMeetings();
        checkNotifications();
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

    private void checkMeetings() {
        List<Meeting> meets = getTodayMeetings();

        if (!meetings.isEmpty()) {
            if (meets.size() > 1) {
                Keeper.showMessage("Je heb meerdere afspraken vandaag. "
                        + "Open TimeKeeper voor details.", TrayIcon.MessageType.INFO);
            } else if (meets.size() == 1) {
                String time = meets.get(0).getTime();
                Keeper.showMessage(meets.get(0).getTitle() + " om " + time.substring(0, time.length() - 3) + ".", TrayIcon.MessageType.INFO);
            }
        }
    }

    public void checkNotifications() {
        try {
            List<Meeting> notifies = new ArrayList<>();
            Calendar cal = Calendar.getInstance();
            Util.getDate(cal);
            Date date = Util.getDate(cal);
            List<Meeting> list = getMeetings().get(cal.get(Calendar.YEAR));
            if (list != null) {
                for (Meeting m : list) {
                    Date notification = m.peekNotify();
                    if (notification != null && Util.getDateString(notification).equals(Util.getDateString(date))) {
                        m.removeNotify();
                        notifies.add(m);
                    }
                }
                if (notifies.size() == 1) {
                    Meeting m = notifies.get(0);
                    Keeper.showMessage(m.getTitle() + " op "
                            + Util.getDateString(m.getStart()) + ".",
                            TrayIcon.MessageType.INFO);
                } else if (notifies.size() > 1) {
                    Keeper.showMessage("Je heb meerdere herinneringen voor "
                            + "afspraken vandaag. Open de app om deze te bekijken.",
                            TrayIcon.MessageType.INFO);
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
