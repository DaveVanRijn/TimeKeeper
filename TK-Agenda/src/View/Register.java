/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Exception.CharNotSupportedException;
import Main.Main;
import Object.User;
import Resource.EncryptionKey;
import Resource.FileUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Dave van Rijn, Student 500714558, Klas IS202
 */
public class Register extends javax.swing.JPanel {

    private final int PASS_ERROR = 0, CONFIRM_ERROR = 1, USERNAME_ERROR = 2;
    private boolean loginPressed = false;

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
            String city, String country, String houseNumber, Date birthday) {
        try {
            int house;
            if(houseNumber.isEmpty()){
                house = -1;
            } else {
                house = Integer.parseInt(houseNumber);
            }
            
            User user = new User(username, password);
            user.setFirstname(firstname);
            user.setInfix(infix);
            user.setLastname(lastname);
            user.setAddress(address);
            user.setAddressExtra(addressExtra);
            user.setCity(city);
            user.setCountry(country);
            user.setHouseNumber(house);
            user.setBirthday(birthday);

            List<User> users = (List<User>) FileUtil.get(FileUtil.USERS);
            if (users == null) {
                users = new ArrayList<>();
            }
            users.add(user);
            FileUtil.add(FileUtil.USERS, users);

            Main.setCurrentUser(user);
            if (Main.getCurrentUser() != null) {
                Main.setPanel(new Startpage());
            }
        } catch (CharNotSupportedException ex) {
            Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean checkUsername(String username) {
        boolean valid = true;
        if (!username.isEmpty()) {
            List<User> users = (List<User>) FileUtil.get(FileUtil.USERS);
            if (users != null) {
                for (User u : users) {
                    if (u.getUsername().equals(username)) {
                        valid = false;
                        break;
                    }
                }
            }
        } else {
            valid = false;
        }
        if (!valid) {
            lblUsernameError.setVisible(true);
        }
        return valid;
    }

    private boolean checkPassword(String password) {
        return new EncryptionKey().isSupported(password) == null;
    }

    private boolean checkPasswords(String password, String confirmPassword) {
        boolean valid = true;
        if (password.isEmpty()) {
            lblPasswordError.setVisible(true);
            valid = false;
        }
        if (!password.equals(confirmPassword)) {
            lblConfirmPassError.setVisible(true);
            valid = false;
        }
        return valid;
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

    private boolean checkInput(String firstname, String infix, String lastname,
            String street, String houseNumber, String postalCode,
            String city, String email) {
        boolean valid = true;
        Border errorBorder = BorderFactory.createLineBorder(Color.RED);
        //Pattern for letters only, separated by a whitespace or '-'
        Pattern p = Pattern.compile("[A-Za-z]*([ |-][A-Za-z]+)?");
        //Firstname is empty or contains anything other than letters
        if (firstname.isEmpty() || !p.matcher(firstname).matches()) {
            lblFirstname.setForeground(Color.RED);
            txtFirstname.setBorder(errorBorder);
            valid = false;
        }
        //Infix contains anything other than letters
        if (!p.matcher(infix.toLowerCase()).matches()) {
            lblInfix.setForeground(Color.RED);
            txtInfix.setBorder(errorBorder);
            valid = false;
        }

        //Lastname is empty or contains anything other than letters
        if (lastname.isEmpty() || !p.matcher(lastname).matches()) {
            lblLastname.setForeground(Color.RED);
            txtLastname.setBorder(errorBorder);
            valid = false;
        }

        //Street contains anyting other than letters
        if (!p.matcher(street).matches()) {
            lblStreet.setForeground(Color.RED);
            txtStreet.setBorder(errorBorder);
            valid = false;
        }

        //Housenumber contains anything other than numbers
        if (!houseNumber.isEmpty() && !Pattern.compile("\\d+").matcher(houseNumber).matches()) {
            lblHouseNumber.setForeground(Color.RED);
            txtHouseNumber.setBorder(errorBorder);
            valid = false;
        }

        //Postal code is not a (Dutch) postal code
        if (!postalCode.isEmpty() && !Pattern.compile("\\d{4}+[A-Za-z]{2}+").matcher(postalCode).matches()) {
            lblPostalCode.setForeground(Color.RED);
            txtPostalCode.setBorder(errorBorder);
            valid = false;
        }

        //City contains anything other than letters
        if (!p.matcher(city).matches()) {
            lblCity.setForeground(Color.RED);
            txtCity.setBorder(errorBorder);
            valid = false;
        }

        //Email is not a valid emailaddress
        if (!email.isEmpty() && !Main.checkEmail(email)) {
            lblEmail.setForeground(Color.RED);
            txtEmail.setBorder(errorBorder);
            valid = false;
        }

        return valid;
    }

    private void resetField(JComponent component) {
        if (component.getName() != null) {
            String componentName = component.getName().replace("txt", "lbl");
            Component label = null;
            for (Component c : pnlPersoons.getComponents()) {
                String name = c.getName();
                if (name != null && name.equals(componentName)) {
                    label = c;
                    break;
                }
            }
            component.setBorder(txtUsername.getBorder());
            if (label != null) {
                label.setForeground(Color.BLACK);
            }
        }
    }

    private void resetAllFields() {
        for (Component c : pnlPersoons.getComponents()) {
            JComponent comp = (JComponent) c;
            if (comp instanceof JTextField) {
                resetField(comp);
            }
        }
    }

    private void setFocusGained(JComponent component) {
        component.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                resetField(component);
            }
        });
    }

    private boolean checkAlgemeen(String username, char[] pass, char[] confirmPass) {
        String password = String.copyValueOf(pass), confirmPassword = String.copyValueOf(confirmPass);
        boolean passCheck = checkPasswords(password, confirmPassword);
        boolean nameCheck = checkUsername(username);
        return nameCheck && passCheck;
    }

    private void setAlgemeenAction(JTextField c) {

        c.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnAlgemeenNext.doClick();
            }
        });
    }

    private void setPersoonsAction(JTextField c) {
        c.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnRegister.doClick();
            }
        });
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
        lblLogin = new javax.swing.JLabel();
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
        btnRegister = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();

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
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtUsernameFocusLost(evt);
            }
        });
        pnlAlgemeen.add(txtUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(493, 65, 150, -1));

        txtPassword.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txtPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPasswordFocusLost(evt);
            }
        });
        pnlAlgemeen.add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(493, 97, 150, -1));

        txtConfirmPassword.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txtConfirmPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtConfirmPasswordFocusLost(evt);
            }
        });
        pnlAlgemeen.add(txtConfirmPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(493, 129, 150, -1));

        btnAlgemeenNext.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        btnAlgemeenNext.setText("Volgende >>");
        btnAlgemeenNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlgemeenNextActionPerformed(evt);
            }
        });
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

        lblLogin.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblLogin.setForeground(new java.awt.Color(0, 153, 255));
        lblLogin.setText("Inloggen met een bestaand account");
        lblLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlAlgemeen.add(lblLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        pnlPersoons.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        pnlPersoons.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 944, 10));

        lblFirstname.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblFirstname.setText("Voornaam*");
        lblFirstname.setName("lblFirstname"); // NOI18N
        pnlPersoons.add(lblFirstname, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 107, -1, -1));

        lblInfix.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblInfix.setText("Tussenvoegsel");
        lblInfix.setName("lblInfix"); // NOI18N
        pnlPersoons.add(lblInfix, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 139, -1, -1));

        lblLastname.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblLastname.setText("Achternaam*");
        lblLastname.setName("lblLastname"); // NOI18N
        pnlPersoons.add(lblLastname, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 168, -1, -1));

        lblStreet.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblStreet.setText("Straat");
        lblStreet.setName("lblStreet"); // NOI18N
        pnlPersoons.add(lblStreet, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 200, -1, -1));

        lblHouseNumber.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblHouseNumber.setText("Huisnummer");
        lblHouseNumber.setName("lblHouseNumber"); // NOI18N
        pnlPersoons.add(lblHouseNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 232, -1, -1));

        lblExtra.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblExtra.setText("Toevoeging");
        pnlPersoons.add(lblExtra, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 265, -1, -1));

        lblPostalCode.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblPostalCode.setText("Postcode");
        lblPostalCode.setName("lblPostalCode"); // NOI18N
        pnlPersoons.add(lblPostalCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(472, 104, -1, -1));

        lblCity.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblCity.setText("Stad");
        lblCity.setName("lblCity"); // NOI18N
        pnlPersoons.add(lblCity, new org.netbeans.lib.awtextra.AbsoluteConstraints(472, 136, -1, -1));

        lblCountry.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblCountry.setText("Land");
        pnlPersoons.add(lblCountry, new org.netbeans.lib.awtextra.AbsoluteConstraints(472, 168, -1, -1));

        lblEmail.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblEmail.setText("Email");
        lblEmail.setName("lblEmail"); // NOI18N
        pnlPersoons.add(lblEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(472, 232, -1, -1));

        lblPersoonsTitle.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblPersoonsTitle.setText("Persoonsgegevens");
        pnlPersoons.add(lblPersoonsTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(374, 16, -1, -1));

        txtInfix.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txtInfix.setName("txtInfix"); // NOI18N
        txtInfix.setNextFocusableComponent(txtLastname);
        pnlPersoons.add(txtInfix, new org.netbeans.lib.awtextra.AbsoluteConstraints(254, 133, 200, -1));

        txtFirstname.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txtFirstname.setName("txtFirstname"); // NOI18N
        txtFirstname.setNextFocusableComponent(txtInfix);
        pnlPersoons.add(txtFirstname, new org.netbeans.lib.awtextra.AbsoluteConstraints(254, 101, 200, -1));

        txtLastname.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txtLastname.setName("txtLastname"); // NOI18N
        txtLastname.setNextFocusableComponent(txtStreet);
        pnlPersoons.add(txtLastname, new org.netbeans.lib.awtextra.AbsoluteConstraints(254, 165, 200, -1));

        txtStreet.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txtStreet.setName("txtStreet"); // NOI18N
        txtStreet.setNextFocusableComponent(txtHouseNumber);
        pnlPersoons.add(txtStreet, new org.netbeans.lib.awtextra.AbsoluteConstraints(254, 197, 200, -1));

        txtHouseNumber.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txtHouseNumber.setName("txtHouseNumber"); // NOI18N
        txtHouseNumber.setNextFocusableComponent(txtExtra);
        pnlPersoons.add(txtHouseNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(254, 229, 200, -1));

        txtExtra.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txtExtra.setNextFocusableComponent(txtPostalCode);
        pnlPersoons.add(txtExtra, new org.netbeans.lib.awtextra.AbsoluteConstraints(254, 262, 200, -1));

        txtPostalCode.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txtPostalCode.setName("txtPostalCode"); // NOI18N
        txtPostalCode.setNextFocusableComponent(txtCity);
        pnlPersoons.add(txtPostalCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(618, 101, 200, -1));

        txtCity.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txtCity.setName("txtCity"); // NOI18N
        txtCity.setNextFocusableComponent(txtEmail);
        pnlPersoons.add(txtCity, new org.netbeans.lib.awtextra.AbsoluteConstraints(618, 133, 200, -1));

        txtEmail.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txtEmail.setName("txtEmail"); // NOI18N
        pnlPersoons.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(618, 229, 200, -1));

        cmbCountries.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        cmbCountries.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        pnlPersoons.add(cmbCountries, new org.netbeans.lib.awtextra.AbsoluteConstraints(618, 165, 200, -1));

        lblBirthday.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblBirthday.setText("Geboortedatum");
        pnlPersoons.add(lblBirthday, new org.netbeans.lib.awtextra.AbsoluteConstraints(472, 200, -1, -1));

        spnBirthday.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        spnBirthday.setModel(new javax.swing.SpinnerDateModel());
        spnBirthday.setNextFocusableComponent(txtEmail);
        pnlPersoons.add(spnBirthday, new org.netbeans.lib.awtextra.AbsoluteConstraints(618, 197, -1, -1));

        btnRegister.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        btnRegister.setText("Registreren");
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });
        pnlPersoons.add(btnRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(689, 261, -1, -1));

        jLabel1.setText("Velden met een * zijn verplicht.");
        pnlPersoons.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 76, -1, -1));

        btnBack.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        btnBack.setText("Terug");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        pnlPersoons.add(btnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 20, -1, -1));

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
                .addComponent(pnlPersoons, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtUsernameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsernameFocusLost
        if (!txtUsername.getText().isEmpty()) {
            if (!checkUsername(txtUsername.getText())) {
                lblUsernameError.setVisible(true);
            }
        }
    }//GEN-LAST:event_txtUsernameFocusLost

    private void txtConfirmPasswordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtConfirmPasswordFocusLost
        if (!String.copyValueOf(txtPassword.getPassword()).equals(
                String.copyValueOf(txtConfirmPassword.getPassword()))) {
            error(CONFIRM_ERROR, null);
        }
    }//GEN-LAST:event_txtConfirmPasswordFocusLost

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        resetAllFields();
        setPersoonsEnabled(false);
        setAlgemeenEnabled(true);
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        String firstname = txtFirstname.getText(), infix = txtInfix.getText(),
                lastname = txtLastname.getText(), street = txtStreet.getText(),
                house = txtHouseNumber.getText(), extra = txtExtra.getText(),
                postalCode = txtPostalCode.getText(), city = txtCity.getText(),
                country = cmbCountries.getSelectedItem().toString(),
                email = txtEmail.getText();
        Date birthday = (Date) spnBirthday.getValue();
        if (checkInput(firstname, infix, lastname, street, house, postalCode,
                city, email)) {
            register(txtUsername.getText(),
                    String.copyValueOf(txtPassword.getPassword()),
                    firstname, infix, lastname, street, extra, city, country,
                    house, birthday);
        } else {
            JOptionPane.showMessageDialog(null, "Enkele velden zijn onjuist "
                    + "ingevoerd, deze hebben een rode kleur.", "Fout",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnRegisterActionPerformed

    private void btnAlgemeenNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlgemeenNextActionPerformed
        if (checkAlgemeen(txtUsername.getText(), txtPassword.getPassword(),
                txtConfirmPassword.getPassword())) {
            setAlgemeenEnabled(false);
            setPersoonsEnabled(true);
        } else {
            if (txtUsername.hasFocus()) {
                lblUsernameError.setVisible(false);
            } else if (txtPassword.hasFocus()) {
                lblPasswordError.setVisible(false);
            } else if (txtConfirmPassword.hasFocus()) {
                lblConfirmPassError.setVisible(false);
            }
        }
    }//GEN-LAST:event_btnAlgemeenNextActionPerformed

    private void txtPasswordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasswordFocusLost
        if (!checkPassword(String.copyValueOf(txtPassword.getPassword()))) {
            error(PASS_ERROR, String.copyValueOf(txtPassword.getPassword()));
        }
    }//GEN-LAST:event_txtPasswordFocusLost

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

        //Add focuslisteneres
        setFocusGained(txtFirstname);
        setFocusGained(txtInfix);
        setFocusGained(txtLastname);
        setFocusGained(txtStreet);
        setFocusGained(txtHouseNumber);
        setFocusGained(txtPostalCode);
        setFocusGained(txtCity);
        setFocusGained(txtEmail);

        //Add actionlisteners
        for (Component c : pnlAlgemeen.getComponents()) {
            if (c instanceof JTextField) {
                setAlgemeenAction((JTextField) c);
            }
        }
        for (Component c : pnlPersoons.getComponents()) {
            if (c instanceof JTextField) {
                setPersoonsAction((JTextField) c);
            }
        }

        //Add input listeners
        txtUsername.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                lblUsernameError.setVisible(false);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                lblUsernameError.setVisible(false);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                lblUsernameError.setVisible(false);
            }

        });
        txtPassword.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                lblPasswordError.setVisible(false);
                lblConfirmPassError.setVisible(false);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                lblPasswordError.setVisible(false);
                lblConfirmPassError.setVisible(false);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                lblPasswordError.setVisible(false);
                lblConfirmPassError.setVisible(false);
            }

        });
        txtConfirmPassword.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                lblConfirmPassError.setVisible(false);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                lblConfirmPassError.setVisible(false);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                lblConfirmPassError.setVisible(false);
            }

        });

        //Underline login label and add mouselistener
        Font font = lblLogin.getFont();
        Map attributes = font.getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        lblLogin.setFont(font.deriveFont(attributes));

        lblLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (lblLogin.isEnabled()) {
                    loginPressed = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (lblLogin.isEnabled()) {
                    if (loginPressed) {
                        loginPressed = false;
                        Main.setPanel(new Login());
                    }
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlgemeenNext;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnRegister;
    private javax.swing.JComboBox cmbCountries;
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
    private javax.swing.JLabel lblLogin;
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
