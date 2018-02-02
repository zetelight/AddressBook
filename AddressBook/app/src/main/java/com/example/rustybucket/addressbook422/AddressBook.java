package com.example.rustybucket.addressbook422;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Adam Chen on 1/21/18.
 * An AddressBook class which stores a list of person and manipulate each person's information
 */

public class AddressBook {
    /*
    * token: int, use to identify address book
    * */
    private String name;
    private List<Person> addressbook;
    private int token;
    private boolean isDeleted;
    private File addressBookFile;
    private Context context;
    private Person midCreationPerson = new Person();


    /* Constructor */
    public AddressBook(Context c, String name) {
        //Initializes file
        this.context = c;
        this.addressBookFile = name.endsWith(".tsv") ? new File(c.getFilesDir(), name) : new File(c.getFilesDir(), name +".tsv");
        this.name = name.endsWith(".tsv") ? name.substring(0, name.length()-4) : name;
        this.addressbook = new ArrayList<>();
        try {
            Reader inputStreamReader = new InputStreamReader(new FileInputStream(addressBookFile));
            int data = inputStreamReader.read();
            while (data != -1) {
                if (data == '\n') {
                    this.addressbook = getEntries();
                }
                data = inputStreamReader.read();
            }
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        this.isDeleted = false;
        try {
            addressBookFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Person createPerson() {
        //Returns person reference that will be created
        Person person = new Person();
        midCreationPerson = person;
        return person;
    }


    public String toString() {
        //Returns address book's file's text file as a string
        StringBuilder stringBuilder = new StringBuilder();
        Reader inputStreamReader;
        try {
            inputStreamReader = new InputStreamReader(new FileInputStream(addressBookFile));
            int data = inputStreamReader.read();
            while(data != -1) {
                stringBuilder.append((char) data);
                data = inputStreamReader.read();
            }
            inputStreamReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public AddressBook(List<Person> book) {
        this.addressbook = new ArrayList<>(book);
    }

    /* Getter methods */
    public List<Person> getAddresses() {
        return addressbook;
    }

    public String getName() {
        return name;
    }

    public List<Person> getEntries() {
        List<Person> entries = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        Reader inputStreamReader;
        try{
            inputStreamReader = new InputStreamReader(new FileInputStream(addressBookFile));
            int data = inputStreamReader.read();
            int ctr = 0;             //Determines what field this is being added in
            boolean changed = false; //Determines if field is empty but still contains a new line character
            String[] attributes = new String[new Person().getMaxFields()];
            while (data != -1) {
                switch (data){
                    case '\n':
                        if (changed) {
                            entries.add(new Person(attributes));
                        }
                        changed = false;
                        ctr = 0;
                        data = inputStreamReader.read();
                        break;
                    case '\t':
                        if (ctr > new Person().getMaxFields()-1) {
                            ctr = 0;
                            //TODO THROW ERROR
                        } else {
                            attributes[ctr++] = stringBuilder.toString();
                            stringBuilder = new StringBuilder();
                        }
                        data = inputStreamReader.read();
                        break;
                    default:
                        changed = true;
                        stringBuilder.append((char) data);
                        data = inputStreamReader.read();
                        break;
                }
            }
            inputStreamReader.close();

            if (ctr > new Person().getMaxFields()) {
                //TODO throw error like above
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return entries;

    }

    public int getToken() {
        return token;
    }

    public Person getPerson(int id) {
        if (id == -1) {
            return midCreationPerson;
        } else {
            updateAllID();
            return addressbook.get(id);
        }
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    /* Setter methods */
    public void setAddressbook(List<Person> addressbook) {
        this.addressbook = addressbook;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void setName(String name) {
        File newFile = new File(addressBookFile.getParent(), name+".tsv");
        addressBookFile.renameTo(newFile);
        updateAllID();
    }

    /* Delete method*/
    public void deletePerson(int id) {

        addressbook.remove(id); // Person's id corresponds to index of address book.

        //Read file into list of strings reference: http://www.java67.com/2016/07/how-to-read-text-file-into-arraylist-in-java.html
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(addressBookFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<String> arr = new ArrayList<>();
        String line;
        try {
            line = reader.readLine();
            while (line != null) {
                arr.add(line);
                line = reader.readLine();
            }

            reader.close();

            arr.remove(id);
            //Write back to file reference: https://stackoverflow.com/questions/6548157/how-to-write-an-arraylist-of-strings-into-a-text-file
            FileWriter writer = new FileWriter(addressBookFile);
            for(String str: arr) {
                writer.write(str);
                writer.write('\n');
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.updateAllID();
    }

    /* Add method */
    public void addPerson(Person p) {
        p.setId(addressbook.size());
        addPerson(p.getPersonFields());
    }

    public void addPerson(String[] personData) {
        //Puts the string array as a tsv into the file
        try {
            FileOutputStream outputStream = new FileOutputStream(addressBookFile, true);
            for (String field : personData) {
                outputStream.write(field.getBytes());
                outputStream.write("\t".getBytes());
            }
            outputStream.write("\n".getBytes());
            outputStream.close();
            //TODO placeholder before we get more fields
            Person person;
            if (personData.length < midCreationPerson.getMaxFields()) {
                String[] newPersonData= new String[midCreationPerson.getMaxFields()];
                for (int i = 0; i < midCreationPerson.getMaxFields(); i++) {
                    newPersonData[i] = personData[i];
                }
                person = new Person(newPersonData);
            } else {
                person = new Person(personData);
            }
            person.setId(addressbook.size());
            addressbook.add(person);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAddressBookFile() {
        addressBookFile.delete();
        updateAllID();
    }

    /*
    * Update all ID in the address book
    * note: It is not a good way but this project is not big, the solution is acceptable.
    * Complexity: O(n)
    * */
    public void updateAllID() {
        this.addressbook = getEntries();
        int id = 0;
        for (Person p : addressbook) {
            p.setId(id);
            id++;
        }
    }

    public String[] getPersonNames(){
        int size = addressbook.size();
        String[] info = new String[size];
        int index = 0;
        for (Person p : addressbook) {
            String fullName = p.getFirstName() + " " + p.getLastName();
            info[index] = fullName;
            index++;
        }
        return info;
    }

    public void updateAddressBook() {
        setAddressbook(getEntries());
    }

    /*
    * Sort methods, based on last name, id or zip code
    * Complexity: O(nlgn + n)
    * return: the copied arraylist
    * */
    public List<Person> sortByName() {
        List<Person> temp = new ArrayList<>(addressbook);
        Collections.sort(temp, new sortByName());
        try {
            PrintWriter writer = new PrintWriter(addressBookFile);
            writer.print("");
            writer.close();
            for (Person p: temp) {
                addPerson(p);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.updateAllID();
        return temp;
    }

    public List<Person> sortByZipCode() {
        List<Person> temp = new ArrayList<>(addressbook);
        Collections.sort(temp, new sortByZipCode());
        try {
            PrintWriter writer = new PrintWriter(addressBookFile);
            writer.print("");
            writer.close();
            for (Person p: temp) {
                addPerson(p);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.updateAllID();
        return temp;
    }

    public List<Person> sortById() {
        List<Person> temp = new ArrayList<>(addressbook);
        Collections.sort(temp, new sortById());
        // this.updateAllID();
        return temp;
    }

    /*
    * Search methods, based on first/last/zip code/phone number/id
    * If find it, return id of the target Person. Otherwise, return Integer.MIN_VALUE
    * Integer.MIN_VALUE will be used as invalid id, which means the target cannot be found in the address book
    * */
    public int findId(int id) {
        try {
            return addressbook.
                    stream().
                    filter(p -> p.getId() == id).
                    findFirst().
                    get().
                    getId();
        } catch (Exception e) {
            e.printStackTrace();
            return Integer.MIN_VALUE;
        }
    }

    public int findFirstName(String first) {
        try {
            return addressbook.
                    stream().
                    filter(p -> p.getFirstName() == first).
                    findFirst().
                    get().
                    getId();
        } catch (Exception e) {
            e.printStackTrace();
            return Integer.MIN_VALUE;
        }
    }

    public int findLastName(String last) {
        try {
            return addressbook.
                    stream().
                    filter(p -> p.getLastName() == last).
                    findFirst().
                    get().
                    getId();
        } catch (Exception e) {
            e.printStackTrace();
            return Integer.MIN_VALUE;
        }
    }

    public int findPhoneNumber(String phone) {
        try {
            return addressbook.
                    stream().
                    filter(p -> p.getPhoneNumber() == phone).
                    findFirst().
                    get().
                    getId();
        } catch (Exception e) {
            e.printStackTrace();
            return Integer.MIN_VALUE;
        }
    }

    public int findZipCode(String zip) {
        try {
            return addressbook.
                    stream().
                    filter(p -> p.getZipCode() == zip).
                    findFirst().
                    get().
                    getId();
        } catch (Exception e) {
            e.printStackTrace();
            return Integer.MIN_VALUE;
        }
    }


}

/* Comparator based on Last Name*/
class sortByName implements Comparator<Person> {
    public int compare(Person p1, Person p2) {
        // The default order of Collections.sort is ascending
        // if return positive, it means greater than.
        // if p1.lastName is empty, p1 > p2, return 1
        // if p2.lastName is empty, p1 < p2, return -1
        if (p1.getLastName().trim().length() == 0 && p1.getFirstName().trim().length() == 0) {
            return 1;
        } else if (p2.getLastName().trim().length() == 0 && p2.getFirstName().trim().length() == 0) {
            return -1;
        } else if (p1.getLastName().trim().length() == 0) {
            return 1;
        } else if (p2.getLastName().trim().length() == 0) {
            return -1;
        } else if (p1.getFirstName().trim().length() == 0) {
            return 1;
        } else if (p2.getFirstName().trim().length() == 0) {
            return -1;
        }
        if (p1.getLastName().compareToIgnoreCase(p2.getLastName()) == 0) {
            return p1.getFirstName().compareToIgnoreCase(p2.getFirstName());
        } else {
            return p1.getLastName().compareToIgnoreCase(p2.getLastName());
        }
    }
}

/* Comparator based on Zip Code*/
class sortByZipCode implements Comparator<Person> {
    public int compare(Person p1, Person p2) {
        if (p1.getZipCode().trim().length() == 0 || p1.getZipCode() == null) return 1;
        if (p2.getZipCode().trim().length() == 0 || p1.getZipCode() == null) return -1;
        return p1.getZipCode().compareToIgnoreCase(p2.getZipCode());
    }
}

/* Comparator based on id*/
class sortById implements Comparator<Person> {
    public int compare(Person p1, Person p2) {
        return p1.getId() - p2.getId();
    }
}

