package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

public class postView extends AppCompatActivity {
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.Wonder);
        setSupportActionBar(toolbar);
        toolbar.setTitle("The Wonder App");
        final Posts post = (Posts)getIntent().getSerializableExtra("Posts");
        final String email = post.getEmail();


        TextView t1 = findViewById(R.id.itemTitle);
        TextView t2 = findViewById(R.id.textView2);
        TextView t3 = findViewById(R.id.textView3);
        TextView t4 = findViewById(R.id.textView4);
        TextView t5 = findViewById(R.id.textView5);

        t1.setText(post.getTitle());
        t2.setText(post.getDescription());
        t4.setText("$" + String.format("%.2f", post.getPrice()));
        String phoneNumberFormatted = post.getContact().replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
        t3.setText(phoneNumberFormatted);
        t5.setText(email);

        final Button b1 = findViewById(R.id.emailSeller);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
                intent.putExtra(Intent.EXTRA_SUBJECT, post.getTitle());
                startActivity(Intent.createChooser(intent, ""));
            }
        });
    }
}