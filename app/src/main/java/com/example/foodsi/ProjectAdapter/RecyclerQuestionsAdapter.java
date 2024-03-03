package com.example.foodsi.ProjectAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodsi.ProjectClass.QuestionInfo;
import com.example.foodsi.R;
import java.util.ArrayList;

public class RecyclerQuestionsAdapter extends RecyclerView.Adapter<RecyclerQuestionsAdapter.ViewHolder>{

    private ArrayList<QuestionInfo> questionList;

    public RecyclerQuestionsAdapter(ArrayList<QuestionInfo> questionList){
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_content_question, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerQuestionsAdapter.ViewHolder holder, int position) {
        holder.question.setText(questionList.get(position).question);
        holder.option1.setText(questionList.get(position).option1);
        holder.option2.setText(questionList.get(position).option2);
        holder.option3.setText(questionList.get(position).option3);
        holder.option4.setText(questionList.get(position).option4);
        holder.answer.setText(questionList.get(position).answer);

        holder.showHideAnsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionList.get(position).isClicked = !questionList.get(position).isClicked;

                if(questionList.get(position).isClicked){
                    holder.answer.setVisibility(View.VISIBLE);
                    holder.showHideAnsText.setText("Hide Ans");
                }
                else{
                    holder.answer.setVisibility(View.GONE);
                    holder.showHideAnsText.setText("Show Ans");
                }
            }
        });
    }

    @Override
    public int getItemCount() { return questionList.size(); }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView question;
        TextView option1;
        TextView option2;
        TextView option3;
        TextView option4;
        TextView answer;
        TextView showHideAnsText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            question = (TextView) itemView.findViewById(R.id.question_text);
            option1 = (TextView) itemView.findViewById(R.id.option1_text);
            option2 = (TextView) itemView.findViewById(R.id.option2_text);
            option3 = (TextView) itemView.findViewById(R.id.option3_text);
            option4 = (TextView) itemView.findViewById(R.id.option4_text);
            answer = (TextView) itemView.findViewById(R.id.answer_text);
            showHideAnsText = (TextView) itemView.findViewById(R.id.show_hide_ans_text);
        }
    }
}
