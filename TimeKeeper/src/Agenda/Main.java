/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agenda;

import Resource.EncryptionKey;
import Exception.CharNotSupportedException;
import TimeKeeper.FileUtil;
import java.awt.TrayIcon;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import TimeKeeper.Keeper;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Map;
import java.util.Stack;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Dave van Rijn, Student 500714558, Klas IS202
 */
public class Main extends javax.swing.JFrame {

    private static Main mainframe;
    private static final SimpleDateFormat dateForm = new SimpleDateFormat("dd/MM/yyyy");

    private final Stack<JPanel> panels;

    /**
     * Creates new form Agenda
     */
    public Main() {
        initComponents();
        panels = new Stack<>();
        pnlMain.setLayout(new BorderLayout());
        if (Keeper.getCurrentUser() == null) {
            pnlMain.add(new Login(), BorderLayout.CENTER);
        } else {
            pnlMain.add(new Startpage(), BorderLayout.CENTER);
        }

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
//        System.out.println("Test");
//        test();
    }

    public static void setPanel(Object o) {
        mainframe.pnlMain.removeAll();
        JPanel panel = (JPanel) o;
        mainframe.panels.push(panel);

        mainframe.pnlMain.add(panel, BorderLayout.CENTER);

        mainframe.pack();
        mainframe.setLocationRelativeTo(null);
    }

    public static void logout() {
        Keeper.setCurrentUser(null);
        mainframe.panels.clear();
        setPanel(new Login());
    }

    private void prevPanel() {
        //Remove showing panel
        panels.pop();
        //Show previous panel
        setPanel(panels.pop());
    }

    public static String getDateString(Date date) {
        return dateForm.format(date);
    }

    public static String getDateString(Calendar cal) {
        return Main.getDateString(cal.getTime());
    }

    public static Date getDate(Calendar cal) throws ParseException {
        return dateForm.parse(getDateString(cal));
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

    private void test() {
        try {
            System.out.println("Start test");
            User testUser = new User("username", "password");
            Calendar now = Calendar.getInstance();
            Calendar birth = Calendar.getInstance();
            birth.set(Calendar.DAY_OF_MONTH, 6);
            birth.set(Calendar.MONTH, 9);
            birth.set(Calendar.YEAR, 1994);
            testUser.setAddress("Achterbos");
            testUser.setBirthday(birth.getTime());
            testUser.setCity("Vinkeveen");
            testUser.setCountry("Nederland");
            testUser.setFirstname("Dave");
            testUser.setInfix("van");
            testUser.setLastname("Rijn");
            testUser.setHouseNumber(75);
            testUser.setPostalCode("3645CB");

            Meeting m1 = new Meeting(testUser.nextMeetingId(), "Test 1",
                    "First test meeting", "Home", now.getTime(),
                    now.getTime(), null);

            Meeting m2 = new Meeting(testUser.nextMeetingId(), "Test 2",
                    "Second test meeting", "School", now.getTime(), now.getTime(),
                    null);

            Calendar noti = (Calendar) now.clone();
            now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) + 1);
            Meeting m3 = new Meeting(testUser.nextMeetingId(), "Test 3",
                    "Third test meeting", "Somewhere", now.getTime(), now.getTime(), noti.getTime());

            testUser.addMeeting(m1);
            testUser.addMeeting(m2);
            testUser.addMeeting(m3);

            Keeper.setCurrentUser(testUser);

        } catch (CharNotSupportedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMain = new javax.swing.JPanel();
        menubar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout pnlMainLayout = new javax.swing.GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        pnlMainLayout.setVerticalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 279, Short.MAX_VALUE)
        );

        jMenu1.setText("Bestand");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem1.setText("Sluiten");
        jMenuItem1.setToolTipText("Sluit de agenda");
        jMenu1.add(jMenuItem1);

        menubar.add(jMenu1);

        jMenu2.setText("Bewerken");
        menubar.add(jMenu2);

        setJMenuBar(menubar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                mainframe = new Main();
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuBar menubar;
    private javax.swing.JPanel pnlMain;
    // End of variables declaration//GEN-END:variables
}
