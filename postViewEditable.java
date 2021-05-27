package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class postViewEditable extends AppCompatActivity {

    EditText eTitle;
    EditText eDesc;
    EditText ePrice;
    EditText eContact;
    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view_editable);
        final Posts post = (Posts)getIntent().getSerializableExtra("Posts");
        final String postID = (String) getIntent().getSerializableExtra("PostIDs");
        Toolbar toolbar = (Toolbar) findViewById(R.id.Wonder);
        setSupportActionBar(toolbar);
        toolbar.setTitle("The Wonder App");

        eTitle = findViewById(R.id.editTitle2);
        eDesc = findViewById(R.id.textView2);
        ePrice = findViewById(R.id.textView4);
        eContact = findViewById(R.id.textView3);

        eTitle.setText(post.getTitle());
        eDesc.setText(post.getDescription());
        ePrice.setText("$" + String.format("%.2f", post.getPrice()));
        eContact.setText(post.getContact());

        Button b2 = findViewById(R.id.deletePost);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDb.collection("posts").document(postID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(postViewEditable.this, "Post deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(postViewEditable.this, "Error deleting post", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        Button b3 = findViewById(R.id.updatePost);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleString = eTitle.getText().toString();
                String priceString = ePrice.getText().toString();
                priceString = priceString.replace("$","0");
                System.out.println(priceString);
                String descriptionString = eDesc.getText().toString();
                String contactString = eContact.getText().toString();
                String email = getIntent().getStringExtra("CurrentUserEmail");

                if (titleString.isEmpty() || priceString.isEmpty() || descriptionString.isEmpty() || contactString.isEmpty()){
                    Toast.makeText(postViewEditable.this, "You must have all fields complete!", Toast.LENGTH_SHORT).show();
                    return;
                }
                try{
                    double priceDouble = Double.parseDouble(priceString);
                }
                catch(Exception e){
                    Toast.makeText(postViewEditable.this, "Please give a valid number for a price.", Toast.LENGTH_SHORT).show();
                    return;
                }
                double priceDouble = Double.parseDouble(priceString);
                if (priceDouble < 0){
                    Toast.makeText(postViewEditable.this, "You must have a valid price!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (contactString.length() < 9 || contactString.length() >= 11){
                    Toast.makeText(postViewEditable.this, "Please give a valid number for a contact reference including area code.", Toast.LENGTH_SHORT).show();
                    return;
                }
                try{
                    double contactDouble = Double.parseDouble(contactString);
                }
                catch (Exception e2){
                    Toast.makeText(postViewEditable.this, "Please give a valid number for a contact reference.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(postViewEditable.this, "Update Successful", Toast.LENGTH_SHORT).show();
                Posts p = new Posts(titleString,priceDouble,descriptionString,contactString,email);
                mDb.collection("posts").document(postID).set(p).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(postViewEditable.this, "Post updated successfully.", Toast.LENGTH_SHORT).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(postViewEditable.this, "Post failed to update.", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });


    }
}