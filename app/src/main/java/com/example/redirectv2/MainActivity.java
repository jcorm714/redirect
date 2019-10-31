package com.example.redirectv2;

import android.Manifest;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import org.opencv.android.CameraBridgeViewBase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LiveCamera.OnFragmentInteractionListener{

    private NavController navController;
    public static final int SETTINGS_REQUEST = 7;
    private static boolean cameraPressed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
//        Contact contact1 = new Contact(Contact.TYPE_EMAIL, "jcorm714@gmail.com");
//        Contact contact2 = new Contact(Contact.TYPE_PHONE, "8608063998");
//        Contact contact3 = new Contact(Contact.TYPE_EMAIL, "helloworld@gmail.com");
//        Contact contact4 = new Contact(Contact.TYPE_PHONE, "8608675309");
//        DatabaseHandler handler = new DatabaseHandler(this);
//        handler.clearTable();
//        handler.addContact(contact1);
//        handler.addContact(contact2);
//        handler.addContact(contact3);
//        handler.addContact(contact4);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraPressed =true;
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }






    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if(requestCode == 0)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED){



            } else
            {
                finish();
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.menu_set)
        {
            Intent i = new Intent(this, SettingsContainer.class);
            startActivityForResult(i, SETTINGS_REQUEST);
            return true;
        }

        if(id == R.id.menu_itemList)
        {
            Intent i = new Intent(this, ListContainer.class);
            startActivity(i);
            return true;
        }
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == SETTINGS_REQUEST && resultCode == Activity.RESULT_OK)
        {
            if(data.hasExtra(SettingsContainer.IS_DEBUG))
            {
                LiveCamera.Debuging = data.getBooleanExtra(SettingsContainer.IS_DEBUG,false);

            }
        }



        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLiveCameraInteraction(CameraBridgeViewBase.CvCameraViewFrame frame) {
        if(cameraPressed)
        {
            //new ProcessImage().execute(frame);

            ArrayList<Bitmap> detectedText = TextDetector.DetectText(frame.gray(),frame.rgba());
            TessOCR ocr = new TessOCR(getApplicationContext());
            ArrayList<String> parsedText = new ArrayList<>();
            Iterator<Bitmap> iterator = detectedText.iterator();
            while(iterator.hasNext())
            {
                Bitmap temp = iterator.next();
                String result = ocr.getOCRResult(temp);
                parsedText.add((result.length() !=0)? result : "");
            }
//
//            String emailTest = "test1email@email.net";
//            String phoneTest = "8603795699";
//
//            parsedText.add(emailTest);
//            parsedText.add(phoneTest);

            TextParser parser = TextParser.getInstance();
            Iterator<String> iterator2 = parsedText.iterator();
            ArrayList<Contact> contacts = new ArrayList<>();


            while(iterator2.hasNext())
            {
                String text = iterator2.next();
                Log.d("Parsed Text" , "Text result"  + text);
                List<Contact> temp = parser.findMatches(text);
                for(int i = 0; i < temp.size(); i++)
                {
                    contacts.add(temp.get(i));
                }

            }
//            Contact phoneTest = new Contact(Contact.TYPE_PHONE,"8603795689");
//            Contact emailTest = new Contact(Contact.TYPE_EMAIL, "testemail@email.net");
//            contacts.add(phoneTest);
//            contacts.add(emailTest);

            if(contacts.size() > 0) {
                //Toast.makeText(getApplicationContext(), "Contacts Added", Toast.LENGTH_LONG).show();
                parser.SaveContacts(this, contacts);
            }
            cameraPressed = !cameraPressed;
        }
    }



    private class ProcessImage extends AsyncTask<CameraBridgeViewBase.CvCameraViewFrame, Void, ArrayList<Bitmap>>
    {


        @Override
        protected ArrayList<Bitmap> doInBackground(CameraBridgeViewBase.CvCameraViewFrame... cvCameraViewFrames) {

            CameraBridgeViewBase.CvCameraViewFrame frame = cvCameraViewFrames[0];
            ArrayList<Bitmap> detectedText = TextDetector.DetectText(frame.gray(),frame.rgba());
            return detectedText;

        }


        @Override
        protected void onPostExecute(ArrayList<Bitmap> detectedText) {

            TessOCR ocr = new TessOCR(getApplicationContext());
            ArrayList<String> parsedText = new ArrayList<>();
            Iterator<Bitmap> iterator = detectedText.iterator();
            while(iterator.hasNext())
            {
                Bitmap temp = iterator.next();
                String result = ocr.getOCRResult(temp);
                parsedText.add((result.length() !=0)? result : "");
            }



            Iterator<String> iterator2 = parsedText.iterator();

            while(iterator2.hasNext())
            {
                Log.d("Parsed Text" , "Text result"  + iterator2.next());
            }


        }
    }
}
