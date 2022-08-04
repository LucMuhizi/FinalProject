/*
 * Author : Luc Muhizi
 */
package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

/**
 * Ticket Master main class with user tool bar, navigation bar, buttons
 * inheritance AppCompatActivity, and implements NavigationView.OnNavigationItemSelectedListener
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    /**
     * Create bundle type saved instanceState
     * @param savedInstanceState  saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginTicketBtn = findViewById(R.id.buttonTicket);
        loginTicketBtn.setOnClickListener(bt -> {
            Intent goToTicket = new Intent(MainActivity.this, TicketSearchActivity.class);
            startActivity(goToTicket);
        });

        //add a toolbart
        Toolbar myToolbar = (Toolbar)findViewById(R.id.mainToolbar);
        setSupportActionBar(myToolbar);

        //add a navigation view
        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, myToolbar, R.string.help, R.string.app_name);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView;
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    /**
     * handle for the navigation drawer buttons
     * @param menu menu created
     * @return true true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_toolbar_menu, menu);
        return true;
    }

    /**
     * handle toolbar buttons
     * @param item each icon
     * @return true true
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.item1:
                Intent goToTicket = new Intent(MainActivity.this, TicketSearchActivity.class);
                startActivity(goToTicket);
                break;

            case R.id.help_id:
                String title =  getResources().getString(R.string.how_to_search);
                String line2 = getResources().getString(R.string.help);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(title).setMessage( line2 + " \n").setNegativeButton(R.string.close, (click, arg) -> { })
                        .create().show();
                break;
        }
        return true;
    }

    /**
     * handle for the navigation drawer buttons
     * @param item icons
     * @return true true
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String message = null;

        switch(item.getItemId())
        {
            case R.id.item1:
                Intent goToTicket = new Intent(MainActivity.this, TicketSearchActivity.class);
                startActivity(goToTicket);
                break;

            case R.id.help_id:
                String title =  getResources().getString(R.string.how_to_search);
                String line2 = getResources().getString(R.string.help);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(title).setMessage(line2 + " \n")
                        .setNegativeButton(R.string.close, (click, arg) -> { })
                        .create().show();
                break;
        }
        DrawerLayout drawerLayout = findViewById(R.id.main_drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}