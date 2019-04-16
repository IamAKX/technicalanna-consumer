package com.akashapplications.technicalanna.HelperActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.akashapplications.technicalanna.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import ozaydin.serkan.com.image_zoom_view.ImageViewZoom;

public class BoosterContent extends AppCompatActivity {
    String type, content;
    TextView tv;
    ImageView iv;
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booster_content);

        tv = findViewById(R.id.text);
        iv = findViewById(R.id.imageView);

        type = getIntent().getStringExtra("type");
        content = getIntent().getStringExtra("content");

        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        if(type.equalsIgnoreCase("Text"))
        {
            tv.setText(content);
            tv.setVisibility(View.VISIBLE);
            iv.setVisibility(View.GONE);
        }
        else
        {
            Glide.with(getBaseContext())
                    .load(content)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.loading)
                            .error(R.drawable.loading))
                    .into(iv);

            iv.setVisibility(View.VISIBLE);
            tv.setVisibility(View.GONE);
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        mScaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f,
                    Math.min(mScaleFactor, 10.0f));
            iv.setScaleX(mScaleFactor);
            iv.setScaleY(mScaleFactor);
            return true;
        }
    }
}
