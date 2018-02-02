package com.example.rustybucket.addressbook422;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.stream.Collectors;

public class AB extends AppCompatActivity {

    private Toolbar toolbar;
    private AddressBookManager ABManager;
    private AddressBook currentAB;
    private List<Person> personList;
    private int abID;
    private int personID;
    private boolean alphaSort;
    private boolean zipSort;

    //TODO: implement exporting
    //TODO: implement search bar
    //TODO: implement link to settings page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ab);

        ABManager  = new AddressBookManager(AB.this);

        //Get abID from MainActivity (assuming abID is an int)
        Intent pastIntent = getIntent();
        abID = pastIntent.getIntExtra("abID", -1);
        alphaSort = pastIntent.getBooleanExtra("alphaSort", false);
        zipSort = pastIntent.getBooleanExtra("zipSort", false);

        /*
        TODO: check for each type of sorting and do if true
        TODO: after list is sorted, must change them both back to false(keeping it true will mess it up later)

        currentAB = ABManager.getAddressBook(abID);
        personList = currentAB.getPersonNames();
        ListAdapter entryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, personList);
        */

        currentAB = ABManager.getAddressBook(abID);
        setTitle(currentAB.getName());
        personList = currentAB.getEntries();
        List<String> personInfoList = personList.stream().map(person -> person.toString()).collect(Collectors.toList());

        ListAdapter entryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, personInfoList);

        ListView entryListView = findViewById(R.id.entryListView);
        entryListView.setAdapter(entryAdapter);

        entryListView.setOnItemClickListener(
                (parent, view, position, id) -> {
                    personID = (int) id;

                    Intent myIntent = new Intent(AB.this, entryAB.class);
                    myIntent.putExtra("abID", abID);
                    myIntent.putExtra("personID", personID);
                    myIntent.putExtra("update", true);
                    startActivity(myIntent);
                }
        );

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.newEntry) {
            Person person = currentAB.createPerson();
            personID = person.getId();        //takes no parameters, returns personID

            Intent myIntent = new Intent(AB.this, entryAB.class);
            myIntent.putExtra("abID", abID);
            myIntent.putExtra("personID", personID);
            startActivity(myIntent);
        }

//        if (id == R.id.alphaSort) {
//            currentAB.sortByLastName();
//            Intent myIntent = new Intent(AB.this, MainActivity.class);
//            setTitle("AddressBook");
//            startActivity(myIntent);
//            return true;
//        }
//
//        if (id == R.id.zipSort) {
//            currentAB.sortByZipCode();
//            Intent myIntent = new Intent(AB.this, MainActivity.class);
//            startActivity(myIntent);
//            return true;
//        }

        if (id == R.id.settingsAB) {
            Intent myIntent = new Intent(AB.this, settingsAB.class);
            myIntent.putExtra("abID", abID);                                             //-1 indicates new Ab creation needed
            setTitle("AddressBook");
            startActivity(myIntent);
        }

        if (id == R.id.deleteAB) {
            showalertDialogue();
            return true;
        }
        if (id == R.id.exportAB) {

            return true;
        }
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    //TODO: fix update button so changes are reflected immediately
    public void showalertDialogue(){
        final Dialog dialog=new Dialog(AB.this);
        dialog.setTitle("Alert Dialogue");
        dialog.setContentView(R.layout.alert_dialogue);
        TextView txtMessage=dialog.findViewById(R.id.txtmessage);
        txtMessage.setText("Are you sure you want to permanently delete this AB?");
        txtMessage.setTextColor(Color.parseColor("#FFFFFF"));
        Button bt=dialog.findViewById(R.id.btdone);
        bt.setOnClickListener(v -> {
            Intent myIntent = new Intent(AB.this, MainActivity.class);
            ABManager.getAddressBook(personID).deleteAddressBookFile();
            myIntent.putExtra("abid", abID);
            setTitle("AddressBook");
            startActivity(myIntent);
            dialog.dismiss();
        });
        dialog.show();
    }
}
