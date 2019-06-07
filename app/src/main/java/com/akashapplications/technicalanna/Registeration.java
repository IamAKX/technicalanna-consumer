package com.akashapplications.technicalanna;

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
import com.akashapplications.technicalanna.UserProfile.EmailVerification;
import com.akashapplications.technicalanna.UserProfile.PhoneVerification;
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
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registeration extends Activity {

    EditText name, email, password, phone;
    LovelyProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        name = findViewById(R.id.loginUsername);
        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.loginPassword);
        phone = findViewById(R.id.registerPhone);

        progressDialog = new LovelyProgressDialog(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Please wait")
                .setMessage("Sit back and relax while we create your account")
                .setTopColorRes(R.color.blue_button);

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), Login.class));
                finish();
            }
        });

        findViewById(R.id.startRegisterationProcessBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    if (!InternetConnectivity.isNetworkAvailable(getBaseContext())) {
                        InternetConnectivity.showPrompt(getBaseContext());
                        return;
                    }

                    new RegisterUser().execute();
                }
            }
        });

    }

    private boolean validateInput() {
        boolean isValid = true;
        String emai_regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        String phone_regex = "\\d{10}";

        if (name.getText().toString().length() == 0) {
            name.setError("Enter name");
            return false;
        }

        Pattern pattern = Pattern.compile(emai_regex);
        Matcher matcher = pattern.matcher(email.getText().toString());
        if (!matcher.matches()) {
            email.setError("Invalid email");
            return false;
        }


        if (password.getText().toString().length() < 8) {
            password.setError("Min 8 characters");
            return false;
        }

        pattern = Pattern.compile(phone_regex);
        matcher = pattern.matcher(phone.getText().toString());
        if (!matcher.matches()) {
            phone.setError("Invalid phone");
            return false;
        }
        return isValid;
    }

    private class RegisterUser extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject reqBody = new JSONObject();
            try {
                reqBody.put("name", name.getText().toString());
                reqBody.put("email", email.getText().toString());
                reqBody.put("password",password.getText().toString());
                reqBody.put("phone",phone.getText().toString());
                reqBody.put("type","email");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API.REGISTER, reqBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();

                            Log.e(Tokens.LOG, response.toString());

                            try {
                                String userID = response.getString("user_id");
                                UserData data = new UserData(getBaseContext());
                                data.setUserID(userID);
                                data.setEmail(email.getText().toString());
                                data.setEmailVerified(false);
                                data.setPhone(phone.getText().toString());
                                data.setPhoneVerified(false);
                                data.setName(name.getText().toString());
                                data.setLoggedIn(false);
                                Toast.makeText(getBaseContext(), response.getString("res"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            startActivity(new Intent(getBaseContext(), EmailVerification.class));

                            startActivity(new Intent(getBaseContext(), PhoneVerification.class));
                            finish();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    NetworkResponse networkResponse = error.networkResponse;
                    if(error.networkResponse != null && new String(networkResponse.data) != null) {
                        if(new String(networkResponse.data) != null)
                        {
                            try {
                                JSONObject object = new JSONObject(new String(networkResponse.data));
                                Toast.makeText(getBaseContext(), object.getString("res") , Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
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
