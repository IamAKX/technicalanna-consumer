package com.akashapplications.technicalanna.HelperActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.akashapplications.technicalanna.Adapters.ExamListAdapter;
import com.akashapplications.technicalanna.Models.BoosterModel;
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

public class Boosters extends AppCompatActivity {

    ListView listView;
    ProgressBar progressBar;
    ArrayList<BoosterModel> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boosters);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Boosters");

        listView = findViewById(R.id.listview);
        progressBar = findViewById(R.id.progressbar);
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.blue_button),
                android.graphics.PorterDuff.Mode.SRC_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new GetAllBoosters().execute();
    }

    private class GetAllBoosters extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject reqBody = new JSONObject();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API.ALL_BOOSTER, reqBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (progressBar.getVisibility() == View.VISIBLE)
                                progressBar.setVisibility(View.GONE);
                            ArrayList<String> titleList = new ArrayList<>();
                            Log.e("checking", response.toString());
                            try {
                                JSONArray arr = response.getJSONArray("boosterList");
                                if(arr.length() == 0)
                                    Toast.makeText(getBaseContext(),"No Booster added", Toast.LENGTH_SHORT).show();
                                list.clear();
                                for (int i=0; i<arr.length(); i++)
                                {
                                    BoosterModel m = new BoosterModel();
                                    JSONObject object = arr.getJSONObject(i);
                                    m.setName(object.getString("name"));
                                    m.setId(object.getString("id"));
                                    m.setType(object.getString("type"));
                                    m.setContent(object.getString("content"));
                                    list.add(m);
                                    Log.e(Tokens.LOG,"Adding "+m.getName());
                                    titleList.add(m.getName());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            ArrayAdapter adapter = new ArrayAdapter<String>(Boosters.this,
                                    android.R.layout.simple_list_item_1, titleList);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    startActivity(new Intent(getBaseContext(), BoosterContent.class)
                                            .putExtra("type",list.get(position).getType())
                                            .putExtra("content",list.get(position).getContent()));
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
