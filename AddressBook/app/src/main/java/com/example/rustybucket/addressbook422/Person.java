package com.example.rustybucket.addressbook422;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by Adam Chen on 1/21/18.
 * A Person class which is used to represent a personal information
 * including first&&last name, address, zip code, phone number and id which is used to indicate the order in an address book
 */

public class Person {
    private String firstName;
    private String lastName;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String email;
    private String zipCode;
    private int id = -1;
    private String phoneNumber;
    private final int MAX_PERSON_FIELDS = 9;


    /* Constructor */
    public Person() {
        this("", "", "", "", "", "", "", "", "");
    }


    public Person(String firstName, String lastName, String address1, String address2, String city, String state, String zipCode, String phoneNumber, String email) {
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.address1 = address1.trim();
        this.address2 = address2.trim();
        this.city = city.trim();
        this.state = state.trim();
        this.zipCode = zipCode.trim();
        this.phoneNumber = phoneNumber.trim();
        this.email = email.trim();
    }

    public Person(String[] personInfo) {
        if (personInfo.length > MAX_PERSON_FIELDS) {
            //TODO Throw some error e
        } else {
            try {
                this.firstName = personInfo[0].trim();
                this.lastName = personInfo[1].trim();
                this.address1 = personInfo[2].trim();
                this.address2 = personInfo[3].trim();
                this.city = personInfo[4].trim();
                this.state = personInfo[5].trim();
                this.zipCode = personInfo[6].trim();
                this.phoneNumber = personInfo[7].trim();
                this.email = personInfo[8].trim();
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    /* Getter methods */
    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getAddress() {
        return this.address1 + " " + this.address2 + " " + this.city + " " + this.state + " " + this.zipCode;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public int getId() {
        return this.id;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getEmail() {return this.email; }

    public int getMaxFields() {
        return MAX_PERSON_FIELDS;
    }
    /* Setter methods */
    public void setFirstName(String firstName) {
        this.firstName = firstName.trim();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.trim();
    }

    public void setAddress1(String address1) {
        this.address1 = address1.trim();
    }

    public void setAddress2(String address2) {
        this.address2 = address2.trim();
    }

    public void setCity(String city) {this.city = city.trim();}

    public void setState(String state) {this.state = state.trim();}

    public void setEmail(String email) {
        this.email = email.trim();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber.trim();
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode.trim();
    }

    /*
    * get the String array representation of single person
    * Return: a String array with personal infos.
    */
    public String[] getPersonInfo() {
        List<String> info = new ArrayList<>();
        info.add(firstName);
        info.add(lastName);
        info.add(address1 + " " + address2 + " " + city + " " + state + " ");
        // we expect zipcode can be parsed as 97401-2023 with a dash
        if (this.zipCode.length() == 9 && !this.zipCode.contains("-")) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.zipCode.substring(0, 5));
            sb.append("-");
            sb.append(this.zipCode.substring(5, this.zipCode.length()));
            info.add(sb.toString());
        } else {
            info.add(this.zipCode);
        }
        info.add(this.phoneNumber);
        info.add(this.email);
        return info.toArray(new String[info.size()]);
    }

    public String[] getPersonFields() {
        List<String> info = new ArrayList<>();
        info.add(firstName);
        info.add(lastName);
        info.add(address1);
        info.add(address2);
        info.add(city);
        info.add(state);
        // we expect zipcode can be parsed as 97401-2023 with a dash
        if (this.zipCode.length() == 9 && !this.zipCode.contains("-")) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.zipCode.substring(0, 5));
            sb.append("-");
            sb.append(this.zipCode.substring(5, this.zipCode.length()));
            info.add(sb.toString());
        } else {
            info.add(this.zipCode);
        }
        info.add(this.phoneNumber);
        info.add(this.email);
        return info.toArray(new String[info.size()]);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(lastName);
        stringBuilder.append(", ");
        stringBuilder.append(firstName);
        return stringBuilder.toString();
    }
}
