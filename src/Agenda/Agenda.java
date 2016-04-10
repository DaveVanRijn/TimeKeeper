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
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dave van Rijn, Student 500714558, Klas IS202
 */
public class Agenda implements Serializable{
    
    private Map<Integer, List<Meeting>> meetings;
    
    public Map<Integer, List<Meeting>> getMeetings(){
        return meetings;
    }
    
    public void doChecks(){
        checkMeetings();
        checkNotifications();
    }
    
    private void checkMeetings(){
        try {
            List<Meeting> meets = new ArrayList<>();
            Calendar cal = Calendar.getInstance();
            Date today = Main.getDate(cal);
            
            for(Meeting m : meetings.get(cal.get(Calendar.YEAR))){
                if(m.getStart() == today && !m.isNotified()){
                    meets.add(m);
                }
                if(m.getStart().before(today) && m.getStart().after(today) && !m.isNotified()){
                    meets.add(m);
                }
            }
            if(meets.size() > 1){
                Keeper.showMessage("Je heb meerdere afspraken vandaag. "
                        + "Open TimeKeeper voor details.", TrayIcon.MessageType.INFO);
            } else if (meets.size() == 1){
                Keeper.showMessage(meets.get(0).getTitle() + " vindt vandaag plaats.", TrayIcon.MessageType.INFO);
            }
        } catch (ParseException ex) {
            Logger.getLogger(Agenda.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void checkNotifications(){
        
    }
    
}
