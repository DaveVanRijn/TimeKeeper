/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Object.User;
import Resource.EncryptionKey;
import Resource.FileUtil;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import javax.swing.JSeparator;
import javax.swing.JSpinner;

/**
 *
 * @author Dave van Rijn, Student 500714558, Klas IS202
 */
public class Register extends javax.swing.JPanel {

    private final int PASS_ERROR = 0, CONFIRM_ERROR = 1, USERNAME_ERROR = 2;

    /**
     * Creates new form Register
     */
    public Register() {
        initComponents();
        initCountries();
        postInit();
    }

    private void register(String username, String password, String firstname,
            String infix, String lastname, String address, String addressExtra,
            String city, String country, int houseNumber, Date birthday) {

    }

    private boolean checkUsername(String username) {
        List<User> users = (List<User>) FileUtil.get(FileUtil.USERS);
        if (users != null) {
            for (User u : users) {
                if (u.getUsername().equals(username)) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    private String checkPassword(String password) {
        return new EncryptionKey().isSupported(password);
    }

    private boolean checkPasswords(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    private void error(int source, String cause) {
        switch (source) {
            case PASS_ERROR:
                String unsupported = new EncryptionKey().isSupported(cause);
                lblPasswordError.setText("Ongeldig teken: '" + unsupported + "'!");
                lblPasswordError.setVisible(true);
                break;
            case CONFIRM_ERROR:
                lblConfirmPassError.setVisible(true);
                break;
            case USERNAME_ERROR:
                lblUsernameError.setVisible(true);
                break;
        }
    }

    private void setAlgemeenEnabled(boolean enabled) {
        for (Component c : pnlAlgemeen.getComponents()) {
            if (!(c instanceof JSeparator)) {
                c.setEnabled(enabled);
            }
        }
    }

    private void setPersoonsEnabled(boolean enabled) {
        for (Component c : pnlPersoons.getComponents()) {
            if (!(c instanceof JSeparator)) {
                c.setEnabled(enabled);
            }
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

        pnlAlgemeen = new javax.swing.JPanel();
        lblAlgemeenTitle = new javax.swing.JLabel();
        lblUsername = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        lblConfirmPassword = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        txtConfirmPassword = new javax.swing.JPasswordField();
        btnAlgemeenNext = new javax.swing.JButton();
        lblConfirmPassError = new javax.swing.JLabel();
        lblUsernameError = new javax.swing.JLabel();
        lblPasswordError = new javax.swing.JLabel();
        pnlPersoons = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        lblFirstname = new javax.swing.JLabel();
        lblInfix = new javax.swing.JLabel();
        lblLastname = new javax.swing.JLabel();
        lblStreet = new javax.swing.JLabel();
        lblHouseNumber = new javax.swing.JLabel();
        lblExtra = new javax.swing.JLabel();
        lblPostalCode = new javax.swing.JLabel();
        lblCity = new javax.swing.JLabel();
        lblCountry = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblPersoonsTitle = new javax.swing.JLabel();
        txtInfix = new javax.swing.JTextField();
        txtFirstname = new javax.swing.JTextField();
        txtLastname = new javax.swing.JTextField();
        txtStreet = new javax.swing.JTextField();
        txtHouseNumber = new javax.swing.JTextField();
        txtExtra = new javax.swing.JTextField();
        txtPostalCode = new javax.swing.JTextField();
        txtCity = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        cmbCountries = new javax.swing.JComboBox();
        lblBirthday = new javax.swing.JLabel();
        spnBirthday = new javax.swing.JSpinner();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        pnlAlgemeen.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblAlgemeenTitle.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblAlgemeenTitle.setText("Algemeen");
        pnlAlgemeen.add(lblAlgemeenTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(413, 11, -1, -1));

        lblUsername.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblUsername.setText("Gebruikersnaam");
        pnlAlgemeen.add(lblUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 68, -1, -1));

        lblPassword.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblPassword.setText("Wachtwoord");
        pnlAlgemeen.add(lblPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 100, -1, -1));

        lblConfirmPassword.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblConfirmPassword.setText("Bevestig wachtwoord");
        pnlAlgemeen.add(lblConfirmPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 132, -1, -1));

        txtUsername.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txtUsername.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtUsernameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtUsernameFocusLost(evt);
            }
        });
        pnlAlgemeen.add(txtUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(493, 65, 150, -1));

