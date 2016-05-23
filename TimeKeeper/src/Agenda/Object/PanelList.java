/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agenda.Object;

import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Dave van Rijn, Student 500714558, Klas IS202
 */
public class PanelList {
    
    private final ArrayList<JPanel> panels;
    private int showingIndex;
    
    public PanelList(){
        panels = new ArrayList<>();
        showingIndex = -1;
    }
    
    public void add(JPanel panel){
        if(showingIndex != panels.size() - 1){
            removeFrom(showingIndex);
        }
        panels.add(panel) ;
        showingIndex++;
    }
    
    public JPanel next() throws IndexOutOfBoundsException{
        if(hasNext()){
            showingIndex++;
            return panels.get(showingIndex);
        }
        throw new IndexOutOfBoundsException("End of list already reached!");
    }
    
    public JPanel prev() throws IndexOutOfBoundsException{
        if(hasPrev()){
            showingIndex--;
            return panels.get(showingIndex);
        }
        return null;
    }
    
    public boolean hasNext(){
        return showingIndex < panels.size() - 1;
    }
    
    public boolean hasPrev(){
        return showingIndex > 0;
    }
    
    private void removeFrom(int index){
        for(int i = index + 1; i < panels.size(); i++){
            panels.remove(i);
        }
    }
}
