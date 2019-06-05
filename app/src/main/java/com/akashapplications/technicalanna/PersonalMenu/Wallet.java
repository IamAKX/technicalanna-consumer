package com.akashapplications.technicalanna.PersonalMenu;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.akashapplications.technicalanna.LocalData.UserData;
import com.akashapplications.technicalanna.Models.PaytmModel;
import com.akashapplications.technicalanna.R;
import com.akashapplications.technicalanna.Utils.API;
import com.akashapplications.technicalanna.Utils.JSONParser;
import com.akashapplications.technicalanna.Utils.RequestQueueSingleton;
import com.akashapplications.technicalanna.Utils.Tokens;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Wallet extends AppCompatActivity {
    final int UPI_PAYMENT = 0;
    ProgressBar progressBar;
    TextView rupee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Wallet");
        rupee = findViewById(R.id.rupee);

        findViewById(R.id.upi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerAmountInpoutForm("upi");
            }
        });

        findViewById(R.id.paytm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerAmountInpoutForm("paytm");
            }
        });

//        Dexter.withActivity(Wallet.this)
//                .withPermissions(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS)
//                .withListener(new BaseMultiplePermissionsListener())
//                .check();
        progressBar = findViewById(R.id.progressbar);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.blue_button),
                android.graphics.PorterDuff.Mode.SRC_IN);

        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        new FetchWallet().execute();
    }

    private void triggerAmountInpoutForm(final String payMethod) {
        new LovelyTextInputDialog(this, R.style.TintTheme)
                .setTopColorRes(R.color.blue_button)
                .setTitle("Enter the Amount")
                .setMessage("Amount will be credited in your wallet")
                .setIcon(R.drawable.ic_money_bag_with_dollar_symbol)
                .setInputType(InputType.TYPE_CLASS_PHONE)
                .setInputFilter("Enter a valid amount", new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        String PATTERN = "[0-9]*\\.?[0-9]*";
                        Pattern pattern = Pattern.compile(PATTERN);
                        Matcher matcher = pattern.matcher(text);
                        return matcher.matches();
                    }
                })
                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        try {
                            double amt = Double.parseDouble(text);
                            if (amt > 0) {
                                if (payMethod.equalsIgnoreCase("upi"))
                                    triggerUPIintent(amt);
                                else
                                    processPaytm(amt);
                            } else
                                Toast.makeText(getBaseContext(), "You can not add Rs. 0 ", Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            Log.e(Tokens.LOG,"ERR : "+e.getMessage());
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                .setCancelable(false)
                .show();
    }


    private void triggerUPIintent(double amt) {
        String payeeAddress="akx.sonu@oksbi";
        String payeeName = new UserData(getBaseContext()).getName();
        String trxnRefId = System.currentTimeMillis()/1000 + "UPI";
        String trxnNote = "Paying for TA";
        String payeeAmount = String.valueOf(amt);
        String currencyCode = "INR";

        String refUrl ="www.akashapplication.com";
        String UPI = "upi://pay?pa=" + payeeAddress + "&pn=" + payeeName
                + "&tr=" + trxnRefId
                + "&tn=" + trxnNote + "&am=" + payeeAmount + "&cu=" + currencyCode
                + "&refUrl=" + refUrl + "&mode=04&mc=9804&orgid=000000&url=https://mystar.com/orderid="+trxnRefId;
//        Log.e(Tokens.LOG, "Amonut : " + amt);
//        Uri uri = Uri.parse("upi://pay").buildUpon()
//                .appendQueryParameter("pa", "akx.sonu@oksbi")
//                .appendQueryParameter("pn", "Akash_Giri")
//                .appendQueryParameter("tn", "Added_to_TA_wallet")
//                .appendQueryParameter("am", String.valueOf(amt))
//                .appendQueryParameter("cu", "INR")
//                .build();
        UPI = UPI.replace(" ", "+");

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(UPI));
        Intent chooser = Intent.createChooser(intent, "Pay with...");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivityForResult(chooser, 999, null);
        }
