package com.example.redirectv2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.net.URI;

public class ListContainer extends AppCompatActivity  implements ItemFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_container);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
    public void onItemListInteraction(Contact c) {
        if(c.getContactType() == Contact.TYPE_PHONE)
        {
            Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + c.getContactInfo()));
            startActivity(i);
        } else if(c.getContactType() == Contact.TYPE_EMAIL)
        {
            String[] addresses = new String[1];
            addresses[0] = c.getContactInfo();
            Intent i = new Intent(Intent.ACTION_SENDTO);
            i.setData(Uri.parse("mailto:"));
            i.putExtra(Intent.EXTRA_EMAIL,addresses);
            if(i.resolveActivity(getPackageManager()) != null)
            {
                startActivity(i);
            }
        }
    }
}
