package com.akashapplications.technicalanna.SubMenuActivity.SubjectWiseExam;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.akashapplications.technicalanna.Adapters.ExamListAdapter;
import com.akashapplications.technicalanna.HelperActivity.ExamPreview;
import com.akashapplications.technicalanna.Models.QuizModel;
import com.akashapplications.technicalanna.Models.SubjectExamsModel;
import com.akashapplications.technicalanna.R;
import com.akashapplications.technicalanna.Utils.API;
import com.akashapplications.technicalanna.Utils.RequestQueueSingleton;
import com.akashapplications.technicalanna.Utils.Tokens;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SubjectAllExam extends AppCompatActivity {

    ListView listView;
    ProgressBar progressBar;
    static String subject = "null";
    ArrayList<SubjectExamsModel> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_all_exam);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        subject = getIntent().getStringExtra("subject");
        getSupportActionBar().setTitle(subject);

        listView = findViewById(R.id.listview);
        progressBar = findViewById(R.id.progressbar);
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.blue_button),
                android.graphics.PorterDuff.Mode.SRC_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new GetAllExams().execute();

    }



    private class GetAllExams extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject reqBody = new JSONObject();
            try {
                reqBody.put("subject",subject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API.ALL_SUBJECT_EXAM, reqBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (progressBar.getVisibility() == View.VISIBLE)
                                progressBar.setVisibility(View.GONE);
                            ArrayList<String> titleList = new ArrayList<>();
                            Log.e("checking", response.toString());
                            try {
                                JSONArray arr = response.getJSONArray("examList");
                                if(arr.length() == 0)
                                    Toast.makeText(getBaseContext(),"No Exam added", Toast.LENGTH_SHORT).show();
                                list.clear();
                                for (int i=0; i<arr.length(); i++)
                                {
                                    SubjectExamsModel m = new SubjectExamsModel();
                                    JSONObject object = arr.getJSONObject(i);
                                    if(!object.has("name"))
                                        continue;
                                    m.setFees(0);
                                    m.setTimeAlloted(object.getInt("time_alloted"));
                                    m.setSubject(object.getString("subject"));
                                    m.setFullMarks(object.getInt("full_marks"));
                                    m.setName(object.getString("name"));
                                    m.setId(object.getString("id"));
                                    m.setNegMark(object.getDouble("negmark"));

                                    ArrayList<QuizModel> qm = new ArrayList<>();

                                    JSONArray qArr = object.getJSONArray("quiz");
                                    for (int j=0; j<qArr.length(); j++)
                                    {
                                        QuizModel qmodel = new QuizModel();
                                        JSONObject qObject = qArr.getJSONObject(j);
                                        qmodel.setSolution(qObject.getString("solution"));
                                        qmodel.setQuestion(qObject.getString("question"));
                                        JSONArray ans = qObject.getJSONArray("answer");
                                        qmodel.setOption1(ans.getString(0));
                                        qmodel.setOption2(ans.getString(1));
                                        qmodel.setOption3(ans.getString(2));
                                        qmodel.setOption4(ans.getString(3));
                                        qmodel.setCorrectOption(qObject.getInt("correct"));

                                        qm.add(qmodel);
                                    }

                                    m.setQuizList(qm);
                                    m.setRegisteredUser(new ArrayList<String>());

                                    list.add(m);
                                    Log.e(Tokens.LOG,"Adding "+m.getName());
                                    titleList.add(m.getName());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            ExamListAdapter adapter = new ExamListAdapter(getBaseContext(),list);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    startActivity(new Intent(getBaseContext(), ExamPreview.class).putExtra("exam",list.get(position)));
                                }
                            });


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (progressBar.getVisibility() == View.VISIBLE)
                        progressBar.setVisibility(View.GONE);
                    NetworkResponse networkResponse = error.networkResponse;
                    Toast.makeText(getBaseContext(), "failed " + new String(networkResponse.data), Toast.LENGTH_SHORT).show();
                }
            });

            jsonObjectRequest.setShouldCache(false);
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            RequestQueue requestQueue = RequestQueueSingleton.getInstance(getBaseContext())
                    .getRequestQueue();
            requestQueue.getCache().clear();
            requestQueue.add(jsonObjectRequest);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            finish();
        }
        return true;
    }
}
