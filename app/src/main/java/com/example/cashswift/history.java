package com.example.cashswift;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class history extends AppCompatActivity {

    String userId;
    Button allHistory,sent,received,category;

    String item[] = {"Food"
            , "Education"
            , "Household"
            , "Health"
            , "Transportation"
            , "Others"
    };
    String dropDownItem;
    ArrayAdapter<String> adapterItems;
    AutoCompleteTextView transaction_type;
    TextInputEditText others;
    TextInputLayout othersLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        userId = getIntent().getStringExtra("userId");
        allHistory = findViewById(R.id.history_allHistory);
        sent = findViewById(R.id.history_sent);
        received = findViewById(R.id.history_received);
        category = findViewById(R.id.history_category);
        others = findViewById(R.id.history_otherTransaction);
        othersLayout = findViewById(R.id.history_otherTransaction_layout);
        transaction_type = findViewById(R.id.transaction_type);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_itme, item);

        allHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(history.this, allHistory.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(history.this,sent_history.class);
                intent.putExtra("userId",userId);
                startActivity(intent);

            }
        });

        received.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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