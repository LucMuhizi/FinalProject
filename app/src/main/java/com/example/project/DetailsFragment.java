/*
 * Author : Luc Muhizi
 */

package com.example.project;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.io.FileNotFoundException;


/**
 * the class shows the details of the events
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {

    //private static String imgURL;
    int position;
    MyOpener myOpenHelper;
    SQLiteDatabase eventDB;

    //Bundle args;
    TextView showName;
    TextView showRange;
    TextView showURL;
    TextView showDate;
    ImageView showImg;
    private Events event;

    // Required empty public constructor
    public DetailsFragment() {
    }

    /**
     * Set required variables for the functions
     * @param savedInstanceState saved Instance
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getArguments from Bundle of ChartRoom
        assert getArguments() != null;
        position = getArguments().getInt("position", 0);
        event = (Events) getArguments().getSerializable("event");

        myOpenHelper = new MyOpener(getContext());
        eventDB = myOpenHelper.getWritableDatabase();
    }

    /**
     * sets the layout for fragment
     * @param inflater
     * @param parent
     * @param savedInstanceState
     * @return detailView
     */
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup parent,
                             Bundle savedInstanceState) {


        // Inflate the xml file for the fragment
        return inflater.inflate(R.layout.fragment_details, parent, false);
    }

    /**
     * add event data to the fragment layout
     * @param view
     * @param savedInstanceState
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        //getArgument from CharRoom to Fragment
        Button button = view.findViewById(R.id.savetofav);
        button.setOnClickListener(view1 -> {
            String thisName = event.getEventName();
            String thisData = event.getStartDate();
           // Cursor query = eventDB.rawQuery("count * from "+ MyOpener.TABLE_NAME+ " where " +
            //        MyOpener.COL_EventName+ "=" + thisName +" and "+
            //        MyOpener.COL_StartDate+"="+thisData+";" , null);
            //if(query.getCount()==0){
            ContentValues newRow = new ContentValues();
            newRow.put(MyOpener.COL_EventName, event.getEventName());
            newRow.put(MyOpener.COL_URL, event.getTicketMasterURL());
            newRow.put(MyOpener.COL_StartDate, event.getStartDate());
            newRow.put(MyOpener.COL_MIN_Price, event.getMinPrice());
            newRow.put(MyOpener.COL_MAX_Price, event.getMaxPrice());
            newRow.put(MyOpener.COL_IMG, event.getImgURL());

            long id=eventDB.insertWithOnConflict(MyOpener.FAVORITE_TABLE_NAME,
                    null, newRow,SQLiteDatabase.CONFLICT_IGNORE);
            //}

            AlertDialog.Builder builder = new AlertDialog.Builder(DetailsFragment.super.getActivity());
            AlertDialog dialog = builder.setMessage(R.string.saved_favorites)
                    .setPositiveButton("OK", (d, w) -> {  /* nothing */})
                    .create();
            dialog.show();
        });

        showName = view.findViewById(R.id.textView);
        showName.setText(getResources().getString(R.string.fragTagName)+" "
                + event.getEventName());

        showImg = view.findViewById(R.id.imageView);
        showImg.setImageBitmap(getBitmap(event.getImgURL()));

        showRange =view.findViewById(R.id.textView2);
        showRange.setText(getResources().getString(R.string.fragTagPrice) +" CA$"+
                event.getMinPrice() + " ~ CA$"+event.getMaxPrice());

        showDate = view.findViewById(R.id.textView3);
        showDate.setText(getResources().getString(R.string.fragTagDate) +" "
                + event.getStartDate());

        showURL = view.findViewById(R.id.textView4);
        showURL.setText(getResources().getString(R.string.fragTagLike)+" "
                + event.getTicketMasterURL());

        }

    /**
     * open a file path and return a bitmap image
     * @param url
     * @return Bitmap a picture to show
     */
    public Bitmap getBitmap(String url) {

        try {
            return BitmapFactory.decodeStream(getActivity().openFileInput(url));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}