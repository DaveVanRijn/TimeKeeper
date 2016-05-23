/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shared.Object;

import java.util.Date;

/**
 *
 * @author Dave van Rijn, Student 500714558, Klas IS202
 */
public class Homework extends Meeting{
    
    public Homework(int id, String title, String description, String location,
            Date start, Date end) {
        super(id, title, description, location, start, end);
    }
    
}
