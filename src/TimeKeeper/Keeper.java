/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TimeKeeper;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author Dave van Rijn, Student 500714558, Klas IS202
 */
public class Keeper extends javax.swing.JDialog {

    private static boolean blinkOption = true;
    private static TrayManager trayManager;

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
        setColor();
        pnlTime.setOpaque(false);
        pnlDateInfo.setOpaque(false);
        pnlDateInfo.setVisible(false);

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

        lblHour.addMouseListener(getMouseAdapter());
        lblMinute.addMouseListener(getMouseAdapter());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                showDetails();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hideDetails();
            }
        });

        Timer time = new Timer(750, null);
        time.addActionListener(getActionListener());
        time.start();

        try {
            trayManager = new TrayManager();
        } catch (AWTException ex) {
            JOptionPane.showMessageDialog(null, "Tray icon error",
                    "Er kan geen icoon aan het systeemvak worden toegevoed."
                    + " Open de agenda app voor opties.",
                    JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Keeper.class.getName()).log(Level.SEVERE, null, ex);
        }
        setVisible(true);
    }

    public static void changeColor(Color color) {
        lblHour.setForeground(color);
        lblMinute.setForeground(color);
        lblSecond.setForeground(color);
        lblDay.setForeground(color);
        lblDate.setForeground(color);
        lblWeek.setForeground(color);

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
        }
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
        this.setBackground(new Color(0, 0, 0, 150));
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
        this.pack();
    }

    private void hideDetails() {
        this.setBackground(new Color(0, 0, 0, 0));
        pnlDateInfo.setVisible(false);
    }

    private static void saveColor(Color color) {
        FileUtil.add(FileUtil.COLOR, color);
    }

    private void setColor() {
        Color color = (Color) FileUtil.get(FileUtil.COLOR);
        if (color == null) {
            changeColor(new Color(255, 102, 0));
        } else {
            changeColor(color);
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlDateInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlDateInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 109, Short.MAX_VALUE))
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
            java.util.logging.Logger.getLogger(Keeper.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Keeper.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Keeper.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Keeper.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
    private static javax.swing.JLabel lblDate;
    private static javax.swing.JLabel lblDay;
    private static javax.swing.JLabel lblHour;
    private static javax.swing.JLabel lblMinute;
    private static javax.swing.JLabel lblSecond;
    private static javax.swing.JLabel lblWeek;
    private static javax.swing.JPanel pnlDateInfo;
    private static javax.swing.JPanel pnlTime;
    // End of variables declaration//GEN-END:variables
}
