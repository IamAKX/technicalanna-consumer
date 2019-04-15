package com.akashapplications.technicalanna.HelperActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.akashapplications.technicalanna.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class TopperTipFull extends Activity {

    String text="", link="";
    ImageView imageView;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topper_tip_full);
        text = getIntent().getStringExtra("text");
        link = getIntent().getStringExtra("link");
        imageView = findViewById(R.id.image);
        textView = findViewById(R.id.text);

        textView.setText(text);
        Glide.with(getBaseContext())
                .load(link)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.loading))
                .into(imageView);

    }
}
