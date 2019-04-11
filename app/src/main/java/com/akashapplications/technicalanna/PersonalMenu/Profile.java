package com.akashapplications.technicalanna.PersonalMenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.akashapplications.technicalanna.LocalData.UserData;
import com.akashapplications.technicalanna.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

public class Profile extends Activity {

    EditText name, email, phone;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);

        image = findViewById(R.id.imageView);

        findViewById(R.id.updateImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.updateProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        loadLocalData();
    }

    private void loadLocalData() {
        UserData data = new UserData(getBaseContext());
        name.setText(data.getName());
        email.setText(data.getEmail());
        phone.setText(data.getPhone());
        Glide.with(getBaseContext())
                .load(data.getImage())
                .apply(RequestOptions.circleCropTransform())
                .into(image);
    }
}
