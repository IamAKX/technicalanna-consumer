package com.akashapplications.technicalanna.MenuFragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akashapplications.technicalanna.MainContainer;
import com.akashapplications.technicalanna.Models.Exams;
import com.akashapplications.technicalanna.Models.SubjectExams;
import com.akashapplications.technicalanna.R;
import com.akashapplications.technicalanna.Test.TestContent;
import com.akashapplications.technicalanna.Utils.Dummy;
import com.akashapplications.technicalanna.Utils.Subjects;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {

    Context context;
    SliderLayout sliderLayout;
    LinearLayout allExamsContainer;
    TextView marqueeExamNotification;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        marqueeExamNotification = getView().findViewById(R.id.marqueeExamNotification);
        sliderLayout = getView().findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.THIN_WORM); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
        sliderLayout.setScrollTimeInSec(2); //set scroll delay in seconds :

        for(String link : Dummy.bannerImages)
        {
            DefaultSliderView sliderView = new DefaultSliderView(context);
            sliderView.setImageUrl(link);
            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Toast.makeText(context, "Redirection will done later", Toast.LENGTH_LONG).show();
                }
            });
            sliderLayout.addSliderView(sliderView);
        }

        allExamsContainer = getActivity().findViewById(R.id.all_exams_container);
        populateAllExams();

        marqueeExamNotification.setText("Registration login for Zilla Parishad examination(last date - 16th April 2019)");
        marqueeExamNotification.setSelected(true);

        getView().findViewById(R.id.moretest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainContainer) getActivity()).getSupportActionBar().setTitle("Exams");

                getFragmentManager().beginTransaction()
                        .replace(R.id.container,new com.akashapplications.technicalanna.MenuFragments.Exams())
                        .commit();
            }
        });

        setExamButtonListeners();

    }

    private void setExamButtonListeners() {
        getView().findViewById(R.id.tv1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubjectExams se = Subjects.getSubjectModelByName("Strength of materials");
                if(se == null)
                {
                    Toast.makeText(context,"Subject is not available",Toast.LENGTH_SHORT).show();
                    return;
                }
                context.startActivity(new Intent(context, TestContent.class).putExtra("subject",se));
            }
        });

        getView().findViewById(R.id.tv2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubjectExams se = Subjects.getSubjectModelByName("Geo-technical Engineering");
                if(se == null)
                {
                    Toast.makeText(context,"Subject is not available",Toast.LENGTH_SHORT).show();
                    return;
                }
                context.startActivity(new Intent(context, TestContent.class).putExtra("subject",se));
            }
        });

        getView().findViewById(R.id.tv3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubjectExams se = Subjects.getSubjectModelByName("General Knowledge");
                if(se == null)
                {
                    Toast.makeText(context,"Subject is not available",Toast.LENGTH_SHORT).show();
                    return;
                }
                context.startActivity(new Intent(context, TestContent.class).putExtra("subject",se));
            }
        });

        getView().findViewById(R.id.tv4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubjectExams se = Subjects.getSubjectModelByName("Marathi");
                if(se == null)
                {
                    Toast.makeText(context,"Subject is not available",Toast.LENGTH_SHORT).show();
                    return;
                }
                context.startActivity(new Intent(context, TestContent.class).putExtra("subject",se));
            }
        });
    }

    private void populateAllExams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(20, 6, 10, 6);
        ArrayList<Exams> examList = new ArrayList<>();

        Exams exams = new Exams();
        exams.setName("MPSC");
        exams.setImageID(R.drawable.mpsc);
        examList.add(exams);

        exams = new Exams();
        exams.setName("ZP");
        exams.setImageID(R.drawable.zp);
        examList.add(exams);

        exams = new Exams();
        exams.setName("CIDCO");
        exams.setImageID(R.drawable.cidco);
        examList.add(exams);

        exams = new Exams();
        exams.setName("MSRTC");
        exams.setImageID(R.drawable.msrtc);
        examList.add(exams);

        exams = new Exams();
        exams.setName("RRB");
        exams.setImageID(R.drawable.rrb);
        examList.add(exams);

        exams = new Exams();
        exams.setName("PWD");
        exams.setImageID(R.drawable.pwd);
        examList.add(exams);


        for(Exams e : examList)
        {
            View button = getLayoutInflater().inflate(R.layout.main_exams_item, null);
            ImageView iv = button.findViewById(R.id.exam_icon);
            TextView tv = button.findViewById(R.id.exam_name);
            tv.setText(e.getName());
            Glide.with(context)
                    .load(e.getImageID())
                    .apply(RequestOptions.circleCropTransform())
                    .into(iv);
            button.setLayoutParams(params);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"Not designed yet",Toast.LENGTH_SHORT).show();
                }
            });
            allExamsContainer.addView(button);
        }
    }


}
