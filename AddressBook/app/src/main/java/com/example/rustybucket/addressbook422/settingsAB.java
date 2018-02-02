package com.example.rustybucket.addressbook422;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class settingsAB extends AppCompatActivity {

    private Toolbar toolbar;
    private AddressBookManager ABManager;
    private AddressBook currentAB;
    private int abID;
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_ab);
        ABManager = new AddressBookManager(settingsAB.this);

        Intent pastIntent = getIntent();
        abID = pastIntent.getIntExtra("abID", -1);

        if (abID == -1) {       //new AB
            editText = findViewById(R.id.ABText);
            editText.setText("");

        } else {                //editing existing AB
            currentAB = ABManager.getAddressBook(abID);
            editText=findViewById(R.id.ABText);
            editText.setText(currentAB.getName());
        }

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settingsab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.AB){
            String abName = editText.getText().toString();
            //Checks if string is alphanumeric
            if (abName.matches("[A-Za-z0-9]+") || abName.contains(" ")) {
                if (abID == -1) {                       //this means we are creating a new AB
                    currentAB = ABManager.createAddressBook(editText.getText().toString());
                    abID = currentAB.getToken();
                } else {                                      //this means we are updating an existing AB
                    currentAB.setName(editText.getText().toString());
                }
            } else {
                Toast toast=Toast.makeText(getApplicationContext(),"Please enter a title that only consists of letters, numbers, or spaces  ",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.show();
            }
            Intent myIntent = new Intent(settingsAB.this, MainActivity.class);
            myIntent.putExtra("abID", abID);                                             //-1 indicates new Ab creation needed
            startActivity(myIntent);
        }
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
