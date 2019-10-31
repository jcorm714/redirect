package com.example.redirectv2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SettingsContainer extends AppCompatActivity implements SettingsFragment.OnFragmentInteractionListener {
    private boolean debug = false;
    public static final String IS_DEBUG = "key for extra in Settings Container";
    public int result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_container);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.result = Activity.RESULT_CANCELED;

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onFragmentInteraction(boolean debugging) {
        debug = debugging;
        this.result = Activity.RESULT_OK;
    }

    @Override
    public void finish() {
        Intent i = new Intent();
        i.putExtra(IS_DEBUG, this.debug);
        setResult(Activity.RESULT_OK, i );


        super.finish();
    }
}
