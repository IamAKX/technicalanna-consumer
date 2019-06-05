package com.akashapplications.technicalanna.UserProfile;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.akashapplications.technicalanna.LocalData.EmailOTP;
import com.akashapplications.technicalanna.LocalData.UserData;
import com.akashapplications.technicalanna.MainContainer;
import com.akashapplications.technicalanna.R;
import com.akashapplications.technicalanna.Utils.API;
import com.akashapplications.technicalanna.Utils.Dummy;
import com.akashapplications.technicalanna.Utils.RequestQueueSingleton;
import com.akashapplications.technicalanna.Utils.Tokens;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import org.json.JSONException;
import org.json.JSONObject;

public class EmailVerification extends Activity implements OnOtpCompletionListener {

    private OtpView otpView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.blue_button),
                android.graphics.PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.GONE);

        otpView = findViewById(R.id.otp_view);
        otpView.setItemRadius(5);
        otpView.setAnimationEnable(true);
        otpView.setCursorVisible(false);
        otpView.setOtpCompletionListener(this);

        int pin = Dummy.generateRandomNumberBetween(100000,999999);
        new TriggerEmail(pin).execute();
    }

    @Override
    public void onOtpCompleted(String otp) {
        progressBar.setVisibility(View.VISIBLE);
        EmailOTP emailOTP = new EmailOTP(getBaseContext());
        if(emailOTP.isPinValid() & otp.equalsIgnoreCase(emailOTP.getPin()))
            new VerifyEmail().execute();

    }

    private class VerifyEmail extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject reqBody = new JSONObject();
            try {
                reqBody.put("email", new UserData(getBaseContext()).getEmail());
                reqBody.put("isEmailVerified",true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API.SET_EMAIL_VERIFIED, reqBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                new UserData(getBaseContext()).setPhoneVerified(true);
                                Toast.makeText(getBaseContext(), response.getString("res"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            startActivity(new Intent(getBaseContext(), MainContainer.class));
                            finish();
//                            Toast.makeText(getBaseContext(),"Email sent. Please check your INBOX / SPAM to get the OTP",Toast.LENGTH_SHORT).show();
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

    private class TriggerEmail extends AsyncTask<Void,Void,Void>{
        int pin;

        public TriggerEmail(int pin) {
            this.pin = pin;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject reqBody = new JSONObject();
            try {
                reqBody.put("email", new UserData(getBaseContext()).getEmail());
                reqBody.put("otp",pin);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(Tokens.LOG,reqBody.toString());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API.TRIGGER_EMAIL, reqBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e(Tokens.LOG,response.toString());
                            new EmailOTP(getBaseContext()).setPin(String.valueOf(pin));
                            Toast.makeText(getBaseContext(),"Email sent. Please check your INBOX / SPAM to get the OTP",Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    Log.i("checking",error.toString());
                    Toast.makeText(getBaseContext(), "Failed", Toast.LENGTH_SHORT).show();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            finish();
        }
        return true;
    }
}
