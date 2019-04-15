package com.akashapplications.technicalanna.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akashapplications.technicalanna.R;
import com.akashapplications.technicalanna.SubMenuActivity.SubjectWiseExam.SubjectAllExam;
import com.akashapplications.technicalanna.Utils.Dummy;

import java.util.ArrayList;

public class SubjectExamsAdapter extends RecyclerView.Adapter<SubjectExamsAdapter.ViewHolder>{
    private Context context;
    private ArrayList<String> list;

    public SubjectExamsAdapter(Context context, ArrayList<String> list) {
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



        viewHolder.subject.setText(list.get(position));
        viewHolder.card.setBackgroundColor(Dummy.generateRandomColor());

        viewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, SubjectAllExam.class).putExtra("subject",list.get(position)));
            }
        });
        viewHolder.subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, SubjectAllExam.class).putExtra("subject",list.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView subject;
        CardView card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subject = itemView.findViewById(R.id.textview);
            card = itemView.findViewById(R.id.card);
        }
    }
}
