package com.example.cashswift;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cashswift.Adapters.SendTo_adapter;
import com.example.cashswift.Adapters.password_data_adapter;
import com.example.cashswift.Models.transactionModel;
import com.example.cashswift.Models.usersModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class password_validate extends AppCompatActivity {

    String userId, sendTo_User;
    Double amount;
    DatabaseReference usersRef, db;
    TextView amount_Text, upi_pin, from_user, to_user;
    RecyclerView rv;
    password_data_adapter ad;
    ProgressBar progressBar;
    TextInputEditText password;
    Button btn;
    String desc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_validate);
        userId = getIntent().getStringExtra("userId");
        sendTo_User = getIntent().getStringExtra("UserId");
        desc = getIntent().getStringExtra("desc");
        amount = getIntent().getDoubleExtra("amount", 0.0);
        amount_Text = findViewById(R.id.password_amount);
        upi_pin = findViewById(R.id.password_upi_pass);
        from_user = findViewById(R.id.password_from_user);
        to_user = findViewById(R.id.password_to_user);
        progressBar = findViewById(R.id.progress_bar);
        amount_Text.setText(amount.toString());
        rv = findViewById(R.id.password_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        password = findViewById(R.id.password_password);
        btn = findViewById(R.id.password_btn);
        db = FirebaseDatabase.getInstance().getReference().child("users");


        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = usersRef.child("Reg_Users").orderByChild("userId").equalTo(sendTo_User);

        FirebaseRecyclerOptions<usersModel> options =
                new FirebaseRecyclerOptions.Builder<usersModel>()
                        .setQuery(query, usersModel.class)
                        .build();

        ad = new password_data_adapter(options);
        rv.setAdapter(ad);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String enteredPassword = password.getText().toString();

                // Ensure userId is not null before proceeding
                if (userId != null) {
                    // Assuming 'db' is correctly initialized to point to the Firebase database location
                    db.child("Reg_Users").child(userId).child("upi_pass").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String data = snapshot.getValue(String.class); // Assuming the data is of type String
                                // Moved the comparison logic here, inside onDataChange
                                if (data.equals(enteredPassword)) {
                                    // Password match, proceed with the update and transaction
                                    updateBalance();
                                    initiateTransaction();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Intent intent = new Intent(password_validate.this, MainActivity.class);
                                    intent.putExtra("userId", userId);
                                    startActivity(intent);
                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(password_validate.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(password_validate.this, "Error: Transaction Password not Set!!!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle possible errors
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(password_validate.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Handle the case where userId is null
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(password_validate.this, "Error: User ID is null", Toast.LENGTH_SHORT).show();
                }
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

    // Define a callback interface
    interface PassCheckCallback {
        void onPassChecked(boolean isCorrect);
    }


    private void updateBalance() {
        usersRef = FirebaseDatabase.getInstance().getReference().child("users").child("Reg_Users");
        usersRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        // Retrieve user data
                        String retrievedUserId = userSnapshot.getKey(); // Get the user ID directly
                        if (retrievedUserId != null && retrievedUserId.equals(userId)) {
                            // Email matches and user ID retrieved
                            double senderBalance = userSnapshot.child("balance").getValue(Double.class);
                            if (senderBalance >= amount) { // Check if sender has sufficient balance
                                // Deduct amount from sender's balance
                                double senderNewBalance = senderBalance - amount;
                                userSnapshot.child("balance").getRef().setValue(senderNewBalance);
                                // Now, add the amount to the recipient's balance
                                addAmountToRecipient(amount);
                            } else {
                                // Handle insufficient balance scenario for sender
                                Toast.makeText(password_validate.this, "Insufficient balance for sender", Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(password_validate.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addAmountToRecipient(double amount) {
        usersRef.orderByChild("userId").equalTo(sendTo_User).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        // Retrieve recipient's user data
                        Double recipientBalance = userSnapshot.child("balance").getValue(Double.class);
                        if (recipientBalance != null) {
                            // Add amount to recipient's balance
                            double recipientNewBalance = recipientBalance + amount;
                            userSnapshot.child("balance").getRef().setValue(recipientNewBalance);
                            // Handle success scenario for adding amount to recipient
                            Toast.makeText(password_validate.this, "Amount added to recipient", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                } else {
                    // Handle recipient's user not found
                    Toast.makeText(password_validate.this, "Recipient's user not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(password_validate.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initiateTransaction() {
        fromUser();
    }

    // Method to populate the 'from_user' field
    private void fromUser() {
        db.child("Reg_Users").child(userId).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String data = snapshot.getValue(String.class);
                    processFromUser(data);
                    // After populating sender, initiate the transaction
                    if (!TextUtils.isEmpty(amount_Text.getText().toString())) {
                        usersTransaction();
                        receiverTransaction();
                    } else {
                        Toast.makeText(password_validate.this, "Please enter the transaction amount", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(password_validate.this, "Internal Error!!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("toUser", "Database error: " + error.getMessage());
            }
        });
    }


    private void processFromUser(String data) {
        from_user.setText(data);
    }

    private void usersTransaction() {
        DatabaseReference userRef = db.child("Transaction_logs").child(userId);
        String Amount = amount_Text.getText().toString();
        String from = from_user.getText().toString();
        long timestamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedTime = sdf.format(new Date(timestamp));
        Log.d("Timestamp", formattedTime);
        String transactionId = userRef.push().getKey();
        String received = "false";
        db.child("Reg_Users").child(sendTo_User).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String data = snapshot.getValue(String.class);
                    to_user.setText(data);
                    String to = to_user.getText().toString();
                    transactionModel TransactionModel = new transactionModel(formattedTime, transactionId, desc, Amount, from, to,received);
                    userRef.child(transactionId).setValue(TransactionModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Transaction successful
                                Toast.makeText(password_validate.this, "Transaction completed successfully!", Toast.LENGTH_SHORT).show();
                            } else {
                                // Transaction failed
                                Toast.makeText(password_validate.this, "Transaction failed. Please try again later.", Toast.LENGTH_SHORT).show();
                                // Log the error for debugging purposes
                                Log.e("Transaction", "Failed to complete transaction: " + task.getException().getMessage());
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void receiverTransaction() {
        DatabaseReference userRef = db.child("Transaction_logs").child(sendTo_User);
        String Amount = amount_Text.getText().toString();
        String from = from_user.getText().toString();
        long timestamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedTime = sdf.format(new Date(timestamp));
        Log.d("Timestamp", formattedTime);
        String transactionId = userRef.push().getKey();
        String received = "true";
        db.child("Reg_Users").child(sendTo_User).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String data = snapshot.getValue(String.class);
                    to_user.setText(data);
                    String to = to_user.getText().toString();
                    transactionModel TransactionModel = new transactionModel(formattedTime, transactionId, desc, Amount, from, to,received);
                    userRef.child(transactionId).setValue(TransactionModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Transaction successful
                                Toast.makeText(password_validate.this, "Transaction completed successfully!", Toast.LENGTH_SHORT).show();
                            } else {
                                // Transaction failed
                                Toast.makeText(password_validate.this, "Transaction failed. Please try again later.", Toast.LENGTH_SHORT).show();
                                // Log the error for debugging purposes
                                Log.e("Transaction", "Failed to complete transaction: " + task.getException().getMessage());
                            }
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}