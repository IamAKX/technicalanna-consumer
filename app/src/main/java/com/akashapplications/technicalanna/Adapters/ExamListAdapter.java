package com.akashapplications.technicalanna.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.akashapplications.technicalanna.Models.SubjectExamsModel;
import com.akashapplications.technicalanna.R;
import com.akashapplications.technicalanna.Utils.Tokens;

import java.util.ArrayList;

public class ExamListAdapter extends ArrayAdapter<SubjectExamsModel> {
    Context context;
    ArrayList<SubjectExamsModel> list;

    public ExamListAdapter(Context context, ArrayList<SubjectExamsModel> list){
        super(context, R.layout.exam_title_item, list);
        this.context = context;
        this.list = list;
    }

    public View getView(int position, View convertView, ViewGroup parent) {


            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.exam_title_item, parent, false);

            if(convertView == null)
                Log.e(Tokens.LOG,"convertview is null");

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView marks = (TextView) convertView.findViewById(R.id.marks);
        TextView fees = (TextView) convertView.findViewById(R.id.fees);

        Log.e(Tokens.LOG,"List size "+list.size()+" position "+list.get(position).getName());
        title.setText(""+list.get(position).getName());
        marks.setText("Full marks : "+list.get(position).getFullMarks());
        fees.setText("Exam fees : Rs."+list.get(position).getFees());
        return convertView;
    }

}