        txtPassword.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txtPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPasswordFocusGained(evt);
            }
        });
        pnlAlgemeen.add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(493, 97, 150, -1));

        txtConfirmPassword.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txtConfirmPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtConfirmPasswordFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtConfirmPasswordFocusLost(evt);
            }
        });
        pnlAlgemeen.add(txtConfirmPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(493, 129, 150, -1));

        btnAlgemeenNext.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        btnAlgemeenNext.setText("Volgende >>");
        pnlAlgemeen.add(btnAlgemeenNext, new org.netbeans.lib.awtextra.AbsoluteConstraints(504, 173, -1, -1));

        lblConfirmPassError.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        lblConfirmPassError.setForeground(new java.awt.Color(255, 0, 0));
        lblConfirmPassError.setText("Wachtwoorden komen niet overeen.");
        pnlAlgemeen.add(lblConfirmPassError, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 130, -1, -1));

        lblUsernameError.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        lblUsernameError.setForeground(new java.awt.Color(255, 0, 0));
        lblUsernameError.setText("Ongeldige gebruikersnaam.");
        pnlAlgemeen.add(lblUsernameError, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 70, -1, -1));

        lblPasswordError.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        lblPasswordError.setForeground(new java.awt.Color(255, 0, 0));
        lblPasswordError.setText("Ongeldig wachtwoord.");
        pnlAlgemeen.add(lblPasswordError, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 100, -1, -1));

        pnlPersoons.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        pnlPersoons.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 944, 10));

        lblFirstname.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblFirstname.setText("Voornaam*");
        pnlPersoons.add(lblFirstname, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 107, -1, -1));

        lblInfix.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblInfix.setText("Tussenvoegsel*");
        pnlPersoons.add(lblInfix, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 139, -1, -1));

        lblLastname.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblLastname.setText("Achternaam*");
        pnlPersoons.add(lblLastname, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 168, -1, -1));

        lblStreet.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblStreet.setText("Straat");
        pnlPersoons.add(lblStreet, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 200, -1, -1));

        lblHouseNumber.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblHouseNumber.setText("Huisnummer");
        pnlPersoons.add(lblHouseNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 232, -1, -1));

        lblExtra.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblExtra.setText("Toevoeging");
        pnlPersoons.add(lblExtra, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 265, -1, -1));

        lblPostalCode.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblPostalCode.setText("Postcode");
        pnlPersoons.add(lblPostalCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(472, 104, -1, -1));

        lblCity.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblCity.setText("Stad");
        pnlPersoons.add(lblCity, new org.netbeans.lib.awtextra.AbsoluteConstraints(472, 136, -1, -1));

        lblCountry.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblCountry.setText("Land");
        pnlPersoons.add(lblCountry, new org.netbeans.lib.awtextra.AbsoluteConstraints(472, 168, -1, -1));

        lblEmail.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblEmail.setText("Email");
        pnlPersoons.add(lblEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(472, 232, -1, -1));

        lblPersoonsTitle.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblPersoonsTitle.setText("Persoonsgegevens");
        pnlPersoons.add(lblPersoonsTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(374, 16, -1, -1));

        txtInfix.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        pnlPersoons.add(txtInfix, new org.netbeans.lib.awtextra.AbsoluteConstraints(254, 133, 200, -1));

        txtFirstname.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        pnlPersoons.add(txtFirstname, new org.netbeans.lib.awtextra.AbsoluteConstraints(254, 101, 200, -1));

        txtLastname.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        pnlPersoons.add(txtLastname, new org.netbeans.lib.awtextra.AbsoluteConstraints(254, 165, 200, -1));

        txtStreet.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        pnlPersoons.add(txtStreet, new org.netbeans.lib.awtextra.AbsoluteConstraints(254, 197, 200, -1));

        txtHouseNumber.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        pnlPersoons.add(txtHouseNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(254, 229, 200, -1));

        txtExtra.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        pnlPersoons.add(txtExtra, new org.netbeans.lib.awtextra.AbsoluteConstraints(254, 262, 200, -1));

        txtPostalCode.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        pnlPersoons.add(txtPostalCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(618, 101, 200, -1));

        txtCity.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        pnlPersoons.add(txtCity, new org.netbeans.lib.awtextra.AbsoluteConstraints(618, 133, 200, -1));

        txtEmail.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        pnlPersoons.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(618, 229, 200, -1));

        cmbCountries.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        cmbCountries.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        pnlPersoons.add(cmbCountries, new org.netbeans.lib.awtextra.AbsoluteConstraints(618, 165, 200, -1));

        lblBirthday.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblBirthday.setText("Geboortedatum");
        pnlPersoons.add(lblBirthday, new org.netbeans.lib.awtextra.AbsoluteConstraints(472, 200, -1, -1));

        spnBirthday.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        spnBirthday.setModel(new javax.swing.SpinnerDateModel());
        pnlPersoons.add(spnBirthday, new org.netbeans.lib.awtextra.AbsoluteConstraints(618, 197, -1, -1));

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jButton1.setText("Registreren");
        pnlPersoons.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(689, 261, -1, -1));

        jLabel1.setText("Velden met een * zijn verplicht.");
        pnlPersoons.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 76, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlPersoons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(pnlAlgemeen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlAlgemeen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlPersoons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtUsernameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsernameFocusLost
        if (!txtUsername.getText().isEmpty()) {
            if (!checkUsername(txtUsername.getText())) {

            }
        }
    }//GEN-LAST:event_txtUsernameFocusLost

    private void txtUsernameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsernameFocusGained
        lblUsernameError.setVisible(false);
    }//GEN-LAST:event_txtUsernameFocusGained

    private void txtPasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasswordFocusGained
        lblPasswordError.setVisible(false);
    }//GEN-LAST:event_txtPasswordFocusGained

    private void txtConfirmPasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtConfirmPasswordFocusGained
        lblConfirmPassError.setVisible(false);
    }//GEN-LAST:event_txtConfirmPasswordFocusGained

    private void txtConfirmPasswordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtConfirmPasswordFocusLost
        if (!checkPasswords(String.copyValueOf(txtPassword.getPassword()),
                String.copyValueOf(txtConfirmPassword.getPassword()))) {
            error(CONFIRM_ERROR, null);
        }
    }//GEN-LAST:event_txtConfirmPasswordFocusLost

    private void initCountries() {
        List<String> countries = new ArrayList<>();
        final String NEDERLAND = "Nederland";
        for (Locale locale : Locale.getAvailableLocales()) {
            try {
                if (!locale.getISO3Country().isEmpty()
                        && !locale.getCountry().isEmpty()
                        && !locale.getDisplayCountry().isEmpty()) {
                    if (!countries.contains(locale.getDisplayCountry())) {
                        countries.add(locale.getDisplayCountry());
                    }
                }
            } catch (MissingResourceException e) {

            }
        }
        Collections.sort(countries);

        cmbCountries.removeAllItems();
        for (String country : countries) {
            cmbCountries.addItem(country);
        }
        cmbCountries.setSelectedItem(NEDERLAND);
    }

    private void postInit() {
        //Title locations
        int x;
        int y;

        x = (getSize().width - lblAlgemeenTitle.getPreferredSize().width) / 2;
        y = lblAlgemeenTitle.getLocation().y;

        lblAlgemeenTitle.setLocation(x, y);

        x = (getSize().width - lblPersoonsTitle.getPreferredSize().width) / 2;
        y = lblPersoonsTitle.getLocation().y;

        lblPersoonsTitle.setLocation(x, y);

        //Birthday picker
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spnBirthday, "dd-MM-yyyy");
        spnBirthday.setEditor(editor);

        //Disable Persoons
        setPersoonsEnabled(false);
        
        //Hide error labels
        lblUsernameError.setVisible(false);
        lblPasswordError.setVisible(false);
        lblConfirmPassError.setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlgemeenNext;
    private javax.swing.JComboBox cmbCountries;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblAlgemeenTitle;
    private javax.swing.JLabel lblBirthday;
    private javax.swing.JLabel lblCity;
    private javax.swing.JLabel lblConfirmPassError;
    private javax.swing.JLabel lblConfirmPassword;
    private javax.swing.JLabel lblCountry;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblExtra;
    private javax.swing.JLabel lblFirstname;
    private javax.swing.JLabel lblHouseNumber;
    private javax.swing.JLabel lblInfix;
    private javax.swing.JLabel lblLastname;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPasswordError;
    private javax.swing.JLabel lblPersoonsTitle;
    private javax.swing.JLabel lblPostalCode;
    private javax.swing.JLabel lblStreet;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JLabel lblUsernameError;
    private javax.swing.JPanel pnlAlgemeen;
    private javax.swing.JPanel pnlPersoons;
    private javax.swing.JSpinner spnBirthday;
    private javax.swing.JTextField txtCity;
    private javax.swing.JPasswordField txtConfirmPassword;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtExtra;
    private javax.swing.JTextField txtFirstname;
    private javax.swing.JTextField txtHouseNumber;
    private javax.swing.JTextField txtInfix;
    private javax.swing.JTextField txtLastname;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtPostalCode;
    private javax.swing.JTextField txtStreet;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
