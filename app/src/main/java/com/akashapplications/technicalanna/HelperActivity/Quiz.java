package com.akashapplications.technicalanna.HelperActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.akashapplications.technicalanna.Models.QuizModel;
import com.akashapplications.technicalanna.Models.SubjectExamsModel;
import com.akashapplications.technicalanna.R;
import com.akashapplications.technicalanna.Utils.Tokens;
import com.budiyev.android.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;

public class Quiz extends AppCompatActivity {

    Handler handler = new Handler();
    CircularProgressBar progressBar;
    TextView time, mark, questionNo, question;
    FancyButton[] optionArray;
    FancyButton next;
    SubjectExamsModel examsModel;
    ArrayList<QuizModel> quizList;
    int currentQuestion = 1;
    int ansArray[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        time = findViewById(R.id.time);
        mark = findViewById(R.id.marks);
        questionNo = findViewById(R.id.questionNo);
        question = findViewById(R.id.question);
        progressBar = findViewById(R.id.progress_bar);
        examsModel = (SubjectExamsModel) getIntent().getSerializableExtra("exam");
        optionArray = new FancyButton[]{
                findViewById(R.id.option1),
                findViewById(R.id.option2),
                findViewById(R.id.option3),
                findViewById(R.id.option4),
        };
        next = findViewById(R.id.next);
        quizList = examsModel.getQuizList();
        ansArray = new int[quizList.size()];
        setDefaultAns();

        mark.setText(String.valueOf(examsModel.getFullMarks()));
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuestion++;
                if(currentQuestion > quizList.size())
                {
                    setEnabilityOfOptions(false);
                    calculateMarks();
                }
                else
                {
                    if(currentQuestion == quizList.size())
                        next.setText("SUBMIT");
                    setAllOptionWhite();
                    populateQuestion();
                }

            }
        });
        updateProgress();
        populateQuestion();
    }

    private void calculateMarks() {
//        setEnabilityOfOptions(false);
        int correct = 0;
        int incorrect = 0;
        int skipped = 0;
        double markForSingleQuestion = examsModel.getFullMarks()/examsModel.getQuizList().size();
        for (int i = 0; i < ansArray.length; i++) {
            if(ansArray[i] == -1)
                skipped++;
            else
                if(ansArray[i] == quizList.get(i).getCorrectOption())
                    correct++;
                else
                    incorrect++;

        }
        double percent = (correct * 100)/quizList.size();
        double fullMarks = ((markForSingleQuestion * correct) - (incorrect * examsModel.getNegMark() * markForSingleQuestion));
        System.out.print("Total "+fullMarks);
        Log.i(Tokens.LOG,"Total "+ fullMarks);

        startActivity(new Intent(getBaseContext(),ExamSummary.class)
        .putExtra("percent",percent)
                        .putExtra("marks",fullMarks)
                        .putExtra("total",examsModel.getFullMarks())
                        .putExtra("correct",correct)
                        .putExtra("skipped",skipped)
                        .putExtra("incorrect",incorrect)
                        .putExtra("correctans",ansArray)
                        .putExtra("exammodel",examsModel)
        );
        finish();
    }

    public void setDefaultAns()
    {
        for (int i = 0; i < ansArray.length; i++) {
            ansArray[i] = -1;
        }
    }

    private void populateQuestion() {
        questionNo.setText(String.valueOf(currentQuestion));
        question.setText(quizList.get(currentQuestion-1).getQuestion());
        optionArray[0].setText(quizList.get(currentQuestion-1).getOption1());
        optionArray[1].setText(quizList.get(currentQuestion-1).getOption2());
        optionArray[2].setText(quizList.get(currentQuestion-1).getOption3());
        optionArray[3].setText(quizList.get(currentQuestion-1).getOption4());

        optionArray[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ansArray[currentQuestion-1] = 1;
                setAllOptionWhite();
                optionArray[0].setBackgroundColor(getResources().getColor(R.color.blue_white));
            }
        });
        optionArray[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ansArray[currentQuestion-1] = 2;
                setAllOptionWhite();
                optionArray[1].setBackgroundColor(getResources().getColor(R.color.blue_white));
            }
        });
        optionArray[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ansArray[currentQuestion-1] = 3;
                setAllOptionWhite();
                optionArray[2].setBackgroundColor(getResources().getColor(R.color.blue_white));
            }
        });
        optionArray[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ansArray[currentQuestion-1] = 4;
                setAllOptionWhite();
                optionArray[3].setBackgroundColor(getResources().getColor(R.color.blue_white));
            }
        });
    }

    private void setAllOptionWhite() {
        optionArray[0].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        optionArray[1].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        optionArray[2].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        optionArray[3].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    private void setEnabilityOfOptions(boolean b) {
        optionArray[0].setEnabled(b);
        optionArray[1].setEnabled(b);
        optionArray[2].setEnabled(b);
        optionArray[3].setEnabled(b);
    }

    private void updateProgress() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                int sec = 0;
                int min = examsModel.getTimeAlloted()-1;

                while (min >=0)// TODO Auto-generated method stub
                {
                    if(min < 0)
                        time.setTextColor(getResources().getColor(R.color.orange));
                    else
                        time.setTextColor(getResources().getColor(R.color.colorAccent));
                    while (sec <= 59) {
                        sec += 1;

                        final int finalsec = sec;
                        final int finalMin = min;
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                progressBar.setProgress((finalsec * 100) / 59);
                                time.setText(String.format("%02d",finalMin) +" : " + String.format("%02d",(60 -finalsec)));
                            }
                        });
                        try {
                            // Sleep for 200 milliseconds.
                            // Just to display the progress slowly
                            Thread.sleep(1000); //thread will take approx 3 seconds to finish
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    sec = 0;
                    min--;
                }

//                setEnabilityOfOptions(false);
                calculateMarks();

            }
        }).start();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            finish();
        }
        return true;
    }
}
