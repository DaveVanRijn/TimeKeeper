/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
