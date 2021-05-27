package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class editPosts extends AppCompatActivity {

    Toolbar toolbar;
    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private static final String POSTS = "posts";
    private static final String TAG = "FirestoreListActivity";
    private ArrayAdapter<Posts> adapter;
    private ArrayList<Posts> posts;
    private ArrayList<QueryDocumentSnapshot> postIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_posts);
        toolbar = (Toolbar) findViewById(R.id.Wonder);
        setSupportActionBar(toolbar);
        toolbar.setTitle("The Wonder App");

        posts = new ArrayList<>();
        postIDs = new ArrayList<>();
        final String email = getIntent().getStringExtra("CurrentUserEmail");

        //Getting data from firebase
        mDb.collection(POSTS)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document: queryDocumentSnapshots){
                            Posts p = document.toObject(Posts.class);
                            if (p.getEmail().equals(email)){
                                posts.add(p);
                                postIDs.add(document);
                                Log.d(TAG, p.getTitle() + " " + p.getPrice());
                            }
                        }
                        Log.d(TAG, "Amount of post => " +  String.valueOf(posts.size()));
                        adapter.clear();
                        adapter.addAll(posts);
                    }
                });

        //Setting up ListView
        ListView postsListView = findViewById(R.id.itemPosts);
        adapter = new ArrayAdapter<Posts>(
                editPosts.this,
                android.R.layout.simple_expandable_list_item_1,
                new ArrayList<Posts>()
        );
        postsListView.setAdapter(adapter);
        postsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(editPosts.this, postViewEditable.class);
                intent.putExtra("Posts", posts.get(i));
                intent.putExtra("PostIDs", postIDs.get(i).getId());
                intent.putExtra("CurrentUserEmail", email);
                startActivity(intent);
            }
        });

        Button b2 = findViewById(R.id.newPost);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(editPosts.this, NewPost.class);
                intent.putExtra("CurrentUserEmail", email);
                startActivity(intent);
            }
        });

        Button b1 = findViewById(R.id.logout);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth firebase = FirebaseAuth.getInstance();
                firebase.signOut();
                Intent intent = new Intent(editPosts.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

}