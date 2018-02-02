package com.example.rustybucket.addressbook422;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class entryAB extends AppCompatActivity {

    String[] personInfo;
    EditText fNameText;
    EditText lNameText;
    EditText add1Text;
    EditText add2Text;
    EditText cityText;
    EditText stateText;
    EditText zipText;
    EditText phoneNumText;
    EditText emailText;

    private AddressBookManager ABManager;

    private AddressBook currentAB;
    private Person currentPerson;
    private Toolbar toolbar;
    private int abID;
    private int personID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_ab);
        ABManager = new AddressBookManager(entryAB.this);

        //Get abID and entryID from AB (assuming they are both ints)
        Intent pastIntent = getIntent();
        abID = pastIntent.getIntExtra("abID", -1);
        personID = pastIntent.getIntExtra("personID", -1);

        currentAB = ABManager.getAddressBook(abID);
        currentPerson = currentAB.getPerson(personID);

        personInfo = currentPerson.getPersonFields();

        fNameText = findViewById(R.id.firstName);
        if (personInfo[0] != "") {fNameText.setText(personInfo[0]);}
        lNameText = findViewById(R.id.lastName);
        if (personInfo[1] != "") {lNameText.setText(personInfo[1]);}
        add1Text = findViewById(R.id.address1);
        if (personInfo[2] != "") {add1Text.setText(personInfo[2]);}
        add2Text = findViewById(R.id.address2);
        if (personInfo[3] != "") {add2Text.setText(personInfo[3]);}
        cityText = findViewById(R.id.city);
        if (personInfo[4] != "") {cityText.setText(personInfo[4]);}
        stateText = findViewById(R.id.state);
        if (personInfo[5] != "") {stateText.setText(personInfo[5]);}
        zipText = findViewById(R.id.zip);
        if (personInfo[6] != "") {zipText.setText(personInfo[6]);}
        phoneNumText = findViewById(R.id.phoneNum);
        if (personInfo[7] != "") {phoneNumText.setText(personInfo[7]);}
        emailText = findViewById(R.id.email);
        if (personInfo[8] != "") {emailText.setText(personInfo[8]);}

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_entryab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(entryAB.this, AB.class);
        myIntent.putExtra("abID", abID);

        int id = item.getItemId();

        if (id == R.id.deleteEntry) {
            if (currentPerson.getId() == -1) {
                this.onBackPressed();
            } else {
                showalertDialogue();
            }
            return true;
        }

        if (id == android.R.id.home) {
            this.onBackPressed();
            return true;
        }

        if (id == R.id.updateEntry) {
            //have to check if one of the names exist
            //have to check if one of the other fields exist

            String fName = fNameText.getText().toString();
            String lName = lNameText.getText().toString();
            String add1 = add1Text.getText().toString();
            String add2 = add2Text.getText().toString();
            String city = cityText.getText().toString();
            String state = stateText.getText().toString();
            String zip = zipText.getText().toString();
            String phoneNum = phoneNumText.getText().toString();
            String email = emailText.getText().toString();

            if (fName.isEmpty() && lName.isEmpty()) {
                Toast toast=Toast.makeText(getApplicationContext(),"Please fill in a first name or last name.",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.show();
            }

            else if (add1.isEmpty() && add2.isEmpty() && city.isEmpty() && state.isEmpty() && zip.isEmpty() && phoneNum.isEmpty() && email.isEmpty()) {
                Toast toast=Toast.makeText(getApplicationContext(),"Please fill in one field other than first name or last name.",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.show();
            }

            else {
                //Regex reference: https://stackoverflow.com/questions/14206768/how-to-check-if-a-string-is-numeric, https://howtodoinjava.com/regex/java-regex-validate-us-postal-zip-codes/
                if (!(zip.matches("^[0-9]{5}(?:-[0-9]{4})?$") || (zip.matches("[-+]?\\d*\\.?\\d+") && zip.length() == 5))) {
                    Toast toast=Toast.makeText(getApplicationContext(),"Note: Zip code does not meet valid American US Standards",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.show();
                }

                currentPerson.setFirstName(fName);
                currentPerson.setLastName(lName);
                currentPerson.setAddress1(add1);
                currentPerson.setAddress2(add2);
                currentPerson.setCity(city);
                currentPerson.setState(state);
                currentPerson.setZipCode(zip);
                currentPerson.setPhoneNumber(phoneNum);
                currentPerson.setEmail(email);
                if (getIntent().getBooleanExtra("update", false)) {
                    currentAB.deletePerson(personID);
                }

                currentAB.addPerson(currentPerson);
                currentAB.sortByName();
                startActivity(myIntent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void showalertDialogue(){
        final Dialog dialog=new Dialog(entryAB.this);
        dialog.setTitle("Alert Dialogue");
        dialog.setContentView(R.layout.alert_dialogue);
        TextView txtMessage=dialog.findViewById(R.id.txtmessage);
        txtMessage.setText("Delete this item?");
        txtMessage.setTextColor(Color.parseColor("#FFFFFF"));
        Button bt=dialog.findViewById(R.id.btdone);
        bt.setOnClickListener(v -> {
            Intent myIntent = new Intent(entryAB.this, AB.class);
            currentAB.deletePerson(personID);
            myIntent.putExtra("abID", abID);

            startActivity(myIntent);
            dialog.dismiss();
        });
        dialog.show();
    }
}
