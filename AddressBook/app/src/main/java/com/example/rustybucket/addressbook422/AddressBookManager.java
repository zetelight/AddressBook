package com.example.rustybucket.addressbook422;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam Chen on 1/21/18.
 * An AddressBookManager class. It is used to manager address books
 */

public class AddressBookManager {
    Context context;
    AddressBook addressBookFile, midCreationAddressBook;
    List<AddressBook> addressBookManager = new ArrayList<>();


    public AddressBookManager(Context c) {
        this.context = c;
        //Used to only find parent directory
        File[] allFiles = c.getFilesDir().listFiles();
        for (File file : allFiles) {
            if (file.getName().endsWith(".tsv")) {
                addressBookManager.add(new AddressBook(context, file.getName()));
            }
        }
        updateAllToken();
    }

    public List<AddressBook> getAddressBookManager() {
        return addressBookManager;
    }

    public AddressBook createAddressBook(String name) {
        //Returns address book reference that will be created
        File file = new File(context.getFilesDir(), name + ".tsv");
        if (file.exists()) {
            //TODO throw some error
            return null; //ERROR
        } else {
            addressBookFile = new AddressBook(context, name);
            addressBookManager.add(addressBookFile);
            updateAllToken();
            midCreationAddressBook = addressBookFile;
            return addressBookFile;
        }
    }

    /*public void addAddressBook(AddressBook book) {
        book.setToken(addressBookManager.size());
       this.addressBookManager.add(book);
    }*/

    public AddressBook getAddressBook(int token) {
        //updateAllToken();
        if (token == -1) {
            return midCreationAddressBook;
        } else {
            return addressBookManager.get(token);
        }
    }

    private void updateAllToken() {
        int token = 0;
        for (AddressBook ab : addressBookManager) {
            ab.setToken(token);
            token++;
        }
    }

    public String[] getAddressBookNames(){
        int size = addressBookManager.size();
        String[] info = new String[size];
        int index = 0;
        for (AddressBook ab : addressBookManager) {
            info[index] = ab.getName();
            index++;
        }
        return info;
    }
}
