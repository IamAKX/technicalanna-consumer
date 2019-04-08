package com.akashapplications.technicalanna.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akashapplications.technicalanna.Models.SubjectExams;
import com.akashapplications.technicalanna.R;
import com.akashapplications.technicalanna.Test.TestContent;
import com.akashapplications.technicalanna.Utils.Dummy;
import com.dinuscxj.progressbar.CircleProgressBar;

import java.util.ArrayList;

public class SubjectExamsAdapter extends RecyclerView.Adapter<SubjectExamsAdapter.ViewHolder>{
    private Context context;
    private ArrayList<SubjectExams> list;

    public SubjectExamsAdapter(Context context, ArrayList<SubjectExams> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SubjectExamsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.subject_exams_progress,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectExamsAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.progressBar.setProgress(list.get(position).getProgress());
        viewHolder.progressBar.setProgressFormatter(new CircleProgressBar.ProgressFormatter() {
            @Override
            public CharSequence format(int progress, int max) {
                return String.format("%d%%", (int) ((float) progress / (float) max * 100));
            }
        });

        viewHolder.subject.setText(list.get(position).getName());
        viewHolder.background.setBackgroundColor(Dummy.generateRandomColor());

        viewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, TestContent.class).putExtra("subject",list.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleProgressBar progressBar;
        TextView subject;
        CardView card;
        RelativeLayout background;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
            subject = itemView.findViewById(R.id.subjectName);
            background = itemView.findViewById(R.id.progressBackground);
            card = itemView.findViewById(R.id.card);
        }
    }
}
