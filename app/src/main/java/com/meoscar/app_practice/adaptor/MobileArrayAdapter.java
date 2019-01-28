package com.meoscar.app_practice.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.meoscar.app_practice.R;

public class MobileArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public MobileArrayAdapter(Context context, String[] values) {
        super(context, R.layout.list_mobile, values);
        this.context = context;
        this.values = values;
    }

    private static class ViewHolder {
        public ImageView img;
        public TextView txt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =  inflater.inflate(R.layout.list_mobile, parent, false);
            holder.txt = (TextView) convertView.findViewById(R.id.label);
            holder.img = (ImageView) convertView.findViewById(R.id.logo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt.setText(values[position]);

        // Change icon based on name
        String s = values[position];
        System.out.println(s);
        if (s.equals("WindowsMobile")) {
            holder.img.setImageResource(R.drawable.windowsmobile_logo);
        } else if (s.equals("iOS")) {
            holder.img.setImageResource(R.drawable.ios_logo);
        } else if (s.equals("Blackberry")) {
            holder.img.setImageResource(R.drawable.blackberry_logo);
        } else {
            holder.img.setImageResource(R.drawable.android_logo);
        }

        return convertView;
    }
}
