package com.akashapplications.technicalanna.HelperActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.akashapplications.technicalanna.LocalData.UserData;
import com.akashapplications.technicalanna.MainContainer;
import com.akashapplications.technicalanna.Models.SubjectExamsModel;
import com.akashapplications.technicalanna.PersonalMenu.Profile;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONException;
import org.json.JSONObject;

import mehdi.sakout.fancybuttons.FancyButton;

public class ExamPreview extends AppCompatActivity {
    SubjectExamsModel examsModel;
    TextView name, subject,full_marks, fees, time, count;
    FancyButton go;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_preview);

        examsModel = (SubjectExamsModel) getIntent().getSerializableExtra("exam");
        name = findViewById(R.id.name);
        subject = findViewById(R.id.subject);
        full_marks = findViewById(R.id.marks);
        fees = findViewById(R.id.fees);
        time = findViewById(R.id.time);
        count = findViewById(R.id.count);
        go = findViewById(R.id.start);


        progressBar = findViewById(R.id.progressbar);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.blue_button),
                android.graphics.PorterDuff.Mode.SRC_IN);

        progressBar.setVisibility(View.GONE);
        progressBar.bringToFront();

        name.setText(examsModel.getName().toUpperCase());
        subject.setText(examsModel.getSubject());
        full_marks.setText(String.valueOf(examsModel.getFullMarks())+ " score");
        fees.setText("Rs. " + examsModel.getFees());
        time.setText(String.valueOf(examsModel.getTimeAlloted())+" Minutes");
        count.setText(String.valueOf(examsModel.getQuizList().size())+ " MCQs");

        Log.e(Tokens.LOG,examsModel.getRegisteredUser().toString());

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (examsModel.getFees() == 0) {
                        startActivity(new Intent(getBaseContext(), Quiz.class).putExtra("exam", examsModel));
                        finish();
                    } else {
                        // TODO : check for exam fee payment
                        new CheckSubscribtion().execute();
                    }

            }
        });
    }

    private class CheckSubscribtion extends AsyncTask<Void,Void,Void> {
        LovelyProgressDialog progressDialog = new LovelyProgressDialog(ExamPreview.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog
                    .setIcon(R.drawable.update)
                    .setTitle("Please wait...")
                    .setCancelable(false)
                    .setMessage("Checking your subscription for this exam")
                    .setTopColorRes(R.color.blue_button);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            final JSONObject reqBody = new JSONObject();
            try {
                reqBody.put("subject", examsModel.getSubject());
                reqBody.put("email", new UserData(getBaseContext()).getEmail());
                reqBody.put("name",examsModel.getName());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e(Tokens.LOG,reqBody.toString());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API.FULL_EXAM_CHECK_SUBSCRIBTION, reqBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();

                            Log.e(Tokens.LOG, response.toString());

                            try {
                                boolean res = response.getBoolean("res");
                                if (res)
                                {
                                    startActivity(new Intent(getBaseContext(), Quiz.class).putExtra("exam", examsModel));
                                    finish();
                                }
                                else
                                {
                                    triggerForPayment();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

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

    private void triggerForPayment() {
        new LovelyStandardDialog(ExamPreview.this, R.style.TintTheme)
                .setTopColorRes(R.color.blue_button)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Paid Exam")
                .setCancelable(false)
                .setMessage("You are not registered for the exam. Please pay the examination fees")
                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Check wallet balance

                        new CheckWalletBalance().execute();

                    }
                })
                .setNegativeButton(android.R.string.cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // dismiss();
                    }
                })
                .show();
    }


    private class CheckWalletBalance extends AsyncTask<Void,Void,Void> {
        LovelyProgressDialog progressDialog = new LovelyProgressDialog(ExamPreview.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog
                    .setIcon(R.drawable.update)
                    .setTitle("Please wait...")
                    .setCancelable(false)
                    .setMessage("We are fetching your wallet balance")
                    .setTopColorRes(R.color.blue_button);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            final JSONObject reqBody = new JSONObject();
            try {
                reqBody.put("email", new UserData(getBaseContext()).getEmail());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e(Tokens.LOG,reqBody.toString());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API.GET_WALLET_DETAIL, reqBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();

                            Log.e(Tokens.LOG, response.toString());
                            double amount = 0.00;
                            try {
                                if(response.has("res"))
                                {
                                    response = response.getJSONObject("res");
                                    if(response.has("wallet_amt"))
                                        amount = response.getDouble("wallet_amt");
                                    else
                                        amount = 0.00;
                                }
                                else
                                {
                                    amount = 0.00;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(amount < examsModel.getFees())
                            {
                                // trigger to add money to wallet
                                startActivity(new Intent(getBaseContext(),Profile.class));
                            }
                            else
                            {
                                // trigger to buy
                                new Debit(examsModel.getFees()).execute();
                            }

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

    private class Debit extends AsyncTask<Void,Void,Void>{
        String tamt;

        public Debit(int tamtt) {
            this.tamt = String.valueOf(tamtt);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject reqBody = new JSONObject();
            try {
                reqBody.put("email",new UserData(getBaseContext()).getEmail());
                reqBody.put("amt",Double.parseDouble(tamt));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API.DEBIT_WALLET, reqBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (progressBar.getVisibility() == View.VISIBLE)
                                progressBar.setVisibility(View.GONE);
                            Log.e("checking", response.toString());
                            try {
                                boolean res = response.has("res") ? response.getBoolean("res") : false;
                                if(res){
                                    new RegisterForExam().execute();
                                }
                                else
                                {
                                    Toast.makeText(getBaseContext(), "failed to update wallet amount", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (progressBar.getVisibility() == View.VISIBLE)
                        progressBar.setVisibility(View.GONE);
                    NetworkResponse networkResponse = error.networkResponse;
                    Toast.makeText(getBaseContext(), "failed to update wallet amount", Toast.LENGTH_SHORT).show();
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

    private class RegisterForExam extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject reqBody = new JSONObject();
            try {
                reqBody.put("email",new UserData(getBaseContext()).getEmail());
                reqBody.put("subject",examsModel.getSubject());
                reqBody.put("name",examsModel.getName());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API.FULL_EXAM_ADD_SUBSCRIBTION, reqBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (progressBar.getVisibility() == View.VISIBLE)
                                progressBar.setVisibility(View.GONE);
                            Log.e("checking", response.toString());
                            startActivity(new Intent(getBaseContext(), Quiz.class).putExtra("exam", examsModel));
                            finish();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (progressBar.getVisibility() == View.VISIBLE)
                        progressBar.setVisibility(View.GONE);
                    NetworkResponse networkResponse = error.networkResponse;
                    Toast.makeText(getBaseContext(), "failed to update wallet amount", Toast.LENGTH_SHORT).show();
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