//        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
//        upiPayIntent.setData(uri);
//
//        // will always show a dialog to user to choose an app
//        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
//        if (null != chooser.resolveActivity(getPackageManager())) {
//            startActivityForResult(chooser, 999);
//        } else {
//            Toast.makeText(Wallet.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 999:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d(Tokens.LOG, "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d(Tokens.LOG, "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d(Tokens.LOG, "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(Wallet.this)) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(Wallet.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.d(Tokens.LOG, "responseStr: " + approvalRefNo);
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(Wallet.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Wallet.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(Wallet.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }


    // -------------------------- PAYTM ---------------------------------------

    protected String generateString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    private void processPaytm(double amt) {

        String custID = generateString();
        String orderID = generateString();
        String callBack = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";


        PaytmModel paytmModel = new PaytmModel();
        paytmModel.setmId("QcWoyS72356675836968");
        paytmModel.setChannelId("WAP");
        paytmModel.setTxnAmount(String.valueOf(amt));
//        paytmModel.setWebsite("WEBSTAGING");
        paytmModel.setWebsite("DEFAULT");
        paytmModel.setCallBackUrl(callBack);
        paytmModel.setIndustryTypeId("Retail");
        paytmModel.setOrderId(orderID);
        paytmModel.setCustId(custID);

        new GenerateChecksum(paytmModel).execute();
//        generateCheckSum(paytmModel);
    }

    String CHECKSUMHASH = "";

    private void generateCheckSum(final PaytmModel model) {
        Log.i(Tokens.LOG, "generate checksum start");

        String url = "technicalanna.000webhostapp.com/paytm/paytm/generateChecksum.php";
//        String varifyurl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        Log.i(Tokens.LOG, "res : "+response);
                        try {
                            JSONObject object = new JSONObject(response);
                            CHECKSUMHASH = object.has("CHECKSUMHASH")?object.getString("CHECKSUMHASH"):"";
                            startPayment(CHECKSUMHASH,model);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i(Tokens.LOG, e.getMessage() + "----------ERROR---------------");

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want

                        NetworkResponse networkResponse = error.networkResponse;
                        Log.i(Tokens.LOG, error.getMessage() + "----------ERROR---------------");

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("MID",model.getmId());
                params.put("ORDER_ID",model.getOrderId());
                params.put("CHANNEL_ID",model.getChannelId());
                params.put("CUST_ID",model.getCustId());
                params.put("TXN_AMOUNT",model.getTxnAmount());
                params.put("WEBSITE",model.getWebsite());
                params.put("INDUSTRY_TYPE_ID",model.getIndustryTypeId());
                params.put("CALLBACK_URL",model.getCallBackUrl());
                return params;
            }
        };

        //Adding the string request to the queue
        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        requestQueue.getCache().clear();

        requestQueue.add(stringRequest);
    }


    private void startPayment(String checksum, final PaytmModel model) {

        PaytmPGService Service = PaytmPGService.getProductionService();
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("MID", model.getmId());
        // Key in your staging and production MID available in your dashboard
        paramMap.put("ORDER_ID", model.getOrderId());
        paramMap.put("CUST_ID", model.getCustId());
        paramMap.put("CHANNEL_ID", model.getChannelId());
        paramMap.put("TXN_AMOUNT", model.getTxnAmount());
        paramMap.put("WEBSITE", model.getWebsite());
        // This is the staging value. Production value is available in your dashboard
        paramMap.put("INDUSTRY_TYPE_ID", model.getIndustryTypeId());
        // This is the staging value. Production value is available in your dashboard
        paramMap.put("CHECKSUMHASH", checksum);
        paramMap.put("CALLBACK_URL", model.getCallBackUrl());

        Log.e(Tokens.LOG, paramMap.toString());
        PaytmOrder Order = new PaytmOrder(paramMap);

        Service.initialize(Order, null);
        Service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {
            /*Call Backs*/
            public void someUIErrorOccurred(String inErrorMessage) {
                Toast.makeText(getBaseContext(), inErrorMessage, Toast.LENGTH_LONG).show();
                Log.e(Tokens.LOG+" - paytm", inErrorMessage);

            }

            public void onTransactionResponse(Bundle inResponse) {
                Log.e(Tokens.LOG+" - paytm", inResponse.toString());
                if(!inResponse.toString().contains("TXN_FAILURE")) {
                    Toast.makeText(getBaseContext(), inResponse.toString(), Toast.LENGTH_LONG).show();
                    new Credit(model.getTxnAmount()).execute();
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Transaction Failed", Toast.LENGTH_LONG).show();
                }
            }

            public void networkNotAvailable() {

                Toast.makeText(getBaseContext(), "Network not available", Toast.LENGTH_LONG).show();

            }

            public void clientAuthenticationFailed(String inErrorMessage) {
                Log.e(Tokens.LOG+" - paytm", inErrorMessage);

                Toast.makeText(getBaseContext(), inErrorMessage, Toast.LENGTH_LONG).show();

            }

            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                Toast.makeText(getBaseContext(), inErrorMessage, Toast.LENGTH_LONG).show();
                Log.e(Tokens.LOG+" - paytm", inErrorMessage);

            }

            public void onBackPressedCancelTransaction() {
                Toast.makeText(getBaseContext(), "Transaction cancel", Toast.LENGTH_LONG).show();

            }

            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                Toast.makeText(getBaseContext(), inErrorMessage, Toast.LENGTH_LONG).show();
                Log.e(Tokens.LOG+" - paytm", inErrorMessage);

            }
        });

    }

    private class GenerateChecksum extends AsyncTask<String,String,String>{
        PaytmModel model;
        String url = "http://technicalanna.000webhostapp.com/paytm/paytm/generateChecksum.php";
        String varifyurl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";
        String CHECKSUMHASH = "";
        public GenerateChecksum(PaytmModel model) {
            this.model = model;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);

                            //If we are getting success from server
                            Log.i(Tokens.LOG+" - paytm", response);
                            try {
                                JSONObject object = new JSONObject(response);
                                CHECKSUMHASH = object.has("CHECKSUMHASH")?object.getString("CHECKSUMHASH"):"";
                                Log.i(Tokens.LOG, CHECKSUMHASH);
                                startPayment(CHECKSUMHASH,model);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //You can handle error here if you want
                            progressBar.setVisibility(View.GONE);

                            NetworkResponse networkResponse = error.networkResponse;
                            Log.i(Tokens.LOG, error.getMessage()+" ----------ERROR---------------");

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request
                    params.put("MID",model.getmId());
                    params.put("ORDER_ID",model.getOrderId());
                    params.put("CHANNEL_ID",model.getChannelId());
                    params.put("CUST_ID",model.getCustId());
                    params.put("TXN_AMOUNT",model.getTxnAmount());
                    params.put("WEBSITE",model.getWebsite());
                    params.put("INDUSTRY_TYPE_ID",model.getIndustryTypeId());
                    params.put("CALLBACK_URL",model.getCallBackUrl());
                    return params;
                }
            };

            //Adding the string request to the queue
            stringRequest.setShouldCache(false);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
            requestQueue.getCache().clear();

            requestQueue.add(stringRequest);
            return CHECKSUMHASH;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e(Tokens.LOG,"post :"+CHECKSUMHASH);
            Log.e(Tokens.LOG,"post-s :"+s);

        }

    }

    private class FetchWallet extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
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
                            progressBar.setVisibility(View.GONE);

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

                            rupee.setText(String.format("%.2f", amount));


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);
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

    private class Credit extends AsyncTask<Void,Void,Void>{
        String tamt;

        public Credit(String tamt) {
            this.tamt = tamt;
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
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API.CREDIT_WALLET, reqBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (progressBar.getVisibility() == View.VISIBLE)
                                progressBar.setVisibility(View.GONE);
                            Log.e("checking", response.toString());
                            try {
                                boolean res = response.has("res") ? response.getBoolean("res") : false;
                                if(res){
                                    new FetchWallet().execute();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            finish();
        }
        return true;
    }
}
