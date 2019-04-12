package com.akashapplications.technicalanna.UserProfile;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.akashapplications.technicalanna.LocalData.UserData;
import com.akashapplications.technicalanna.R;
import com.akashapplications.technicalanna.Utils.API;
import com.akashapplications.technicalanna.Utils.InternetConnectivity;
import com.akashapplications.technicalanna.Utils.RequestQueueSingleton;
import com.akashapplications.technicalanna.Utils.Tokens;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class PasswordRecoveryFirst extends Activity {
    EditText email;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery_first);
        email = findViewById(R.id.email);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.blue_button),
                android.graphics.PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.GONE);

        findViewById(R.id.proceed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!InternetConnectivity.isNetworkAvailable(getBaseContext()))
                {
                    InternetConnectivity.showPrompt(getBaseContext());
                    return;
                }

                new GetUserDetail().execute();
            }
        });
    }


    private class GetUserDetail extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject reqBody = new JSONObject();
            try {
                reqBody.put("email",email.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API.GET_PROFILE_DETAIL, reqBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressBar.setVisibility(View.GONE);
                            String phone = "";
                            Log.e(Tokens.LOG, response.toString());
                            try {

                                if (response.has("phone"))
                                {
                                    phone = response.getString("phone");
                                    startActivity(new Intent(getBaseContext(),PhoneVerification.class).putExtra("phone",phone).putExtra("email",email.getText().toString()));
                                }
                                else
                                    Toast.makeText(getBaseContext(),"Could not fetch linked phone number",Toast.LENGTH_LONG).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);
                    NetworkResponse networkResponse = error.networkResponse;
                    Toast.makeText(getBaseContext(), new String(networkResponse.data), Toast.LENGTH_SHORT).show();
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
