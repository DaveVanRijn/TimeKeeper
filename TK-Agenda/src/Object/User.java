/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Object;

import Exception.CharNotSupportedException;
import Main.Main;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author Dave van Rijn, Student 500714558, Klas IS202
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username, password, firstname, infix, lastname, address,
            addressExtra, city, country, postalCode, email;
    private int houseNumber;
    private Date birthday;
    private final Agenda agenda;

    public User(String username, String password) throws CharNotSupportedException {
        this.username = username;
        this.password = Main.encrypt(password);
        agenda = new Agenda();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getDecryptedPassword() throws CharNotSupportedException {
        return Main.decrypt(password);
    }

    public void setPassword(String password) throws CharNotSupportedException {
        this.password = Main.encrypt(password);
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getInfix() {
        return infix;
    }

    public void setInfix(String infix) {
        this.infix = infix;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressExtra() {
        return addressExtra;
    }

    public void setAddressExtra(String addressExtra) {
        this.addressExtra = addressExtra;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getEmail() {
        return email;
    }

    public boolean setEmail(String email) {
        Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        boolean match = p.matcher(email).matches();
        if(match){
            this.email = email;
        }
        return match;
    }

    public void addMeeting(Meeting m) {
        agenda.addMeeting(m);
    }

    public int nextMeetingId() {
        int id = 0;
        List<Integer> ids = new ArrayList<>();
        for (List<Meeting> meetings : agenda.getMeetings().values()) {
            for (Meeting m : meetings) {
                ids.add(m.getId());
            }
        }

        while (ids.contains(id)) {
            id++;
        }
        return id;
    }
}
