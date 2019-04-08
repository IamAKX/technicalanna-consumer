package com.akashapplications.technicalanna;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;
import com.github.paolorotolo.appintro.model.SliderPage;

public class AppIntro extends AppIntro2{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setFadeAnimation();
//        setZoomAnimation();
//        setFlowAnimation();
//        setSlideOverAnimation();
//        setDepthAnimation();


        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle("Online Mentors");
        sliderPage.setTitleColor(Color.parseColor("#000000"));
        sliderPage.setDescription("Mentor for Civil Engineering Exams - MPSC, ZP, CIDCO, RRB , PWD, MSRTC");
        sliderPage.setDescColor(Color.parseColor("#01579b"));
        sliderPage.setImageDrawable(R.drawable.professor);
        sliderPage.setDescTypeface("font/aller.ttf");
        sliderPage.setBgColor(getResources().getColor(R.color.into2));
        addSlide(AppIntroFragment.newInstance(sliderPage));
        showSkipButton(false);
        setProgressButtonEnabled(true);
        setDepthAnimation();



        sliderPage = new SliderPage();
        sliderPage.setTitle("Test and Analyse");
        sliderPage.setTitleColor(Color.parseColor("#000000"));
        sliderPage.setDescription("Improve your score with exams like Subject Test, Full Length Test and detailed performance analysis  ");
        sliderPage.setDescColor(Color.parseColor("#01579b"));
        sliderPage.setDescTypeface("font/aller.ttf");
        sliderPage.setImageDrawable(R.drawable.test);
        sliderPage.setBgColor(getResources().getColor(R.color.into2));
        addSlide(AppIntroFragment.newInstance(sliderPage));
        showSkipButton(false);
        setProgressButtonEnabled(true);
        setDepthAnimation();


        sliderPage = new SliderPage();
        sliderPage.setTitle("Exercise on the go");
        sliderPage.setTitleColor(Color.parseColor("#000000"));
        sliderPage.setDescription("Practice anytime , anywhere");
        sliderPage.setDescColor(Color.parseColor("#01579b"));
        sliderPage.setDescTypeface("font/aller.ttf");
        sliderPage.setImageDrawable(R.drawable.student);
        sliderPage.setBgColor(getResources().getColor(R.color.into2));
        addSlide(AppIntroFragment.newInstance(sliderPage));
        showSkipButton(false);
        setProgressButtonEnabled(true);
        setDepthAnimation();

    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(getBaseContext(),Login.class));
        finish();
    }
}
