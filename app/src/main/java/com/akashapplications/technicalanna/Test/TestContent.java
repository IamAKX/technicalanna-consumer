package com.akashapplications.technicalanna.Test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.akashapplications.technicalanna.R;
import com.akashapplications.technicalanna.SubMenuActivity.FullLengthExams.FullLengthAllExams;
import com.akashapplications.technicalanna.SubMenuActivity.SubjectWiseExam.SubjectAllExam;

public class TestContent extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView listView;
    String subject = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_content);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        subject = getIntent().getStringExtra("subject");
        getSupportActionBar().setTitle(subject);

        listView = findViewById(R.id.listview);

        String itemArray[] = {"Notification","Syllabus","Previous Year question Paper","Full Length Tests"};

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, itemArray);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position)
        {
            case 0:

                break;

            case 1:
                break;

            case 2:
                break;

            case 3:
                startActivity(new Intent(getBaseContext(), FullLengthAllExams.class).putExtra("subject",subject));
                break;
        }
    }
}
