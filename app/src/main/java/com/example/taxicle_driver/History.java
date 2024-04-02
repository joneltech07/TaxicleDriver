package com.example.taxicle_driver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.taxicle_driver.adapter.HistoryAdapter;
import com.example.taxicle_driver.Model.DriverHistory;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class History extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    HistoryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<DriverHistory> options =
                new FirebaseRecyclerOptions.Builder<DriverHistory>()
                        .setQuery(
                                FirebaseDatabase.getInstance().getReference(DriverHistory.class.getSimpleName()).child(user.getUid()),
                                DriverHistory.class
                        )
                        .build();
        adapter = new HistoryAdapter(options);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}