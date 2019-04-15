package com.akashapplications.technicalanna.MenuFragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akashapplications.technicalanna.MainContainer;
import com.akashapplications.technicalanna.Models.Exams;
import com.akashapplications.technicalanna.R;
import com.akashapplications.technicalanna.SubMenuActivity.SubjectWiseExam.SubjectAllExam;
import com.akashapplications.technicalanna.Test.TestContent;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import org.w3c.dom.Text;

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
        sliderLayout.setScrollTimeInSec(1); //set scroll delay in seconds :

        allExamsContainer = getActivity().findViewById(R.id.all_exams_container);
        populateAllExams();

        marqueeExamNotification.setText("");
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
        updateExamNotification();
        updateToppersTip();
    }

    private void updateToppersTip() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Config/TopperTips");


        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for(final DataSnapshot ds : dataSnapshot.getChildren()) {

                    DefaultSliderView sliderView = new DefaultSliderView(context);
                    sliderView.setImageUrl(ds.child("link").getValue(String.class));
                    sliderView.setDescription(ds.child("text").getValue(String.class));
                    sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                    sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(SliderView sv) {
//                            context.startActivity(new Intent(context, TopperTipFull.class)
//                                    .putExtra("text",sv.getDescription())
//                                    .putExtra("link",sv.getImageUrl()));
                        }
                    });
                    sliderLayout.addSliderView(sliderView);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for(final DataSnapshot ds : dataSnapshot.getChildren()) {

                    DefaultSliderView sliderView = new DefaultSliderView(context);
                    sliderView.setImageUrl(ds.child("link").getValue(String.class));
                    sliderView.setDescription(ds.child("text").getValue(String.class));
                    sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                    sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(SliderView sv) {
//                            context.startActivity(new Intent(context, TopperTipFull.class)
//                                    .putExtra("text",sv.getDescription())
//                                    .putExtra("link",sv.getImageUrl()));
                        }
                    });
                    sliderLayout.addSliderView(sliderView);
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot ds : dataSnapshot.getChildren()) {

                    DefaultSliderView sliderView = new DefaultSliderView(context);
                    sliderView.setImageUrl(ds.child("link").getValue(String.class));
                    sliderView.setDescription(ds.child("text").getValue(String.class));
                    sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                    sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(SliderView sv) {
//                            context.startActivity(new Intent(context, TopperTipFull.class)
//                                    .putExtra("text",sv.getDescription())
//                                    .putExtra("link",sv.getImageUrl()));
                        }
                    });
                    sliderLayout.addSliderView(sliderView);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for(final DataSnapshot ds : dataSnapshot.getChildren()) {

                    DefaultSliderView sliderView = new DefaultSliderView(context);
                    sliderView.setImageUrl(ds.child("link").getValue(String.class));
                    sliderView.setDescription(ds.child("text").getValue(String.class));
                    sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                    sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(SliderView sv) {
//                            context.startActivity(new Intent(context, TopperTipFull.class)
//                                    .putExtra("text",sv.getDescription())
//                                    .putExtra("link",sv.getImageUrl()));
                        }
                    });
                    sliderLayout.addSliderView(sliderView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void updateExamNotification() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("Config/ExamNotification");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                marqueeExamNotification.setText((CharSequence) dataSnapshot.child("text").getValue());
                marqueeExamNotification.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri webpage = Uri.parse(String.valueOf(dataSnapshot.child("link").getValue()));
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
                        context.startActivity(myIntent);
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                marqueeExamNotification.setText((CharSequence) dataSnapshot.child("text").getValue());
                marqueeExamNotification.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri webpage = Uri.parse(String.valueOf(dataSnapshot.child("link").getValue()));
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
                        context.startActivity(myIntent);
                    }
                });
            }

            @Override
            public void onChildRemoved(@NonNull final DataSnapshot dataSnapshot) {
                marqueeExamNotification.setText((CharSequence) dataSnapshot.child("text").getValue());
                marqueeExamNotification.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri webpage = Uri.parse(String.valueOf(dataSnapshot.child("link").getValue()));
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
                        context.startActivity(myIntent);
                    }
                });
            }

            @Override
            public void onChildMoved(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                marqueeExamNotification.setText((CharSequence) dataSnapshot.child("text").getValue());
                marqueeExamNotification.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri webpage = Uri.parse(String.valueOf(dataSnapshot.child("link").getValue()));
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
                        context.startActivity(myIntent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setExamButtonListeners() {
        getView().findViewById(R.id.tv1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, SubjectAllExam.class).putExtra("subject","Strength of materials"));

            }
        });

        getView().findViewById(R.id.tv2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, SubjectAllExam.class).putExtra("subject","Geo-technical Engineering"));
            }
        });

        getView().findViewById(R.id.tv3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, SubjectAllExam.class).putExtra("subject","General Knowledge"));
            }
        });

        getView().findViewById(R.id.tv4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, SubjectAllExam.class).putExtra("subject","Marathi"));
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
            final String examName = e.getName();
            View button = getLayoutInflater().inflate(R.layout.main_exams_item, null);
            ImageView iv = button.findViewById(R.id.exam_icon);
            final TextView tv = button.findViewById(R.id.exam_name);
            tv.setText(e.getName());
            Glide.with(context)
                    .load(e.getImageID())
                    .apply(RequestOptions.circleCropTransform())
                    .into(iv);
            button.setLayoutParams(params);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, TestContent.class).putExtra("subject",tv.getText().toString()));

                }
            });
            allExamsContainer.addView(button);
        }
    }


}
