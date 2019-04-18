package com.akashapplications.technicalanna.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.akashapplications.technicalanna.R;
import com.akashapplications.technicalanna.Utils.Tokens;

import java.util.ArrayList;

public class NotificationAdapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<String> list;

    public NotificationAdapter(Context context, ArrayList<String> list){
        super(context, R.layout.list_noti_item, list);
        this.context = context;
        this.list = list;
    }
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_noti_item, parent, false);

        if(convertView == null)
            Log.e(Tokens.LOG,"convertview is null");

        TextView title = (TextView) convertView.findViewById(R.id.text);


        Log.e(Tokens.LOG,"List size "+list.size()+" position "+list.get(position));
        title.setText(""+list.get(position));

        return convertView;
    }
}
