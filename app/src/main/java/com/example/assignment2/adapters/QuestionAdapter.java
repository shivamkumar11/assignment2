package com.example.assignment2.adapters;

import android.graphics.Color;
import android.view.*;
import android.widget.TextView;
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

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.ViewHolder holder, int position) {
        QuestionModel question = questionList.get(position);
        holder.txtQuestion.setText("Q: "+question.getQuestion());
        holder.txtCategory.setText("#" + question.getCategory());
        holder.txtCategory.setTextColor(Color.parseColor("#2196F3")); // Material Blue 500


        holder.txtAnswer.setText("Answer: " + question.getAnswer());
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }
}
