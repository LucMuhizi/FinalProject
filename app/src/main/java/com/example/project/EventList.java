/*
 * Author : Luc Muhizi
 */

package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

/**
 * This class is for handle the event_list Page, including the functions:
 * 1. display the search result saved in SQLite
 * 2. click on one item to initialize the fragment page for detailed information
 * 3. long click on one item to delete the record
 */
public class EventList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = "EventList";

    private boolean isTablet;
    public ArrayList<Events> eventList = new ArrayList<>(  );
    private SQLiteDatabase eventDB;
    private MyListAdapter myAdapter;
    private DetailsFragment newFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        //add a toolbart
        Toolbar myToolbar = (Toolbar) findViewById(R.id.ticketToolbar);
        setSupportActionBar(myToolbar);

        isTablet= findViewById(R.id.theFrameView) != null;

        //open the database and create a query;
        MyOpener myOpenHelper = new MyOpener(this);
        eventDB = myOpenHelper.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = eventDB.rawQuery("select * from " +
                MyOpener.TABLE_NAME + ";", null);

        //Convert column names to indices:
        int eventDBId = cursor.getColumnIndex(MyOpener.COL_ID);
        int eventDBName = cursor.getColumnIndex(MyOpener.COL_EventName);
        int eventDBDate = cursor.getColumnIndex(MyOpener.COL_StartDate);
        int eventDBMinP = cursor.getColumnIndex(MyOpener.COL_MIN_Price);
        int eventDBMaxP = cursor.getColumnIndex(MyOpener.COL_MAX_Price);
        int eventDBURL = cursor.getColumnIndex(MyOpener.COL_URL);
        int imgDBURL = cursor.getColumnIndex(MyOpener.COL_IMG);

        //add elements to Arraylist of events
        while (cursor.moveToNext()){
            int id = cursor.getInt(eventDBId);
            String eventName = cursor.getString(eventDBName);
            String eventDate = cursor.getString(eventDBDate);
            double eventMinP = cursor.getDouble(eventDBMinP);
            double eventMaxP = cursor.getDouble(eventDBMaxP);
            String eventURL = cursor.getString(eventDBURL);
            String imgURL = cursor.getString(imgDBURL);

            eventList.add(new Events(id,eventName,eventDate,eventMinP,
                    eventMaxP, eventURL, imgURL));
        }

        ListView myList = findViewById(R.id.theListView);
        myList.setAdapter( myAdapter = new MyListAdapter()); //display the ListView to Adapter

        //click an item to open a dialog to launch to fragment page
        myList.setOnItemClickListener( (listView, view, pos, id) -> {

            //setArguments for the Fragment
            newFragment=new DetailsFragment();
            Bundle args = new Bundle();
            args.putInt("position", pos);
            args.putSerializable("event",eventList.get(pos));

            //create a fragment transaction (if a tablet)
            if (isTablet)  {
                newFragment.setArguments(args);
                getSupportFragmentManager()
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.theFrameView, newFragment)
                        .commit();
            }else{
                //go to Empty if phone and open Fragment if a phone
                Intent goToFrag = new Intent(this ,EmptyActivity.class);
                goToFrag.putExtras(args);
                startActivity(goToFrag);
            }

        });

        //long click one of the event to delete it from ArrayList and DB
        myList.setOnItemLongClickListener( (listView, view, pos, id) -> {
        Events evtChose = eventList.get(pos);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Delete the event")

                //prompt message:
                .setMessage("Want to delete: " + eventList.get(pos).getEventName()+
                        "\n The selected row is: "+ pos +
                        "\n The database id is: "+id)

                //what the Yes button does:
                .setNegativeButton("No", (click1, arg) -> { })
                .setPositiveButton("Yes", (click2, arg) -> {
                    eventList.remove(pos);
                    myAdapter.notifyDataSetChanged();

                    eventDB.delete(MyOpener.TABLE_NAME,MyOpener.COL_ID+
                            "=?",new String[]{Long.toString(evtChose.getId())});

                //remove the fragment if delete
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().remove(newFragment).commit();

                }).create().show();
                return true;
        });

        //add a navigation drawer
        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, myToolbar, R.string.help,
                R.string.app_name);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView;
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    /**
     * Inflate toolbar
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

        switch (item.getItemId()) {
            case R.id.item0:

                Intent goToHome = new Intent(EventList.this, MainActivity.class);
                startActivity(goToHome);
                break;

            case R.id.item1:
                Intent goToTicket = new Intent(EventList.this, TicketSearchActivity.class);
                startActivity(goToTicket);
                break;

            case R.id.help_id:
                String title = getResources().getString(R.string.how_to_search);
                String line2 = getResources().getString(R.string.help);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(title).setMessage(line2 + " \n")
                        .setNegativeButton(R.string.close, (click, arg) -> {
                        })
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item0:
                Intent goToHome = new Intent(EventList.this, MainActivity.class);
                startActivity(goToHome);
                break;

            case R.id.item1:
                Intent goToTicket = new Intent(EventList.this, TicketSearchActivity.class);
                startActivity(goToTicket);
                break;

            case R.id.help_id:
                String title = getResources().getString(R.string.how_to_search);
                String line2 = getResources().getString(R.string.help);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(title)
                        .setMessage(line2 + " \n").setNegativeButton(R.string.close, (click, arg) -> {
                })
                        .create().show();
                break;
        }
        DrawerLayout drawerLayout = findViewById(R.id.main_drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * create ListAdapter with getCount(), getItem, getItemId, and getView methods
     * to implement the list view

     */
    private class MyListAdapter extends BaseAdapter {
        public int getCount() { Log.i(TAG, "total number of even"+eventList.size());
        return eventList.size();
        }
        public Object getItem(int position) { return eventList.get(position).eventName; }
        public long getItemId(int id) { return (long) id;}
        public View getView(int position, View old, ViewGroup parent){
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint("ViewHolder") View newView= inflater
                    .inflate(R.layout.row_of_list, parent, false);
            TextView tView = newView.findViewById(R.id.eventLine);
            tView.setText(getItem(position).toString());
            return newView;
        }

    }
}