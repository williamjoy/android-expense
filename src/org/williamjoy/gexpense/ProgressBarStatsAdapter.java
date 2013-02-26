package org.williamjoy.gexpense;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressBarStatsAdapter extends BaseAdapter {

    private static LayoutInflater mInflater = null;
    private int mMaxProgress = 100;
    private List<Object> mLabels = new ArrayList<Object>();
    private List<Integer> mVales = new ArrayList<Integer>();

    public int pushData(Object object, int value) {
        this.mLabels.add(object);
        this.mVales.add(value);
        if (value > mMaxProgress) {
            mMaxProgress = value;
        }
        return mLabels.size();
    }

    public ProgressBarStatsAdapter(Activity activity) {
        mInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = mInflater.inflate(R.layout.stats_row, null);
        TextView label = (TextView) vi.findViewById(R.id.textViewLabel);
        label.setText(mLabels.get(position).toString());
        ProgressBar progressBarValue = (ProgressBar) vi
                .findViewById(R.id.progressBarValue);
        progressBarValue.setProgress(mVales.get(position));
        progressBarValue.setMax(mMaxProgress);
        return vi;
    }

    @Override
    public int getCount() {
        return mLabels.size();
    }

    @Override
    public Object getItem(int position) {
        return new Object();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
