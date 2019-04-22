package com.example.proj;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegisterActivity extends AppCompatActivity {

    EditText newEmail, newPass;
    Button signUpButton, hasAccountButton;
    private FirebaseAuth mAuth;
    ProgressBar signUpProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        newEmail = (EditText) findViewById(R.id.newEmail);
        newPass = (EditText) findViewById(R.id.newPass);
        signUpProgressBar = (ProgressBar) findViewById(R.id.signUpProgressBar);

        mAuth = FirebaseAuth.getInstance();

        signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                registerUser();
            }
        });

        hasAccountButton = (Button) findViewById(R.id.hasAccountButton);
        //set the on click listener for the second button
        hasAccountButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });


    }

    private void registerUser(){
        String email = newEmail.getText().toString().trim();
        String password = newPass.getText().toString().trim();

        if (email.isEmpty()){
            newEmail.setError("Email field cannot be left blank");
            newEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            newEmail.setError("Please enter a valid email address.");
            newEmail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            newPass.setError("Password field cannot be left blank");
            newPass.requestFocus();
            return;
        }

        if(password.length()<6){
            newPass.setError("Minimum password length is 6 characters.");
            newPass.requestFocus();
            return;
        }

        signUpProgressBar.setVisibility(View.INVISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                signUpProgressBar.setVisibility(View.VISIBLE);
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "User registration successful!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
                else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(), "User is already registered", Toast.LENGTH_LONG).show();
                        signUpProgressBar.setVisibility(View.INVISIBLE);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Error occurred. Please try again!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                }
            }
        });
    }

}
