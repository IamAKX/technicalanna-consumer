package com.akashapplications.technicalanna.Test;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.akashapplications.technicalanna.R;
import com.akashapplications.technicalanna.SubMenuActivity.FullLengthExams.FullLengthAllExams;
import com.akashapplications.technicalanna.SubMenuActivity.FullLengthExams.FullLengthNotification;
import com.akashapplications.technicalanna.SubMenuActivity.FullLengthExams.FullLengthPrevYearQuestionPaper;
import com.akashapplications.technicalanna.SubMenuActivity.SubjectWiseExam.SubjectAllExam;
import com.akashapplications.technicalanna.Utils.API;
import com.akashapplications.technicalanna.Utils.RequestQueueSingleton;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

        String itemArray[] = {"Notification", "Syllabus", "Previous Year question Paper", "Full Length Tests"};

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
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                startActivity(new Intent(getBaseContext(), FullLengthNotification.class).putExtra("subject", subject));
                break;

            case 1:
                new FetchSyllabus().execute();
                break;

            case 2:
                startActivity(new Intent(getBaseContext(), FullLengthPrevYearQuestionPaper.class).putExtra("subject", subject));
                break;

            case 3:
                startActivity(new Intent(getBaseContext(), FullLengthAllExams.class).putExtra("subject", subject));
                break;
        }
    }



    private class FetchSyllabus extends AsyncTask<Void,Void,Void> {
        LovelyProgressDialog progressDialog = new LovelyProgressDialog(TestContent.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog
                    .setIcon(R.drawable.update)
                    .setTitle("Please wait...")
                    .setCancelable(false)
                    .setMessage("Redirecting to Syllabus page")
                    .setTopColorRes(R.color.blue_button);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject reqBody = new JSONObject();
            try {
                reqBody.put("subject",subject);
                reqBody.put("name","syllabus");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API.FULL_EXAM_GET_DETAIL_BY_NAME, reqBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();
                            if(response.has("res")) {
                                try {
                                    response = response.getJSONObject("res");
                                    Toast.makeText(getBaseContext(),"Redirecting...",Toast.LENGTH_LONG).show();
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(response.getString("syllabus")));
                                    startActivity(browserIntent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                Toast.makeText(getBaseContext(),"Syllabus not added by admin",Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
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
    }
}
