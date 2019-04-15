package com.akashapplications.technicalanna.HelperActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.akashapplications.technicalanna.R;

public class ExamSummary extends AppCompatActivity {

    TextView percent, marks, correct, incorrect,skipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_summary);
        percent = findViewById(R.id.percent);
        marks = findViewById(R.id.mark);
        skipped = findViewById(R.id.skipped);
        incorrect = findViewById(R.id.incorrect);
        correct = findViewById(R.id.correct);

        float percentE = getIntent().getFloatExtra("percent",0);
        int totalE = getIntent().getIntExtra("total",0);
        int skippedE = getIntent().getIntExtra("skipped",0);
        int correctE = getIntent().getIntExtra("correct",0);
        int incorrectE = getIntent().getIntExtra("incorrect",0);
        int score = getIntent().getIntExtra("marks",0);

        percent.setText(String.format("%.2f",percentE) + " %");
        marks.setText("Scored "+score+" out of "+totalE);
        correct.setText(correctE + " correct answers");
        skipped.setText(skippedE + " questions skipped");
        incorrect.setText(incorrectE + " incorrect answers");

        findViewById(R.id.finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
