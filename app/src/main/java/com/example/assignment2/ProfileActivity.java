package com.example.assignment2;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {


        TextView nameTextView, emailTextView, uidTextView;

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
