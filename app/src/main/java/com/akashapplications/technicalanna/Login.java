package com.akashapplications.technicalanna;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.akashapplications.technicalanna.LocalData.UserData;
import com.akashapplications.technicalanna.UserProfile.PasswordRecoveryFirst;
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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    ProgressBar progressBar;
    EditText emailET, passwordET;
    private CallbackManager callbackmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailET = findViewById(R.id.loginUsername);
        passwordET = findViewById(R.id.loginPassword);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Tokens.GOOGLE_CLIENT_ID)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        FacebookSdk.setApplicationId(Tokens.FACEBOOK_APP_ID);
        FacebookSdk.sdkInitialize(getApplicationContext());

        progressBar = findViewById(R.id.progressbar);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.blue_button),
                android.graphics.PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.GONE);

        findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), Registeration.class));
                finish();
            }
        });

        findViewById(R.id.loginGo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!InternetConnectivity.isNetworkAvailable(getBaseContext())) {
                    InternetConnectivity.showPrompt(getBaseContext());
                    return;
                }
                new EmailLogin().execute();
            }
        });

        findViewById(R.id.loginGoogleBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!InternetConnectivity.isNetworkAvailable(getBaseContext())) {
                    InternetConnectivity.showPrompt(getBaseContext());
                    return;
                }
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 1001);
            }
        });

        findViewById(R.id.loginFacebookBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithFacebook();
            }
        });

        findViewById(R.id.loginForgetPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), PasswordRecoveryFirst.class));
            }
        });
    }

    private void loginWithFacebook() {

        callbackmanager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackmanager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        Log.d("checking", "facebook:onSuccess:" + loginResult);
                        firebaseAuthWithFacebook(loginResult.getAccessToken());

                    }

                    @Override
                    public void onCancel() {
                        Log.i("checking", "On cancel");
                        if (progressBar != null || progressBar.getVisibility() == View.VISIBLE)
                            progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.i("checking", error.toString());
                        if (progressBar != null || progressBar.getVisibility() == View.VISIBLE)
                            progressBar.setVisibility(View.GONE);
                    }
                });

    }

    private void firebaseAuthWithFacebook(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            Log.e(Tokens.LOG, user.getEmail());
                            try {
                                JSONObject reqBody = new JSONObject();
                                reqBody.put("name", user.getDisplayName());
                                reqBody.put("email", user.getEmail());
                                reqBody.put("password", "");
                                reqBody.put("phone", user.getPhoneNumber());
                                reqBody.put("image", user.getPhotoUrl().toString());
                                reqBody.put("social", "Google");
                                reqBody.put("type", "social");

                                if (user.getPhoneNumber() == null) {
                                    triggerPhoneAlert(user);
                                    if (phoneNumber != "" && phoneNumber.length() == 10)
                                        reqBody.put("phone", phoneNumber);

                                }
                                if (reqBody.getString("phone").length() == 10)
                                    new SocialRegisteration(reqBody).execute();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("checking", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getBaseContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 1001) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.e(Tokens.LOG, e.toString());
            }
        }
        else
            if(requestCode == 1002)
            {
                callbackmanager.onActivityResult(requestCode, resultCode, data);
            }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        progressBar.setVisibility(View.VISIBLE);

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user.getPhoneNumber() == null) {
                                triggerPhoneAlert(user);
                            }
                            else
                            {
                                JSONObject reqBody = new JSONObject();
                                try {
                                    reqBody.put("name", user.getDisplayName());
                                    reqBody.put("email", user.getEmail());
                                    reqBody.put("password", "");
                                    reqBody.put("phone", user.getPhoneNumber());
                                    reqBody.put("image", user.getPhotoUrl().toString());
                                    reqBody.put("social", "Google");
                                    reqBody.put("type", "social");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                new SocialRegisteration(reqBody).execute();
                            }


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getBaseContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });
    }

    String phoneNumber = "";

    private void triggerPhoneAlert(final FirebaseUser user) {
        new LovelyTextInputDialog(this, R.style.TintTheme)
                .setTopColorRes(R.color.blue_button)
                .setTitle("Enter mobile number")
                .setMessage("Could not fetch your phone number, as we failed to do it for you")
                .setIcon(R.drawable.phone_solid)
                .setInputType(InputType.TYPE_CLASS_PHONE)
                .setInputFilter("Invalid phone number", new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        String PATTERN = "\\d{10}";
                        Pattern pattern = Pattern.compile(PATTERN);
                        Matcher matcher = pattern.matcher(text);
                        return matcher.matches();
                    }
                })
                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        JSONObject reqBody = new JSONObject();
                        try {
                            reqBody.put("name", user.getDisplayName());
                            reqBody.put("email", user.getEmail());
                            reqBody.put("password", "");
                            reqBody.put("phone", text);
                            reqBody.put("image", user.getPhotoUrl().toString());
                            reqBody.put("social", "Google");
                            reqBody.put("type", "social");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        new SocialRegisteration(reqBody).execute();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar.setVisibility(View.GONE);
                    }
                })
                .setCancelable(false)
                .show();
    }

    class SocialRegisteration extends AsyncTask<Void, Void, Void> {
        JSONObject reqBody;

        public SocialRegisteration(JSONObject reqBody) {
            this.reqBody = reqBody;
            Log.e(Tokens.LOG, reqBody.toString());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.e(Tokens.LOG, reqBody.toString());

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API.REGISTER, reqBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (progressBar.getVisibility() == View.VISIBLE)
                                progressBar.setVisibility(View.GONE);

                            Log.e(Tokens.LOG, response.toString());

                            try {
                                String userID = response.getString("user_id");
                                UserData data = new UserData(getBaseContext());
                                data.setUserID(userID);
                                data.setEmail(reqBody.getString("email"));
                                data.setEmailVerified(true);
                                data.setPhone(reqBody.getString("phone"));
                                data.setPhoneVerified(true);
                                data.setName(reqBody.getString("name"));
                                data.setImage(reqBody.getString("image"));
                                data.setLoggedIn(true);
//                                Toast.makeText(getBaseContext(), response.getString("res"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            startActivity(new Intent(getBaseContext(), MainContainer.class));
                            finish();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (progressBar.getVisibility() == View.VISIBLE)
                        progressBar.setVisibility(View.GONE);
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

    private class EmailLogin extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            JSONObject reqBody = new JSONObject();
            try {
                reqBody.put("email",emailET.getText().toString());
                reqBody.put("password",passwordET.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API.LOGIN, reqBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (progressBar.getVisibility() == View.VISIBLE)
                                progressBar.setVisibility(View.GONE);

                            Log.e(Tokens.LOG, response.toString());

                            try {
                                if(response.getString("res").equalsIgnoreCase("success"))
                                {
                                    response = response.getJSONObject("user");

                                    UserData data = new UserData(getBaseContext());
                                    if (response.has("user_id"))
                                        data.setUserID(response.getString("user_id"));
                                    if (response.has("phone"))
                                        data.setPhone(response.getString("phone"));
                                    if (response.has("isPhoneVerified"))
                                        data.setPhoneVerified(response.getBoolean("isPhoneVerified"));
                                    if (response.has("image"))
                                        data.setImage(response.getString("image"));
                                    if (response.has("email"))
                                        data.setEmail(response.getString("email"));
                                    if (response.has("name"))
                                        data.setName(response.getString("name"));
                                    if(response.has("isEmailVerified"))
                                        data.setEmailVerified(response.getBoolean("isEmailVerified"));
                                    startActivity(new Intent(getBaseContext(), MainContainer.class));
                                    finish();
                                }
                                else
                                {
                                    new LovelyStandardDialog(Login.this, R.style.TintTheme)
                                            .setTopColorRes(R.color.blue_button)
                                            .setIcon(android.R.drawable.ic_dialog_info)
                                            .setTitle("Attention")
                                            .setCancelable(false)
                                            .setMessage(response.getString("res"))
                                            .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                }
                                            })
                                            .show();
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
