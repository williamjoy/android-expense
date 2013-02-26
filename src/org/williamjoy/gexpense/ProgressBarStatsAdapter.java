package org.williamjoy.gexpense;

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
        TextView value = (TextView) vi.findViewById(R.id.textViewValue);
        ProgressBar progressBarValue = (ProgressBar) vi
                .findViewById(R.id.progressBarValue);
        progressBarValue.setProgress(9);
        progressBarValue.setMax(mMaxProgress);
        label.setText("Hello");
        value.setText("Hello");
        return vi;
    }

    @Override
    public int getCount() {
        return 10;
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
