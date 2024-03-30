package com.example.cashswift;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.cashswift.Adapters.sent_adapter;
import com.example.cashswift.Models.Transaction;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class sent_history extends AppCompatActivity {

    private RecyclerView recyclerView;
    private sent_adapter sentAdapter;
    private List<Transaction> transactionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_history);

        String userId = getIntent().getStringExtra("userId");
        recyclerView = findViewById(R.id.sent_history_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        transactionList = new ArrayList<>();
        sentAdapter = new sent_adapter(transactionList);
        recyclerView.setAdapter(sentAdapter);

        Log.d("Starting...","Starting...");
         DatabaseReference transactionsRef = FirebaseDatabase.getInstance().getReference().child("users").child("Transaction_logs").child(userId);
         transactionsRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 transactionList.clear();
                 for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                     Transaction transaction = snapshot.getValue(Transaction.class);
                     if (transaction != null) {
                         if (transaction.getReceived().equals("true")) {
                             transactionList.add(transaction);
                         }
                     }
                 }
                 sentAdapter.notifyDataSetChanged();
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {
                 Toast.makeText(sent_history.this, "Database Error: "+databaseError, Toast.LENGTH_SHORT).show();
             }
         });
    }
}
