package com.example.assignment2.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.*;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment2.QuestionModel;
import com.example.assignment2.R;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private final List<QuestionModel> questionList;

    public QuestionAdapter(List<QuestionModel> questionList) {
        this.questionList = questionList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtQuestion, txtCategory, txtAnswer;

        public ViewHolder(View itemView) {
            super(itemView);
            txtQuestion = itemView.findViewById(R.id.txtQuestion);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            txtAnswer = itemView.findViewById(R.id.txtAnswer);
        }
    }

    @NonNull
    @Override
    public QuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.ViewHolder holder, int position) {
        QuestionModel question = questionList.get(position);
        holder.txtQuestion.setText("Q: " + question.getQuestion());
        holder.txtCategory.setText("#" + question.getCategory());
        holder.txtCategory.setTextColor(Color.parseColor("#2196F3"));
        holder.txtAnswer.setText("Answer: " + question.getAnswer());

        // ✅ Click listener on whole itemView
        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Clicked functionality is not added..", Toast.LENGTH_SHORT).show();
        });
    }


    @Override
    public int getItemCount() {
        return questionList.size();
    }

    // Animation added here
    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        animateItem(holder.itemView);
    }

    private void animateItem(View view) {
        view.setAlpha(0f);
        view.setTranslationY(50f);
        view.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(400)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }
}
