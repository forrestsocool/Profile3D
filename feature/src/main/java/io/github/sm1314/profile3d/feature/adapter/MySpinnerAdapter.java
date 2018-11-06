package io.github.sm1314.profile3d.feature.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinnerAdapter;

import java.util.List;

import io.github.sm1314.profile3d.feature.R;

public class MySpinnerAdapter extends MaterialSpinnerAdapter {
    public MySpinnerAdapter(Context context, List items) {
        super(context, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mView = super.getView(position, convertView, parent);
        TextView textView = (TextView) mView.findViewById(R.id.tv_tinted_spinner);
        textView.setTextSize(8);

        return mView;
    }
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View mView = super.getView(position, convertView, parent);
//        TextView textView = (TextView) convertView.findViewById(R.id.tv_tinted_spinner);
//        textView.setTextSize(10);
//        return mView;
//    }
}
