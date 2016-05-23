/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shared.Resource;

import Shared.Exception.CharNotSupportedException;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;
import javax.swing.JLabel;

/**
 *
 * @author Dave van Rijn, Student 500714558, Klas IS202
 */
public class Util {

    private static final SimpleDateFormat dateForm = new SimpleDateFormat("dd/MM/yyyy");

    public static String getDateString(Date date) {
        return dateForm.format(date);
    }

    public static String getDateString(Calendar cal) {
        return getDateString(cal.getTime());
    }

    public static Date getDate(Calendar cal) throws ParseException {
        return Date.from(cal.toInstant());
    }

    public Date getDate(String date) throws ParseException {
        return dateForm.parse(date);
    }

    public static JLabel underLine(JLabel label) {
        Font font = label.getFont();
        Map attr = font.getAttributes();
        attr.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        label.setFont(font.deriveFont(attr));
        return label;
    }

    public static String encrypt(String decrypted) throws CharNotSupportedException {
        return new EncryptionKey().encrypt(decrypted);
    }

    public static String decrypt(String encrypted) throws CharNotSupportedException {
        return new EncryptionKey().decrypt(encrypted);
    }

    public static boolean checkEmail(String email) {
        Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        return p.matcher(email).matches();
    }
}
