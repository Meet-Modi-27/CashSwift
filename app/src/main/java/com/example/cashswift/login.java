package com.example.cashswift;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {

    Button new_user,login,forgot;

    TextInputEditText email,pass;
    String EmailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[a-zA-Z]+";
    ProgressBar progressBar;
    TextInputLayout pass1;


    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        new_user = findViewById(R.id.new_user);
        forgot = findViewById(R.id.forgot_btn);
        login = findViewById(R.id.login_btn);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        pass1 = findViewById(R.id.password_layout);

        progressBar = findViewById(R.id.progress_bar);

        mAuth = FirebaseAuth.getInstance();


        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, forgot_password.class);
                startActivity(intent);
                finish();
            }
        });

        new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, register.class);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });
    }

    private void performLogin() {
        String Email = email.getText().toString();
        String Pass = pass.getText().toString();

        if (!Email.matches(EmailPattern)){
            email.setError("Enter Correct Email Id");
        } else if(Pass.isEmpty()|| Pass.length() < 6){
            pass.setError("Minimum Password length is 6 characters.");
        } else {
            progressBar.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(Email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();
                            checkEmailInDatabaseAndRetrieveUserId(Email, userId);
                        }
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        // Handle authentication failure
                        Exception exception = task.getException();
                        if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                            // Password is incorrect
                            pass1.setError("Incorrect password.");
                        } else {
                            // Other error occurred
                            Toast.makeText(login.this, "Login failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    private void checkEmailInDatabaseAndRetrieveUserId(String email, String userId) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child("Reg_Users");
        usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.INVISIBLE);
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        // Retrieve user data
                        String retrievedUserId = userSnapshot.getKey(); // Get the user ID directly
                        if (retrievedUserId != null && retrievedUserId.equals(userId)) {
                            // Email matches and user ID retrieved
                            SendUserToNextActivity(retrievedUserId);
                            return;
                        }
                    }

                    // User ID not found for the matched email
                    Toast.makeText(login.this, "User ID not found", Toast.LENGTH_SHORT).show();
                } else {
                    // Email not found in the database
                    Toast.makeText(login.this, "Email not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(login.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void SendUserToNextActivity(String userid) {
        Intent intent = new Intent(login.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("userId",userid);
        startActivity(intent);
    }
}