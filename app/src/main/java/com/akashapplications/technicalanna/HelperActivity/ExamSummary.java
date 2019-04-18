package com.akashapplications.technicalanna.HelperActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
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

        double percentE = getIntent().getDoubleExtra("percent",0);
        int totalE = getIntent().getIntExtra("total",0);
        int skippedE = getIntent().getIntExtra("skipped",0);
        int correctE = getIntent().getIntExtra("correct",0);
        int incorrectE = getIntent().getIntExtra("incorrect",0);
        double score = getIntent().getDoubleExtra("marks",0.0);

        percent.setText(String.format("%.2f",percentE) + " %");
        marks.setText("Scored "+String.format("%.2f",score)+" out of "+totalE);
        correct.setText(correctE + " correct answers");
        skipped.setText(skippedE + " questions skipped");
        incorrect.setText(incorrectE + " incorrect answers");

        findViewById(R.id.finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.soln).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),QuizSolution.class)
                        .putExtra("exammodel",getIntent().getSerializableExtra("exammodel"))
                        .putExtra("correctans",getIntent().getIntArrayExtra("correctans"))
                );
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            finish();
        }
        return true;
    }
}
