package com.example.assignment2.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment2.QuestionModel;
import com.example.assignment2.R;
import com.example.assignment2.adapters.QuestionAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton fab;
    FirebaseAuth mAuth;
    DatabaseReference questionsRef;

    Spinner categorySpinner;
    List<QuestionModel> allQuestions = new ArrayList<>();
    QuestionAdapter questionAdapter;

    MenuItem loginItem, profileItem, logoutItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.assignment2.R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(com.example.assignment2.R.id.questionRecycler);
        fab = findViewById(com.example.assignment2.R.id.fabAddQuestion);
        categorySpinner = findViewById(com.example.assignment2.R.id.categorySpinner);
        questionsRef = FirebaseDatabase.getInstance().getReference("questions");

        // Toolbar
        MaterialToolbar toolbar = findViewById(com.example.assignment2.R.id.topAppBar);
        setSupportActionBar(toolbar);

        fab.setOnClickListener(view -> {
            if (mAuth.getCurrentUser() != null) {
                startActivity(new Intent(MainActivity.this, UploadQuestionActivity.class));
            } else {
                Toast.makeText(MainActivity.this, "Please log in to upload a question", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        questionAdapter = new QuestionAdapter(allQuestions);
        recyclerView.setAdapter(questionAdapter);

        loadQuestionsAndCategories();
    }

    private void loadQuestionsAndCategories() {
        questionsRef.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allQuestions.clear();
                HashSet<String> categorySet = new HashSet<>();
                List<QuestionModel> tempList = new ArrayList<>();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    String question = snap.child("question").getValue(String.class);
                    String category = snap.child("category").getValue(String.class);
                    String answer = snap.child("answer").getValue(String.class);
                    if (question != null && category != null) {
                        tempList.add(new QuestionModel(question, category, answer));
                        categorySet.add(category);
                    }
                }

                // Add items in reverse order so latest ones appear on top
                for (int i = tempList.size() - 1; i >= 0; i--) {
                    allQuestions.add(tempList.get(i));
                }

                questionAdapter.notifyDataSetChanged();

                List<String> categoryList = new ArrayList<>();
                categoryList.add("All");
                categoryList.addAll(categorySet);

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, categoryList);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(spinnerAdapter);

                categorySpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                        String selected = categoryList.get(position);
                        filterQuestions(selected);
                    }

                    @Override
                    public void onNothingSelected(android.widget.AdapterView<?> parent) { }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to load questions", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterQuestions(String category) {
        List<QuestionModel> filteredList = new ArrayList<>();
        if (category.equals("All")) {
            filteredList.addAll(allQuestions);
        } else {
            for (QuestionModel q : allQuestions) {
                if (q.getCategory().equalsIgnoreCase(category)) {
                    filteredList.add(q);
                }
            }
        }
        questionAdapter = new QuestionAdapter(filteredList);
        recyclerView.setAdapter(questionAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        fab.setEnabled(mAuth.getCurrentUser() != null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.assignment2.R.menu.top_menu, menu);
        loginItem = menu.findItem(com.example.assignment2.R.id.action_login);
        profileItem = menu.findItem(com.example.assignment2.R.id.action_profile);
        logoutItem = menu.findItem(com.example.assignment2.R.id.action_logout);

        FirebaseUser user = mAuth.getCurrentUser();
        boolean loggedIn = user != null;

        loginItem.setVisible(!loggedIn);
        profileItem.setVisible(loggedIn);
        logoutItem.setVisible(loggedIn);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == com.example.assignment2.R.id.action_login) {
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        } else if (id == com.example.assignment2.R.id.action_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        } else if (id == R.id.action_logout) {
            mAuth.signOut();
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            invalidateOptionsMenu();
            fab.setEnabled(false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
