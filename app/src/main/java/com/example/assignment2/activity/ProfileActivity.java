package com.example.assignment2.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {


    TextView nameTextView;
    TextView emailTextView;

    @SuppressLint("SetTextI18n")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profile);

            nameTextView = findViewById(R.id.textUserName);
            emailTextView = findViewById(R.id.textUserEmail);


            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null) {
                nameTextView.setText("Name: " + (user.getDisplayName() != null ? user.getDisplayName() : "N/A"));
                emailTextView.setText("Email: " + user.getEmail());

            } else {
                Toast.makeText(this, "No user is logged in", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
