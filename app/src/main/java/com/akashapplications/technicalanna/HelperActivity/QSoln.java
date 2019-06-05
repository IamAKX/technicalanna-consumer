package com.akashapplications.technicalanna.HelperActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.akashapplications.technicalanna.Models.QuizModel;
import com.akashapplications.technicalanna.Models.SubjectExamsModel;
import com.akashapplications.technicalanna.R;
import com.akashapplications.technicalanna.Utils.Tokens;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;

public class QSoln extends AppCompatActivity {
    TextView time, mark, questionNo, question,solution;
    FancyButton[] optionArray;
    FancyButton next,prev;
    SubjectExamsModel examsModel;
    ArrayList<QuizModel> quizList;
    int currentQuestion = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qsoln);
        time = findViewById(R.id.time);
        mark = findViewById(R.id.marks);
        questionNo = findViewById(R.id.questionNo);
        question = findViewById(R.id.question);
        solution = findViewById(R.id.solution);
        examsModel = (SubjectExamsModel) getIntent().getSerializableExtra("exam");
        quizList = examsModel.getQuizList();
        optionArray = new FancyButton[]{
                findViewById(R.id.option1),
                findViewById(R.id.option2),
                findViewById(R.id.option3),
                findViewById(R.id.option4),
        };
        next = findViewById(R.id.next);
        prev = findViewById(R.id.prev);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(Tokens.LOG,"Curr val before : "+currentQuestion);
                if(currentQuestion >= quizList.size()){
                    finish();
                }else
                if(currentQuestion == (quizList.size() - 1)) {
                    next.setText("EXIT");
                    currentQuestion++;
                    populateQuestion();
                }
                else{
                    currentQuestion++;
                    populateQuestion();
                }
                Log.e(Tokens.LOG,"Curr val after : "+currentQuestion);
            }
        });


        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentQuestion == (quizList.size()-1))
                    next.setText("NEXT");
                if(currentQuestion > 1){
                    currentQuestion--;
                    populateQuestion();
                }
            }
        });
        populateQuestion();
    }

    private void populateQuestion() {
        if(currentQuestion > quizList.size())
            return;
        setDefaultBorder();
        questionNo.setText(String.valueOf(currentQuestion));
        question.setText(quizList.get(currentQuestion-1).getQuestion());
        optionArray[0].setText(quizList.get(currentQuestion-1).getOption1());
        optionArray[1].setText(quizList.get(currentQuestion-1).getOption2());
        optionArray[2].setText(quizList.get(currentQuestion-1).getOption3());
        optionArray[3].setText(quizList.get(currentQuestion-1).getOption4());

        optionArray[quizList.get(currentQuestion-1).getCorrectOption()-1].setBorderColor(getResources().getColor(android.R.color.holo_green_dark));
        optionArray[quizList.get(currentQuestion-1).getCorrectOption()-1].setBackgroundColor(getResources().getColor(R.color.gree_trans));
        solution.setText(quizList.get(currentQuestion-1).getSolution());
    }

    private void setDefaultBorder()
    {
        optionArray[0].setBorderColor(getResources().getColor(R.color.option_button_border));
        optionArray[1].setBorderColor(getResources().getColor(R.color.option_button_border));
        optionArray[2].setBorderColor(getResources().getColor(R.color.option_button_border));
        optionArray[3].setBorderColor(getResources().getColor(R.color.option_button_border));

        optionArray[0].setBackgroundColor(getResources().getColor(android.R.color.transparent));
        optionArray[1].setBackgroundColor(getResources().getColor(android.R.color.transparent));
        optionArray[2].setBackgroundColor(getResources().getColor(android.R.color.transparent));
        optionArray[3].setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }
}
