/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Exception.CharNotSupportedException;
import Object.Meeting;
import Object.User;
import Resource.EncryptionKey;
import Resource.FileUtil;
import View.Login;
import View.MeetingDetail;
import View.Register;
import View.Startpage;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.WindowConstants;

/**
 *
 * @author Dave van Rijn, Student 500714558, Klas IS202
 */
public class Main extends javax.swing.JFrame {

    private static Main mainframe;
    private final Stack<JPanel> panels;
    private User currentUser;

    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        try {
            setIconImage(ImageIO.read(getClass().getResource("/Resource/agendaIcon.png")));
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        setTitle("TimeKeeper - Agenda");
        panels = new Stack<>();

        pnlMain.setLayout(new BorderLayout());

        FileUtil.read();
//        try {
//            User test = new User("Dave", "Hoi");
//            ArrayList<User> users = new ArrayList<>();
//            users.add(test);
//            FileUtil.add(FileUtil.USERS, users);
//        } catch (CharNotSupportedException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }

        JPanel panel = getStartPanel();
        if (panel instanceof Login) {
            menu.setVisible(false);
        } else {
            menu.setVisible(true);
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
                    System.exit(0);
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

        mainframe.panels.push(panel);

        mainframe.pnlMain.removeAll();
        mainframe.pnlMain.add(panel, BorderLayout.CENTER);

        mainframe.pack();
        mainframe.setLocationRelativeTo(null);
    }

    private static void prevPanel() {
        //Remove showing panel
        mainframe.panels.pop();
        //Show and remove previous panel
        setPanel(mainframe.panels.pop());
    }

    public static User getCurrentUser() {
        return mainframe.currentUser;
    }

    public static void setCurrentUser(User user) {
        mainframe.currentUser = user;
    }
    
    public static boolean checkEmail(String email){
        Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        return p.matcher(email).matches();
    }

    public static String encrypt(String text) throws CharNotSupportedException {
        return new EncryptionKey().encrypt(text);
    }

    public static String decrypt(String text) throws CharNotSupportedException {
        return new EncryptionKey().decrypt(text);
    }

    private static JPanel getStartPanel() {
        User current = (User) FileUtil.get(FileUtil.LOGGED_USER);
        Meeting selected = (Meeting) FileUtil.get(FileUtil.SELECTED_MEETING);
        List<User> users = (List<User>) FileUtil.get(FileUtil.USERS);

        if (current != null) {
            setCurrentUser(current);
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
        FileUtil.read();
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
    }

    private void logout() {
        currentUser = null;
        setPanel(new Login());
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
        menu = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        btnNewMeeting = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        btnLogout = new javax.swing.JMenuItem();
        btnExit = new javax.swing.JMenuItem();
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

        jMenu1.setText("File");

        btnNewMeeting.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        btnNewMeeting.setText("Nieuwe afspraak");
        jMenu1.add(btnNewMeeting);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_BACK_SPACE, 0));
        jMenuItem1.setText("Vorige");
        jMenu1.add(jMenuItem1);
        jMenu1.add(jSeparator1);

        btnLogout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        btnLogout.setText("Uitloggen");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });
        jMenu1.add(btnLogout);

        btnExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        btnExit.setText("Afsluiten");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        jMenu1.add(btnExit);

        menu.add(jMenu1);

        jMenu2.setText("Edit");
        menu.add(jMenu2);

        setJMenuBar(menu);

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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                mainframe = new Main();
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem btnExit;
    private javax.swing.JMenuItem btnLogout;
    private javax.swing.JMenuItem btnNewMeeting;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JMenuBar menu;
    private javax.swing.JPanel pnlMain;
    // End of variables declaration//GEN-END:variables
}
