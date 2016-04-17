/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agenda;

import TimeKeeper.Keeper;
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

    private void checkMeetings() {
        try {
            List<Meeting> meets = new ArrayList<>();
            Calendar cal = Calendar.getInstance();
            Date today = Main.getDate(cal);

            for (Meeting m : meetings.get(cal.get(Calendar.YEAR))) {
                if (Main.getDateString(m.getStart()).equals(Main.getDateString(today))) {
                    meets.add(m);
                } else if (m.getStart().before(today) && m.getEnd().after(today)) {
                    meets.add(m);
                }
            }
            if (meets.size() > 1) {
                Keeper.showMessage("Je heb meerdere afspraken vandaag. "
                        + "Open TimeKeeper voor details.", TrayIcon.MessageType.INFO);
            } else if (meets.size() == 1) {
                String time = meets.get(0).getTime();
                Keeper.showMessage(meets.get(0).getTitle() + " om " + time.substring(0, time.length() - 3) + ".", TrayIcon.MessageType.INFO);
            }
        } catch (ParseException ex) {
            Logger.getLogger(Agenda.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void checkNotifications() {
        try {
            List<Meeting> notifies = new ArrayList<>();
            Calendar cal = Calendar.getInstance();
            Main.getDate(cal);
            Date date = Main.getDate(cal);
            List<Meeting> list = getMeetings().get(cal.get(Calendar.YEAR));
            for (Meeting m : list) {
                Date notification = m.peekNotify();
                if (notification != null && Main.getDateString(notification).equals(Main.getDateString(date))) {
                    m.getNotify();
                    notifies.add(m);
                }
            }
            if (notifies.size() == 1) {
                Meeting m = notifies.get(0);
                Keeper.showMessage(m.getTitle() + " op "
                        + Main.getDateString(m.getStart()) + ".",
                        TrayIcon.MessageType.INFO);
            } else if (notifies.size() > 1) {
                Keeper.showMessage("Je heb meerdere herinneringen voor "
                        + "afspraken vandaag. Open de app om deze te bekijken.",
                        TrayIcon.MessageType.INFO);
            }
        } catch (ParseException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
