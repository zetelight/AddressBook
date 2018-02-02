package com.example.rustybucket.addressbook422;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private AddressBookManager ABManager;
    private int abID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("AddressBook");

        ABManager = new AddressBookManager(MainActivity.this);

        ListAdapter exampleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ABManager.getAddressBookNames());

        ListView exampleListView = findViewById(R.id.exampleListView);
        exampleListView.setAdapter(exampleAdapter);
        exampleListView.setOnItemClickListener(
                (parent, view, position, id) -> {

                    abID = ABManager.getAddressBook((int) id).getToken();

                    Intent myIntent = new Intent(MainActivity.this, AB.class);
                    myIntent.putExtra("abID", abID);
                    myIntent.putExtra("alphaSort", false);
                    myIntent.putExtra("zipSort", false);
                    startActivity(myIntent);
                }
        );

        //custom app bar used by most activities
        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.newAB){
            Intent myIntent = new Intent(MainActivity.this, settingsAB.class);
            myIntent.putExtra("abID", -1);                                             //-1 indicates new Ab creation needed
            startActivity(myIntent);
        }
        if (id == R.id.importAB){
            startActivity(new Intent(this, importAB.class));
        }

        return super.onOptionsItemSelected(item);
    }


}
