package com.example.survey_app;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PickStoreAndScan extends AppCompatActivity {
    private Button take; //used to scan survey
    private DatabaseReference db;
    public static EditText surveyEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_store_and_scan);

        //the spinner is what the drop down menu is called in the activity files
        Spinner mySpinner = (Spinner) findViewById(R.id.spin);

        //sets strings that I've listed in the strings.xml file into a drop down menu
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(PickStoreAndScan.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        //=====================Requesting Permissions=======
        final int PERMISSION_ALL = 1;
        final String[] PERMISSIONS = {
                Manifest.permission.INTERNET,
                android.Manifest.permission.CAMERA
        };

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        // initialization of widgets
        take = findViewById(R.id.takeButton);

        //connecting to database
        db = FirebaseDatabase.getInstance().getReference();

        //code for take button
        final Context thisContext = this;
        final Activity thisActivity = this;
        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hasPermissions(thisContext, PERMISSIONS)) {
                    Intent i = new Intent(getApplicationContext(), com.example.bookies.BarcodeScannerActivity.class);
                    startActivity(i);
                    return;
                }
                Toast.makeText(thisContext, "Permission to use camera not granted.", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(thisActivity, PERMISSIONS, PERMISSION_ALL);
            }
        });

    }

    /**
     * validates format of ISBN number
     */
    //use when making request to database
    protected boolean isValidISBNFormat(String isbnNumber) {
        //TODO: Tweak conditions before use
        return isbnNumber.matches("^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})"
                + "[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)"
                + "(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$");
    }

    /**
     * Retrieves book review from database
     */
    protected Book getBookReview(final String isbnNumber) {

        final Book[] books = new Book[1];

        book = new Book();//instantiate book object

        db.collection("book")
                .document(isbnNumber)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            try {

                                DocumentSnapshot document = task.getResult();

                                //passing data to Review Activity
                                Intent i = new Intent(getApplicationContext(), ReviewActivity.class);

                                //retrieving book data from database
                                book.setAuthor(document
                                        .getData()
                                        .get("author")
                                        .toString());

                                book.setReview(document
                                        .getData()
                                        .get("amazon review")
                                        .toString());

                                book.setTitle(document
                                        .getData()
                                        .get("title")
                                        .toString());

                                book.setSeller(document
                                        .getData()
                                        .get("seller")
                                        .toString());

                                book.setISBNNumber(isbnNumber);

                                book.setImageLink(document
                                        .getData()
                                        .get("image")
                                        .toString());
                                //passing book to reviewActivity
                                i.putExtra("book", book);

                                books[0] = book;
                                startActivity(i);//starting activity

                            }
                            //in case retrieval fails
                            catch (Exception e) {
                                Toast.makeText(IsbnActivity
                                        .this, "ISBN number not found", Toast
                                        .LENGTH_LONG)
                                        .show();
                            }

                        }
                    }
                });
        return books[0];
    }

    /**
     * checks for permissions
     */
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
}
