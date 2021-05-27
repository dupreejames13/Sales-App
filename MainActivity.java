package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button b1;
    Button b2;
    EditText inputEmail;
    EditText inputPassword;
    FirebaseAuth firebase;
    Toolbar toolbar;
    public void onStart(){
        super.onStart();
        if (firebase.getCurrentUser() != null){
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("CurrentUserEmail", firebase.getCurrentUser().getEmail());
            startActivity(intent);
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1 = (Button) findViewById(R.id.loginto);
        b2 = (Button) findViewById(R.id.newAccount);
        toolbar = (Toolbar) findViewById(R.id.Wonder);
        setSupportActionBar(toolbar);
        toolbar.setTitle("The Wonder App");
        toolbar.setSubtitle("Please login.");
        inputEmail = (EditText) findViewById(R.id.Email);
        inputPassword = (EditText) findViewById(R.id.Password);
        firebase = FirebaseAuth.getInstance();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                if (email.isEmpty() || password.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
                    return;
                }
                firebase.signInWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Incorrect Login.", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                            intent.putExtra("CurrentUserEmail", inputEmail.getText().toString());
                            startActivity(intent);
                        }
                    }
                });
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                if (email.isEmpty() || password.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
                }

                else if (!email.contains("@")){
                    Toast.makeText(MainActivity.this, "Please give a valid email.", Toast.LENGTH_SHORT).show();
                }
                else if(password.length() < 6){
                    Toast.makeText(MainActivity.this, "Password needs to be greater than six characters.", Toast.LENGTH_SHORT).show();
                }
                else{
                    firebase.createUserWithEmailAndPassword(email,password);
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("CurrentUserEmail", inputEmail.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
}
