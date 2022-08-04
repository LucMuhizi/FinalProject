/*
 * Author : Luc Muhizi
 */

package com.example.project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * A container to hold the fragment
 */
public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        Bundle args = getIntent().getExtras();

        DetailsFragment newFragment = new DetailsFragment();
        newFragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.theFrameView, newFragment )
                .commit();
    }
}