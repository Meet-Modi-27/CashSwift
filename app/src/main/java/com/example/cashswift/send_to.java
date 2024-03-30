package com.example.cashswift;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.cashswift.Adapters.SendTo_adapter;
import com.example.cashswift.Models.usersModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class send_to extends AppCompatActivity {

    String item[] = {"Food"
            , "Education"
            , "Household"
            , "Health"
            , "Transportation"
            , "Others"
    };
    RecyclerView rv;
    SendTo_adapter ad;
    DatabaseReference usersRef;
    Button send;
    TextInputEditText amount_text,others;
    TextInputLayout othersLayout;
    String amountString;
    double amount;
    String dropDownItem;
    ArrayAdapter<String> adapterItems;
    AutoCompleteTextView transaction_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to);

        String send_to_userId = getIntent().getStringExtra("UserId");
        String UserId = getIntent().getStringExtra("userId");
        rv = findViewById(R.id.send_to_recycler);
        rv.setLayoutManager(new LinearLayoutManager(this));

        send = findViewById(R.id.send_to_send);
        amount_text = findViewById(R.id.send_to_amount);

        others= findViewById(R.id.send_to_otherTransaction);
        othersLayout= findViewById(R.id.send_to_otherTransaction_layout);

        transaction_type = findViewById(R.id.transaction_type);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_itme, item);

        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = usersRef.child("Reg_Users").orderByChild("userId").equalTo(send_to_userId);

        FirebaseRecyclerOptions<usersModel> options =
                new FirebaseRecyclerOptions.Builder<usersModel>()
                        .setQuery(query, usersModel.class)
                        .build();

        ad = new SendTo_adapter(options);
        rv.setAdapter(ad);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountString = amount_text.getText().toString();

                if ("Others".equals(dropDownItem)) {
                    dropDownItem = others.getText().toString();
                }

                if (dropDownItem == null || dropDownItem.isEmpty()) {
                    Toast.makeText(send_to.this, "Category Can't be Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!amountString.isEmpty()) {
                    try {
                        amount = Double.parseDouble(amountString);
                    } catch (NumberFormatException e) {
                        // Handle the case where the input is not a valid double
                        // For example, show an error message to the user
                        Log.d("Running","Catch Running");
                        amount = 0.0; // Set a default value or handle it accordingly
                    }
                } else {
                    // Handle the case where the input is empty
                    // For example, show an error message to the user
                    Log.d("Running","else Running");
                    amount = 0.0; // Set a default value or handle it accordingly
                }

                Log.d("Others",dropDownItem);

                // Proceed with the intent to the next activity
                Intent intent = new Intent(send_to.this, password_validate.class);
                intent.putExtra("userId", UserId);
                intent.putExtra("amount", amount);
                intent.putExtra("UserId", send_to_userId);
                intent.putExtra("desc",dropDownItem);
                startActivity(intent);
            }
        });
        transaction_type.setAdapter(adapterItems);
        transaction_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateSelectedItem(parent.getItemAtPosition(position).toString());
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (ad != null) {
            ad.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (ad != null) {
            ad.stopListening();
        }
    }

    private void updateSelectedItem(String selectedItem) {
        dropDownItem = selectedItem;
        if (dropDownItem != null) {
            Toast.makeText(this, "Selected Item: " + dropDownItem, Toast.LENGTH_SHORT).show();
        }
        if ("Others".equals(dropDownItem)) {
            others.setVisibility(View.VISIBLE);
            othersLayout.setVisibility(View.VISIBLE);
        }
    }
}