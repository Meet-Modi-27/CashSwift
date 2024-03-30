package com.example.cashswift;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.cashswift.Adapters.SendTo_adapter;
import com.example.cashswift.Adapters.checkBalance_adapter;
import com.example.cashswift.Models.usersModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class check_balance extends AppCompatActivity {

    RecyclerView rv;
    checkBalance_adapter ad;
    DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_balance);

        String userId = getIntent().getStringExtra("userId");
        rv = findViewById(R.id.checkBalance_recycler);
        rv.setLayoutManager(new LinearLayoutManager(this));

        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = usersRef.child("Reg_Users").orderByChild("userId").equalTo(userId);

        FirebaseRecyclerOptions<usersModel> options =
                new FirebaseRecyclerOptions.Builder<usersModel>()
                        .setQuery(query, usersModel.class)
                        .build();

        ad = new checkBalance_adapter(options);
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