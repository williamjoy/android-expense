package org.williamjoy.gexpense;

import java.util.List;

import org.williamjoy.gexpense.model.CalendarInstanceData;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CalendarInstanceAdapter extends BaseAdapter {
    private List<CalendarInstanceData> instances;
    private static LayoutInflater inflater = null;
    private String mCurrencyUnit = "";

    public CalendarInstanceAdapter(Activity activity,
            List<CalendarInstanceData> list) {
        this.instances = list;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_row_expense, null);
        TextView title = (TextView) vi.findViewById(R.id.title); // title
        TextView startDate = (TextView) vi.findViewById(R.id.textViewStartDate);
        TextView location = (TextView) vi.findViewById(R.id.location); // location
                                                                       // name
        TextView money = (TextView) vi.findViewById(R.id.textViewMoney); // money
        CalendarInstanceData instance = this.instances.get(instances.size()
                - position - 1);
        // Setting all values in listview
        title.setText(instance.getTitle());
        startDate.setText(instance.getStartDate());
        location.setText(instance.getEventLocation());
        money.setText(instance.getMoney() + mCurrencyUnit);
        return vi;
    }

    @Override
    public int getCount() {
        return instances.size();
    }

    @Override
    public Object getItem(int position) {
        return this.instances.get(instances.size() - position - 1);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public String getCurrencyUnit() {
        return mCurrencyUnit;
    }

    public void setCurrencyUnit(String mCurrencyUnit) {
        this.mCurrencyUnit = mCurrencyUnit;
    }

}
