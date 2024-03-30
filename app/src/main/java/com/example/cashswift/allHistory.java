package com.example.cashswift;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.cashswift.Adapters.allHistory_adapter;
import com.example.cashswift.Adapters.profile_adapter;
import com.example.cashswift.Models.transactionModel;
import com.example.cashswift.Models.usersModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class allHistory extends AppCompatActivity {

    String userId;
    DatabaseReference db;
    RecyclerView rv;
    allHistory_adapter ad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_history);

        userId = getIntent().getStringExtra("userId");
        rv = (RecyclerView) findViewById(R.id.allHistory_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseDatabase.getInstance().getReference().child("users");

        Log.d("Starting","Starting........");
        FirebaseRecyclerOptions<transactionModel> options =
                new FirebaseRecyclerOptions.Builder<transactionModel>()
                        .setQuery(db.child("Transaction_logs").child(userId), transactionModel.class)
                        .build();

        ad = new allHistory_adapter(options);
        rv.setAdapter(ad);
    }
    @Override
    protected void onStart() {
        super.onStart();
        ad.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ad.stopListening();
    }
}