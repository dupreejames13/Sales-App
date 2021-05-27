package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class NewPost extends AppCompatActivity {
    Button b1;
    EditText title;
    EditText price;
    EditText description;
    EditText contact;
    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    public void onSubmitClick(View view){
        title = findViewById(R.id.editTextTitle);
        price = findViewById(R.id.editTextPrice);
        description = findViewById(R.id.editTextDescription);
        contact = findViewById(R.id.editTextContact);

        String titleString = title.getText().toString();
        String priceString = price.getText().toString();
        String descriptionString = description.getText().toString();
        String contactString = contact.getText().toString();

        if(getIntent().getStringExtra("CurrentUserEmail") == null)
        {
            //Sends user back to home page if somehow they were logged in without an account.
            Intent intent = new Intent(NewPost.this, DetailActivity.class);
            startActivity(intent);
        }
        String email = getIntent().getStringExtra("CurrentUserEmail");


        if (titleString.isEmpty() || priceString.isEmpty() || descriptionString.isEmpty() || contactString.isEmpty()){
            Toast.makeText(NewPost.this, "You must have all fields complete!", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            double priceDouble = Double.parseDouble(priceString);
        }
        catch(Exception e){
            Toast.makeText(NewPost.this, "Please give a valid number for a price.", Toast.LENGTH_SHORT).show();
            return;
        }
        double priceDouble = Double.parseDouble(priceString);
        if (priceDouble < 0){
            Toast.makeText(NewPost.this, "You must have a valid price!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (contactString.length() < 9 || contactString.length() >= 11){
            Toast.makeText(NewPost.this, "Please give a valid number for a contact reference including area code.", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            double contactDouble = Double.parseDouble(contactString);
        }
        catch (Exception e2){
            Toast.makeText(NewPost.this, "Please give a valid number for a contact reference.", Toast.LENGTH_SHORT).show();
            return;
        }

        Posts p = new Posts(titleString, priceDouble, descriptionString, contactString, email);
        Log.d("NewPost", "Submitted title:" + titleString + ", price" + priceString + ", description" + descriptionString + ", contact" + contactString);
        mDb.collection("posts").add(p).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("NewPost", "Entry added successfully");
                Toast.makeText(NewPost.this, "Post submitted successfully.", Toast.LENGTH_SHORT).show();
                title.setText("");
                price.setText("");
                description.setText("");
                contact.setText("");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("NewPost", "Entry added failed");
                        Toast.makeText(NewPost.this, "Post submission failed.", Toast.LENGTH_SHORT).show();
                    }
                });

        //Sends user back to home page
        Intent intent = new Intent(NewPost.this, DetailActivity.class);
        startActivity(intent);
    }
}