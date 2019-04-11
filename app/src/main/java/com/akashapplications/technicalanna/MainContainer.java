    package com.akashapplications.technicalanna;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akashapplications.technicalanna.LocalData.UserData;
import com.akashapplications.technicalanna.MenuFragments.Exams;
import com.akashapplications.technicalanna.MenuFragments.Home;
import com.akashapplications.technicalanna.MenuFragments.Notification;
import com.akashapplications.technicalanna.MenuFragments.Wallet;
import com.akashapplications.technicalanna.PersonalMenu.Profile;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONException;
import org.json.JSONObject;

    public class MainContainer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;
    TextView email, name;
    ImageView imageView;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setTitle("Home");
        fragmentManager = getSupportFragmentManager();
        changeFragment(new Home());

        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UserData(getBaseContext()).logOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {

                            }
                        });
                startActivity(new Intent(getBaseContext(),Login.class));
                finish();
            }
        });

//        View navHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_main_container);
        View navHeaderView = navigationView.getHeaderView(0);
        name = navHeaderView.findViewById(R.id.name);
        email = navHeaderView.findViewById(R.id.email);
        imageView = navHeaderView.findViewById(R.id.imageView);

        UserData data = new UserData(getBaseContext());
        name.setText(data.getName());
        email.setText(data.getEmail());
        Glide.with(getBaseContext())
                .load(data.getImage())
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);

        new GetUserDetail().execute();

    }

        @Override
        protected void onStart() {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            mGoogleApiClient.connect();
            super.onStart();

        }

    boolean doubleBackToExitPressedOnce = false;

        @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_container, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.nav_profile:
                startActivity(new Intent(getBaseContext(), Profile.class));
                break;

            case R.id.nav_share:
                String shareBody = "Enhance your studying style and test your progress with Technical Anna.\nInstall today from https://app.playstore.google.com";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share Technical Anna"));
                break;

            case R.id.nav_rate:
                Toast.makeText(getBaseContext(),"Redirect to app play store link", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    private void changeFragment(Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.container,fragment)
                .commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        switch (id){
            case R.id.nav_home:
                fragment = new Home();
                getSupportActionBar().setTitle("Home");
                changeFragment(fragment);
                break;

            case R.id.nav_exams:
                fragment = new Exams();
                getSupportActionBar().setTitle("Exams");
                changeFragment(fragment);
                break;

            case R.id.nav_notification:
                fragment = new Notification();
                getSupportActionBar().setTitle("Notification");
                changeFragment(fragment);
                break;

            case R.id.nav_wallet:
                fragment = new Wallet();
                getSupportActionBar().setTitle("Wallet");
                changeFragment(fragment);
                break;

            case R.id.nav_profile:
                startActivity(new Intent(getBaseContext(), Profile.class));
                break;

            case R.id.nav_share:
                String shareBody = "Enhance your studying style and test your progress with Technical Anna.\nInstall today from https://app.playstore.google.com";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share Technical Anna"));
                break;

            case R.id.nav_rate:
                Toast.makeText(getBaseContext(),"Redirect to app play store link", Toast.LENGTH_LONG).show();
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


        private class GetUserDetail extends AsyncTask<Void,Void,Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                JSONObject reqBody = new JSONObject();
                try {
                    reqBody.put("email",new UserData(getBaseContext()).getEmail());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API.GET_PROFILE_DETAIL, reqBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Log.e(Tokens.LOG, response.toString());
                                UserData data = new UserData(getBaseContext());
                                try {
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
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                name.setText(data.getName());
                                email.setText(data.getEmail());
                                Glide.with(getBaseContext())
                                        .load(data.getImage())
                                        .apply(RequestOptions.circleCropTransform())
                                        .into(imageView);

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
    }
