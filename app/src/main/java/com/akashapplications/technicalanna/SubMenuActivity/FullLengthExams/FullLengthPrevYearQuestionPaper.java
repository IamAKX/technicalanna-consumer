package com.akashapplications.technicalanna.SubMenuActivity.FullLengthExams;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.akashapplications.technicalanna.HelperActivity.PDFReader;
import com.akashapplications.technicalanna.Models.PrevQPaperModel;
import com.akashapplications.technicalanna.R;
import com.akashapplications.technicalanna.Utils.API;
import com.akashapplications.technicalanna.Utils.RequestQueueSingleton;
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

public class FullLengthPrevYearQuestionPaper extends AppCompatActivity {
    ListView listView;
    ProgressBar progressBar;
    static String subject = "null";
    ArrayList<PrevQPaperModel> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_length_prev_year_question_paper);
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
        new GetPrevQuestionPaper().execute();

    }

    private class GetPrevQuestionPaper extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject reqBody = new JSONObject();
            try {
                reqBody.put("subject",subject);
                reqBody.put("name","previousQuestionPaper");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API.FULL_EXAM_GET_DETAIL_BY_NAME, reqBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (progressBar.getVisibility() == View.VISIBLE)
                                progressBar.setVisibility(View.GONE);
                            ArrayList<String> titleList = new ArrayList<>();
                            if(response.has("res")) {

                                try {
                                    response = response.getJSONObject("res");
                                    JSONArray arr = response.getJSONArray("previousQuestionPaper");
                                    for (int i = 0; i < arr.length(); i++) {

                                        JSONObject obj = arr.getJSONObject(i);
                                        PrevQPaperModel m = new PrevQPaperModel();
                                        m.setYear(obj.getString("year"));
                                        m.setLink(obj.getString("link"));
                                        list.add(m);
                                        titleList.add(m.getYear());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            ArrayAdapter adapter = new ArrayAdapter<String>(FullLengthPrevYearQuestionPaper.this,
                                    android.R.layout.simple_list_item_1, titleList);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                    startActivity(new Intent(getBaseContext(), PDFReader.class).putExtra("link",list.get(position).getLink() ));
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).getLink())));

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
}
