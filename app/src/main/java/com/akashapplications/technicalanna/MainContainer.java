    package com.akashapplications.technicalanna;

import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.akashapplications.technicalanna.MenuFragments.Exams;
import com.akashapplications.technicalanna.MenuFragments.Home;
import com.akashapplications.technicalanna.MenuFragments.Notification;
import com.akashapplications.technicalanna.MenuFragments.Wallet;
import com.akashapplications.technicalanna.PersonalMenu.Profile;

    public class MainContainer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;

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
                startActivity(new Intent(getBaseContext(),Login.class));
                finish();
            }
        });
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


}
