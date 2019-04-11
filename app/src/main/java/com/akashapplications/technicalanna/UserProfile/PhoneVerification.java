package com.akashapplications.technicalanna.UserProfile;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.akashapplications.technicalanna.AppIntro;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class PhoneVerification extends Activity {

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.blue_button),
                android.graphics.PorterDuff.Mode.SRC_IN);


        setCallBack();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+ new UserData(getBaseContext()).getPhone(),        // Phone number to verify
                120,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void setCallBack() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                if(!InternetConnectivity.isNetworkAvailable(getBaseContext()))
                {
                    InternetConnectivity.showPrompt(getBaseContext());
                    return;
                }

                new VerifyPhone().execute();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.i(Tokens.LOG,"code : "+e);
                if(e instanceof FirebaseAuthInvalidCredentialsException)
                {
                    Toast.makeText(getBaseContext(),"Wrong OTP",Toast.LENGTH_SHORT).show();
                }
                else if(e instanceof FirebaseTooManyRequestsException)
                {
                    Toast.makeText(getBaseContext(),"SMS Quota exceeded",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Log.i(Tokens.LOG,"code : "+s);
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }
        };
    }

    private class VerifyPhone extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject reqBody = new JSONObject();
            try {
                reqBody.put("email", new UserData(getBaseContext()).getEmail());
                reqBody.put("isPhoneVerified",true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API.SET_PHONE_VERIFIED, reqBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e(Tokens.LOG, response.toString());
                            try {
                                new UserData(getBaseContext()).setPhoneVerified(true);
                                Toast.makeText(getBaseContext(), response.getString("res"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            startActivity(new Intent(getBaseContext(), EmailVerification.class));
                            finish();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    Toast.makeText(getBaseContext(), "Registration failed " + new String(networkResponse.data), Toast.LENGTH_SHORT).show();
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
