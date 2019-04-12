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
import com.akashapplications.technicalanna.Login;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordRecoverySecond extends Activity {
    EditText password;
    ProgressBar progressBar;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery_second);

        password = findViewById(R.id.password);

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
                if(password.getText().toString().length() >= 8)
                    new UpdatePassword().execute();
                else
                    password.setError("Min character is 8");

            }
        });
    }


    private class UpdatePassword extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject reqBody = new JSONObject();
            try {
                reqBody.put("email", getIntent().getStringExtra("email"));
                reqBody.put("password",password.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API.UPDATE_PASSWORD, reqBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e(Tokens.LOG, response.toString());
                            try {
                                Toast.makeText(getBaseContext(), response.getString("res"), Toast.LENGTH_SHORT).show();
                                new UserData(getBaseContext()).logOut();
                                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                        new ResultCallback<com.google.android.gms.common.api.Status>() {
                                            @Override
                                            public void onResult(com.google.android.gms.common.api.Status status) {

                                            }
                                        });
                                startActivity(new Intent(getBaseContext(), Login.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
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

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();

    }
}
