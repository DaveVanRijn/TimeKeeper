/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agenda;

/**
 *
 * @author Dave van Rijn, Student 500714558, Klas IS202
 */
public class CharNotSupportedException extends Exception {

    /**
     * Creates a new instance of <code>CharNotSupportedException</code> without
     * detail message.
     */
    public CharNotSupportedException() {
    }

    /**
     * Constructs an instance of <code>CharNotSupportedException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CharNotSupportedException(String msg) {
        super(msg);
    }
    
    public CharNotSupportedException(char c){
        super("Character " + c + " is not supported by this key.");
    }
}
