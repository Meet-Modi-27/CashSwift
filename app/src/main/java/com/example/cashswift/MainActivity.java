package com.example.cashswift;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.cashswift.Adapters.dashboard_adapter;
import com.example.cashswift.Models.usersModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class MainActivity extends AppCompatActivity {
    RecyclerView rv;
    dashboard_adapter adapter;
    DatabaseReference usersRef;
    CardView scan_qr;
    CardView history;
    CardView check_balance;
    CardView offers;
    String results;
    ImageView profile;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userId = getIntent().getStringExtra("userId");

        scan_qr = findViewById(R.id.dashboard_scan_qr);
        check_balance = findViewById(R.id.dashboard_checkBalance);
        offers = findViewById(R.id.dashboard_offers);
        history = findViewById(R.id.history);
        rv = findViewById(R.id.dashboard_recycler);
        profile = findViewById(R.id.user_logo);
        rv.setLayoutManager(new LinearLayoutManager(this));


        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        // Query to fetch data for the specific user based on userId
        Query query = usersRef.child("Reg_Users").orderByChild("userId").equalTo(userId);

        FirebaseRecyclerOptions<usersModel> options =
                new FirebaseRecyclerOptions.Builder<usersModel>()
                        .setQuery(query, usersModel.class)
                        .build();

        adapter = new dashboard_adapter(options);
        rv.setAdapter(adapter);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,user_profile.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

        offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Offers.class);
                startActivity(intent);
            }
        });

        check_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, check_balance.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

        scan_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setPrompt("Scan A QR");
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.initiateScan();
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,history.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (intentResult != null){
            String contents = intentResult.getContents();
            if (contents != null){
                results = intentResult.getContents();
                Intent intent = new Intent(MainActivity.this,send_to.class);
                intent.putExtra("UserId",results);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

}