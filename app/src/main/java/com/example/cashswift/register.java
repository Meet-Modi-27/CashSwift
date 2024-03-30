package com.example.cashswift;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cashswift.Models.usersModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {

    TextInputEditText name,upi_pass,email,phone,pass;
    Button login,register;
    String EmailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[a-zA-Z]+";
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.reg_name);
        upi_pass = findViewById(R.id.reg_transaction);
        email = findViewById(R.id.reg_email);
        phone = findViewById(R.id.reg_phone);
        pass = findViewById(R.id.reg_password);
        login = findViewById(R.id.btn_login);
        register = findViewById(R.id.reg_next);
        progressBar = findViewById(R.id.progress_bar);
        mAuth= FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance().getReference().child("users");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(register.this,login.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRegistration();
            }
        });



    }

    private void performRegistration() {
        String Name = name.getText().toString();
        String Upi_pass = upi_pass.getText().toString();
        String Email = email.getText().toString();
        String Phone = phone.getText().toString();
        String Pass = pass.getText().toString();

        if (Name.isEmpty()){
            name.setError("Enter the Name.");
        } else if (!Email.matches(EmailPattern)){
            email.setError("Enter Correct Email Id");
        } else if (Phone.isEmpty()){
            phone.setError("Enter the Phone Number");
        } else if (Phone.length() < 10 || Phone.length() > 13){
            phone.setError("Enter Correct Phone Number");
        } else if(Pass.isEmpty()|| Pass.length() < 6){
            pass.setError("Minimum Password length is 6 characters.");
        } else if(Upi_pass.isEmpty()|| Upi_pass.length() < 6){
            pass.setError("Minimum Password length is 6 characters.");
        } else {
            progressBar.setVisibility(View.VISIBLE);


            // Register user with email and password
            mAuth.createUserWithEmailAndPassword(Email, Pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Registration successful
                                FirebaseUser user = mAuth.getCurrentUser();
                                UpdateDataToFirebase(Name,Upi_pass,Email,Phone);
                                // Redirect user to the login activity
                                progressBar.setVisibility(View.INVISIBLE);
                                SendUserToNextActivity();
                                Toast.makeText(register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                // Registration failed
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(register.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void UpdateDataToFirebase(String NAME, String USERNAME, String EMAIL, String PHONE) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid(); // Get the user ID from Firebase Authentication
            DatabaseReference userRef = db.child("Reg_Users").child(userId); // Use the user ID as the key
            String upi = EMAIL.split("@")[0];
            String upiId = upi+"@cashswift";
            Double balance = 0.0;
            usersModel userModel = new usersModel(NAME, USERNAME, EMAIL, PHONE, userId,upiId,balance);
            userRef.setValue(userModel);
        }
    }

    private void SendUserToNextActivity() {
        Intent intent = new Intent(register.this, login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}