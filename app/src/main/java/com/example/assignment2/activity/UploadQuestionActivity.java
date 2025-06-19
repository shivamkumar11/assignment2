package com.example.assignment2.activity;

import android.os.Bundle;
import android.text.InputFilter;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.*;

public class UploadQuestionActivity extends AppCompatActivity {

    EditText edtQuestion, edtNewCategory, edtAnswer;
    Spinner spinnerCategories;
    Button btnSubmit;

    DatabaseReference questionsRef;

    List<String> categoriesList = new ArrayList<>();
    ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.assignment2.R.layout.activity_upload_question);

        edtQuestion = findViewById(com.example.assignment2.R.id.edtQuestion);
        edtNewCategory = findViewById(com.example.assignment2.R.id.edtNewCategory);
        edtAnswer = findViewById(com.example.assignment2.R.id.edtAnswer);
        spinnerCategories = findViewById(com.example.assignment2.R.id.spinnerCategories);
        btnSubmit = findViewById(R.id.btnSubmit);

        questionsRef = FirebaseDatabase.getInstance().getReference("questions");

        // Initialize spinner
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoriesList);
        spinnerCategories.setAdapter(spinnerAdapter);

        loadCategoriesFromFirebase();
        edtNewCategory.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(20),
                (source, start, end, dest, dstart, dend) -> source.toString().toUpperCase()
        });
        btnSubmit.setOnClickListener(v -> {

            String question = edtQuestion.getText().toString().trim();
            String selectedCategory = spinnerCategories.getSelectedItem() != null ? spinnerCategories.getSelectedItem().toString() : "";
            String newCategory = edtNewCategory.getText().toString().trim();
            String answer = edtAnswer.getText().toString().trim();

            String finalCategory = !newCategory.isEmpty() ? newCategory : selectedCategory;

            if (question.isEmpty() || finalCategory.isEmpty() || answer.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference ref = questionsRef.push();
            Map<String, Object> map = new HashMap<>();
            map.put("question", question);
            map.put("category", finalCategory);
            map.put("answer", answer);
            map.put("timestamp", ServerValue.TIMESTAMP);
            map.put("userId", FirebaseAuth.getInstance().getUid());

            ref.setValue(map)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Question posted", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }

    private void loadCategoriesFromFirebase() {
        questionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> uniqueCategories = new HashSet<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String cat = snap.child("category").getValue(String.class);
                    if (cat != null) uniqueCategories.add(cat);
                }
                categoriesList.clear();
                categoriesList.addAll(uniqueCategories);
                Collections.sort(categoriesList);
                spinnerAdapter.notifyDataSetChanged();
            }

            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
