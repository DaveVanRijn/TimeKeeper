/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agenda.View;

import Shared.Exception.CharNotSupportedException;
import Shared.Object.Meeting;
import Agenda.Object.PanelList;
import Keeper.View.Keeper;
import Shared.Object.User;
import Shared.Options.Options;
import Shared.Resource.FileUtil;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.WindowConstants;

/**
 *
 * @author Dave van Rijn, Student 500714558, Klas IS202
 */
public class Agenda extends javax.swing.JFrame {

    private static Agenda mainframe;
    private User currentUser;
    private PanelList panels;

    /**
     * Creates new form Main
     */
    public Agenda() {
        initComponents();
        //Init PanelList
        panels = new PanelList();
        try {
            setIconImage(ImageIO.read(getClass().getResource("/Agenda/Resource/agendaIcon.png")));
        } catch (IOException ex) {
            Logger.getLogger(Agenda.class.getName()).log(Level.SEVERE, null, ex);
        }
        setTitle("TimeKeeper - Agenda");

        pnlMain.setLayout(new BorderLayout());

        try {
            FileUtil.init(true);
        } catch (IOException ex) {
            Logger.getLogger(Agenda.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Calendar now = Calendar.getInstance();
            Calendar now2 = Calendar.getInstance();
            User test = new User("Dave", "Hoi");
            now2.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + 1);
            Meeting m1 = new Meeting(test.nextMeetingId(), "Test 1 met een heel lange titel dus zal wel afgekort moeten worden",
                    "First test meeting", "Home", now.getTime(),
                    now2.getTime());

            Meeting m2 = new Meeting(test.nextMeetingId(), "Test 2",
                    "Second test meeting", "School", now.getTime(), now2.getTime());

            now2.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + 2);
            Meeting m4 = new Meeting(test.nextMeetingId(), "Test 4",
                    "Fourth test meeting", "Thuis", now.getTime(), now2.getTime());

            Meeting m5 = new Meeting(test.nextMeetingId(), "Test 5",
                    "Fifth test meeting", "Beneden", now.getTime(), now2.getTime());

            Calendar noti = (Calendar) now.clone();
            now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) + 1);
            Meeting m3 = new Meeting(test.nextMeetingId(), "Test 3",
                    "Third test meeting", "Somewhere", now.getTime(), now.getTime());
            m3.addNotify(noti.getTime());
            test.addMeeting(m1);
            test.addMeeting(m2);
            test.addMeeting(m3);
            test.addMeeting(m4);
            test.addMeeting(m5);
            ArrayList<User> users = new ArrayList<>();
            users.add(test);

            FileUtil.add(FileUtil.USERS, users);
        } catch (CharNotSupportedException ex) {
            Logger.getLogger(Agenda.class.getName()).log(Level.SEVERE, null, ex);
        }

        JPanel panel = getStartPanel();
        if (panel instanceof Login) {
            menu.setVisible(false);
        } else {
            menu.setVisible(true);
        }
        if (!(panel instanceof Login || panel instanceof Register)) {
            panels.add(panel);
            btnBack.setVisible(panels.hasPrev());
            btnNext.setVisible(panels.hasNext());
        } else {
            btnBack.setVisible(panels.hasPrev());
            btnNext.setVisible(panels.hasNext());
        }
        pnlMain.add(panel, BorderLayout.CENTER);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showOptionDialog(null,
                        "Weet je zeker dat je wil afsluiten?", "Bevestig",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                        new Object[]{"Ja", "Nee"}, "Nee");
                if (option == JOptionPane.YES_OPTION) {
                    Agenda.this.dispose();
                }
            }
        });

        Timer timer = new Timer(15000, null);
        timer.addActionListener(timerAction());
        timer.start();

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void setPanel(Object o) {
        refresh();
        JPanel panel = (JPanel) o;

        if (panel instanceof Login) {
            mainframe.menu.setVisible(false);
        } else {
            mainframe.menu.setVisible(true);
        }

        mainframe.pnlMain.removeAll();
        mainframe.pnlMain.add(panel, BorderLayout.CENTER);

        boolean loginOrRegister = panel instanceof Login || panel instanceof Register;
        if (!loginOrRegister) {
            mainframe.panels.add(panel);
        }
        mainframe.btnBack.setVisible(mainframe.panels.hasPrev() && !loginOrRegister);
        mainframe.btnNext.setVisible(mainframe.panels.hasNext() && !loginOrRegister);

        mainframe.pack();
        mainframe.setLocationRelativeTo(null);
    }

    public static User getCurrentUser() {
        return mainframe.currentUser;
    }

    public static void setCurrentUser(User user) {
        mainframe.currentUser = user;
    }

    private static JPanel getStartPanel() {
        User current = (User) FileUtil.get(FileUtil.LOGGED_USER);
        Meeting selected = (Meeting) FileUtil.get(FileUtil.SELECTED_MEETING);
        List<User> users = (List<User>) FileUtil.get(FileUtil.USERS);

        if (current != null) {
            //Log user in
            if (selected != null) {
                //Go to meeting page
                return new MeetingDetail(selected);
            } else {
                return new Startpage();
                //Go to startpage
            }
        } else if (users != null && !users.isEmpty()) {
            //Go to login page
            return new Login();
        } else {
            //Go to register page
            return new Register();
        }
    }

    private static void refresh() {
        try {
            FileUtil.init(true);
            if (mainframe.currentUser != null) {
                if (FileUtil.get(FileUtil.USERS) != null) {
                    String username = mainframe.currentUser.getUsername();
                    for (User u : (List<User>) FileUtil.get(FileUtil.USERS)) {
                        if (u.getUsername().equals(username)) {
                            mainframe.currentUser = u;
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Agenda.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void logout() {
        currentUser = null;
        FileUtil.remove(FileUtil.LOGGED_USER);
        setPanel(new Login());
        Keeper.refresh();
    }

    private static ActionListener timerAction() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refresh();
            }

        };
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
        btnBack = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        menu = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        btnNewMeeting = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem1 = new javax.swing.JMenuItem();
        btnLogout = new javax.swing.JMenuItem();
        btnExit = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout pnlMainLayout = new javax.swing.GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        pnlMainLayout.setVerticalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 249, Short.MAX_VALUE)
        );

        btnBack.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnBack.setText("Vorige");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnNext.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnNext.setText("Volgende");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        jMenu1.setText("File");
        jMenu1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        btnNewMeeting.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        btnNewMeeting.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnNewMeeting.setText("Nieuwe afspraak");
        jMenu1.add(btnNewMeeting);
        jMenu1.add(jSeparator1);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jMenuItem1.setText("Options");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        btnLogout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        btnLogout.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnLogout.setText("Uitloggen");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });
        jMenu1.add(btnLogout);

        btnExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        btnExit.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnExit.setText("Afsluiten");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        jMenu1.add(btnExit);

        menu.add(jMenu1);

        setJMenuBar(menu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btnBack)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnNext))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBack)
                    .addComponent(btnNext))
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(pnlMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        int option = JOptionPane.showOptionDialog(null,
                "Weet je zeker dat je wil afsluiten?", "Bevestig",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new Object[]{"Ja", "Nee"}, "Nee");
        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        logout();
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        setPanel(mainframe.panels.prev());
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        setPanel(mainframe.panels.next());
    }//GEN-LAST:event_btnNextActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        new Options();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

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
            java.util.logging.Logger.getLogger(Agenda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Agenda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Agenda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Agenda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                mainframe = new Agenda();
                User u = (User) FileUtil.get(FileUtil.LOGGED_USER);
                if(u != null){
                    setCurrentUser(u);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JMenuItem btnExit;
    private javax.swing.JMenuItem btnLogout;
    private javax.swing.JMenuItem btnNewMeeting;
    private javax.swing.JButton btnNext;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JMenuBar menu;
    private javax.swing.JPanel pnlMain;
    // End of variables declaration//GEN-END:variables
}
