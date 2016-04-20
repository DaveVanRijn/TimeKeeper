/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TimeKeeper;

import Resource.TrayManager;
import Resource.FileUtil;
import Object.Meeting;
import Object.User;
import Exception.CharNotSupportedException;
import Resource.Util;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import org.netbeans.lib.awtextra.AbsoluteConstraints;

/**
 *
 * @author Dave van Rijn, Student 500714558, Klas IS202
 */
public class Keeper extends javax.swing.JDialog {

    private static boolean blinkOption = true, syncing;
    private static TrayManager trayManager;
    private static User currentUser;

    /**
     * Creates new form Keeper
     */
    public Keeper(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        //Properties
        setUndecorated(true);
        getRootPane().setOpaque(false);
        getContentPane().setBackground(new Color(0, 0, 0, 0));
        setBackground(new Color(0, 0, 0, 0));
        FileUtil.read();

        //Components
        initComponents();
        syncing = true;
        pnlTime.setOpaque(false);
        pnlDateInfo.setOpaque(false);
        pnlDateInfo.setVisible(false);
        pnlMeetings.setOpaque(false);
        pnlMeetings.setVisible(false);

        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        String text = (hour < 10) ? "0" + hour : Integer.toString(hour);
        lblHour.setText(text);
        text = (minute < 10) ? "0" + minute : Integer.toString(minute);
        lblMinute.setText(text);
        pack();

        //Location
        int width = (int) getPreferredSize().getWidth();
        int screenwidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        setLocation(screenwidth - (width + 20), 20);
        setAlwaysOnTop(true);

        try {
            trayManager = new TrayManager(this);
        } catch (AWTException ex) {
            JOptionPane.showMessageDialog(null, "Tray icon error",
                    "Er kan geen icoon aan het systeemvak worden toegevoed."
                    + " Open de agenda app voor opties.",
                    JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Keeper.class.getName()).log(Level.SEVERE, null, ex);
        }
        checkUser();

//        test();
        setColor();

        mouseAdapters(this);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                showDetails();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                System.out.println("hide");
                hideDetails();
            }
        });

        Timer time = new Timer(750, null);
        time.addActionListener(getActionListener());
        time.start();
        setVisible(true);
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
        currentUser.checkAgenda();
    }

    public static void changeColor(Color color, Container container) {
        for (Component c : container.getComponents()) {
            if (c instanceof Container) {
                changeColor(color, (Container) c);
            }
            if (c instanceof JLabel || c instanceof JButton) {
                c.setForeground(color);
            }
        }
        saveColor(color);
    }

    public static void showTime(boolean visible) {
        pnlTime.getParent().setVisible(visible);
    }

    public static void setBlink(boolean blink) {
        blinkOption = blink;
    }

    public static void showMessage(String message, MessageType type) {
        try {
            TrayManager.showMessage(message, type);
        } catch (AWTException ex) {
            Logger.getLogger(Keeper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void openAgenda() {
        String app = System.getProperty("user.dir") + "\\TK-Agenda.jar";
        try {
            Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "java -jar " + app});
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(TrayManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void mouseAdapters(Container container) {
        for (Component c : container.getComponents()) {
            if (c instanceof Container) {
                mouseAdapters((Container) c);
            }
            c.removeMouseListener(getMouseAdapter());
            c.addMouseListener(getMouseAdapter());
        }
    }

    private void checkTime() {
        int hourText = Integer.parseInt(lblHour.getText());
        int minuteText = Integer.parseInt(lblMinute.getText());
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        if (hourText != hour) {
            String text = (hour < 10) ? "0" + hour : Integer.toString(hour);
            lblHour.setText(text);
        }
        if (minuteText != minute) {
            String text = (minute < 10) ? "0" + minute : Integer.toString(minute);
            lblMinute.setText(text);
            if (currentUser != null) {
                currentUser.getAgenda().checkNotifications();
            }
        }
        int sec = cal.get(Calendar.SECOND);
        boolean between = sec != 0 && sec != 15 && sec != 30 && sec != 45;
        if((sec == 0 || sec == 15 || sec == 30 || sec == 45) && syncing){
            refresh();
            syncing = false;
        } else if (between){
            syncing = true;
        }
    }

    private void refresh() {
        FileUtil.read();
        if (currentUser != null) {
            if (FileUtil.get(FileUtil.USERS) != null) {
                String username = currentUser.getUsername();
                for (User u : (List<User>) FileUtil.get(FileUtil.USERS)) {
                    if (u.getUsername().equals(username)) {
                        currentUser = u;
                        break;
                    }
                }
            }
            setMeetingPanel();
        }
    }

    private void checkUser() {
        User u = (User) FileUtil.get(FileUtil.LOGGED_USER);
        if (u != null) {
            setCurrentUser(u);
            setMeetingPanel();
        }
    }

    private void setMeetingPanel() {
        pnlMeetings.remove(pnlMore);
        for (Component c : pnlMeetings.getComponents()) {
            if (c instanceof MeetingPanel) {
                pnlMeetings.remove(c);
            }
        }
        if (currentUser != null) {
            Calendar today = Calendar.getInstance();
            List<Meeting> meetings = currentUser.getAgenda().getMeetings().get(today.get(Calendar.YEAR));
            List<Meeting> todayMeetings = new ArrayList<>();
            int x = 0;
            int y = 0;
            int width = 198;
            int height = 83;
            if (meetings != null) {
                for (Meeting m : meetings) {
                    if (Util.getDateString(m.getStart()).equals(Util.getDateString(today)) && m.getEnd().after(Date.from(today.toInstant()))) {
                        todayMeetings.add(m);
                    }
                }
                if (!todayMeetings.isEmpty()) {
                    Collections.sort(todayMeetings);
                    int size = (todayMeetings.size() < 3) ? todayMeetings.size() : 3;
                    for (int i = 0; i < size; i++) {
                        pnlMeetings.add(new MeetingPanel(todayMeetings.get(i)), new AbsoluteConstraints(x, y, width, height));
                        y += 83;
                    }
                    if (todayMeetings.size() > 3) {
                        height = 70;
                        pnlMeetings.add(pnlMore, new AbsoluteConstraints(x, y, width, height));
                    }
                } else {
                    MeetingPanel pane = new MeetingPanel(null);
                    pane.setBounds(x, y, width, height);
                    pnlMeetings.add(new MeetingPanel(null), new AbsoluteConstraints(x, y, width, 60));
                }
            } else {
                MeetingPanel pane = new MeetingPanel(null);
                pane.setBounds(x, y, width, height);
                pnlMeetings.add(new MeetingPanel(null), new AbsoluteConstraints(x, y, width, 60));

            }
        }
        mouseAdapters(this);
        pack();
    }

    private ActionListener getActionListener() {
        ActionListener taskPerformer = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (blinkOption) {
                    lblSecond.setVisible(!lblSecond.isVisible());
                } else if (!lblSecond.isVisible()) {
                    lblSecond.setVisible(true);
                }
                checkTime();
            }
        };
        return taskPerformer;
    }

    private MouseAdapter getMouseAdapter() {
        MouseAdapter adap = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                showDetails();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
                Point location = Keeper.this.getLocation();
                if (!(mouseLocation.x >= location.x
                        && mouseLocation.x <= Toolkit.getDefaultToolkit().getScreenSize().width
                        && mouseLocation.y >= location.y
                        && mouseLocation.y <= location.y + Keeper.this.getHeight())) {
                    hideDetails();
                }
            }
        };

        return adap;
    }

    private void showDetails() {
        setBackground(new Color(0, 0, 0, 150));
        Calendar cal = Calendar.getInstance();
        DateFormat dateForm = new SimpleDateFormat("dd-MM-yyyy");
        String date = dateForm.format(cal.getTime());
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        String day;

        int dayNumber = cal.get(Calendar.DAY_OF_WEEK);
        switch (dayNumber) {
            case Calendar.MONDAY:
                day = "Maandag";
                break;
            case Calendar.TUESDAY:
                day = "Dinsdag";
                break;
            case Calendar.WEDNESDAY:
                day = "Woensdag";
                break;
            case Calendar.THURSDAY:
                day = "Donderdag";
                break;
            case Calendar.FRIDAY:
                day = "Vrijdag";
                break;
            case Calendar.SATURDAY:
                day = "Zaterdag";
                break;
            case Calendar.SUNDAY:
                day = "Zondag";
                break;
            default:
                day = "NaN";
                break;
        }
        lblDay.setText(day);
        lblDate.setText(date);
        lblWeek.setText("Week " + week);
        pnlDateInfo.setVisible(true);
        if (currentUser != null) {
            pnlMeetings.setVisible(true);
        }
        pack();
    }

    private void hideDetails() {
        setBackground(new Color(0, 0, 0, 0));
        pnlDateInfo.setVisible(false);
        pnlMeetings.setVisible(false);
    }

    private static void saveColor(Color color) {
        FileUtil.add(FileUtil.COLOR, color);
    }

    private void setColor() {
        Color color = (Color) FileUtil.get(FileUtil.COLOR);
        if (color == null) {
            changeColor(new Color(255, 102, 0), this);
        } else {
            changeColor(color, this);
        }
    }

    private void test() {
        try {
            System.out.println("Using test account");
            User testUser = new User("username", "password");
            Calendar now = Calendar.getInstance();
            Calendar now2 = Calendar.getInstance();
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

            now2.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + 1);
            Meeting m1 = new Meeting(testUser.nextMeetingId(), "Test 1",
                    "First test meeting", "Home", now.getTime(),
                    now2.getTime());

            Meeting m2 = new Meeting(testUser.nextMeetingId(), "Test 2",
                    "Second test meeting", "School", now.getTime(), now2.getTime());

            now2.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + 2);
            Meeting m4 = new Meeting(testUser.nextMeetingId(), "Test 4",
                    "Fourth test meeting", "Thuis", now.getTime(), now2.getTime());

            Meeting m5 = new Meeting(testUser.nextMeetingId(), "Test 5",
                    "Fifth test meeting", "Beneden", now.getTime(), now2.getTime());

            Calendar noti = (Calendar) now.clone();
            now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) + 1);
            Meeting m3 = new Meeting(testUser.nextMeetingId(), "Test 3",
                    "Third test meeting", "Somewhere", now.getTime(), now.getTime());
            m3.addNotify(noti.getTime());

            testUser.addMeeting(m1);
            testUser.addMeeting(m2);
            testUser.addMeeting(m3);
            testUser.addMeeting(m4);
            testUser.addMeeting(m5);
            setCurrentUser(testUser);
            setMeetingPanel();

        } catch (CharNotSupportedException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
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

        pnlTime = new javax.swing.JPanel();
        lblHour = new javax.swing.JLabel();
        lblSecond = new javax.swing.JLabel();
        lblMinute = new javax.swing.JLabel();
        pnlDateInfo = new javax.swing.JPanel();
        lblDay = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        lblWeek = new javax.swing.JLabel();
        pnlMeetings = new javax.swing.JPanel();
        pnlMore = new javax.swing.JPanel();
        sepMore = new javax.swing.JSeparator();
        btnMore = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        pnlTime.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblHour.setFont(new java.awt.Font("Tahoma", 1, 60)); // NOI18N
        lblHour.setForeground(new java.awt.Color(255, 102, 0));
        lblHour.setText("18");
        pnlTime.add(lblHour, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        lblSecond.setFont(new java.awt.Font("Tahoma", 1, 60)); // NOI18N
        lblSecond.setForeground(new java.awt.Color(255, 102, 0));
        lblSecond.setText(":");
        pnlTime.add(lblSecond, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 0, -1, -1));

        lblMinute.setFont(new java.awt.Font("Tahoma", 1, 60)); // NOI18N
        lblMinute.setForeground(new java.awt.Color(255, 102, 0));
        lblMinute.setText("37");
        pnlTime.add(lblMinute, new org.netbeans.lib.awtextra.AbsoluteConstraints(118, 0, -1, -1));

        lblDay.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblDay.setForeground(new java.awt.Color(255, 102, 0));
        lblDay.setText("Vrijdag");

        lblDate.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblDate.setForeground(new java.awt.Color(255, 102, 0));
        lblDate.setText("01-04-2016");

        lblWeek.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblWeek.setForeground(new java.awt.Color(255, 102, 0));
        lblWeek.setText("Week 13");

        javax.swing.GroupLayout pnlDateInfoLayout = new javax.swing.GroupLayout(pnlDateInfo);
        pnlDateInfo.setLayout(pnlDateInfoLayout);
        pnlDateInfoLayout.setHorizontalGroup(
            pnlDateInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDateInfoLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(pnlDateInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDay)
                    .addComponent(lblDate)
                    .addComponent(lblWeek)))
        );
        pnlDateInfoLayout.setVerticalGroup(
            pnlDateInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDateInfoLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(lblDay)
                .addGap(4, 4, 4)
                .addComponent(lblDate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblWeek)
                .addContainerGap())
        );

        pnlMeetings.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlMore.setOpaque(false);

        btnMore.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        btnMore.setForeground(new java.awt.Color(255, 102, 0));
        btnMore.setText("Meer..");
        btnMore.setOpaque(false);
        btnMore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoreActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMoreLayout = new javax.swing.GroupLayout(pnlMore);
        pnlMore.setLayout(pnlMoreLayout);
        pnlMoreLayout.setHorizontalGroup(
            pnlMoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sepMore, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(pnlMoreLayout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(btnMore)
                .addContainerGap(51, Short.MAX_VALUE))
        );
        pnlMoreLayout.setVerticalGroup(
            pnlMoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMoreLayout.createSequentialGroup()
                .addComponent(sepMore, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMore)
                .addContainerGap())
        );

        pnlMeetings.add(pnlMore, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 198, 60));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlDateInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlMeetings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlDateInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(pnlMeetings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoreActionPerformed
        FileUtil.add(FileUtil.SELECTED_MEETING, null);
        openAgenda();
    }//GEN-LAST:event_btnMoreActionPerformed

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Keeper.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame();
                new Keeper(frame, true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JButton btnMore;
    private static javax.swing.JLabel lblDate;
    private static javax.swing.JLabel lblDay;
    private static javax.swing.JLabel lblHour;
    private static javax.swing.JLabel lblMinute;
    private static javax.swing.JLabel lblSecond;
    private static javax.swing.JLabel lblWeek;
    private static javax.swing.JPanel pnlDateInfo;
    private static javax.swing.JPanel pnlMeetings;
    private javax.swing.JPanel pnlMore;
    private static javax.swing.JPanel pnlTime;
    private javax.swing.JSeparator sepMore;
    // End of variables declaration//GEN-END:variables
}
