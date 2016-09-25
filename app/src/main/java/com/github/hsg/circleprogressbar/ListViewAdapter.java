package com.github.hsg.circleprogressbar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by hsg on 9/25/16.
 */

public class ListViewAdapter extends ArrayAdapter<Float> {
    private int resourceId;

    public ListViewAdapter(Context context, int resource, List<Float> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        float progress = getItem(position).floatValue();

        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null, false);
            viewHolder = new ViewHolder();
            viewHolder.circleProgressBar = (CircleProgressBar) view.findViewById(R.id.circle_progress_bar);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.circleProgressBar.setProgress(progress);
        viewHolder.circleProgressBar.startAnimationAction(null);
        return view;
    }


    class ViewHolder {
        CircleProgressBar circleProgressBar;
    }
}
