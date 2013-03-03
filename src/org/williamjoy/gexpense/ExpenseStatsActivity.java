package org.williamjoy.gexpense;

import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract.Instances;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class ExpenseStatsActivity extends Activity {
    ProgressBarStatsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPrefences = PreferenceManager
                .getDefaultSharedPreferences(this);
        this.setContentView(R.layout.expense_stats);
        ListView listView = (ListView) this.findViewById(R.id.listViewStats);
        
        String unit=sharedPrefences.getString("unit", "￥");
        mAdapter = new ProgressBarStatsAdapter(this);
        mAdapter.setCurrencyUnit(unit);
        this.doStats();
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ExpenseStatsActivity.this);
                parent.getAdapter().getItem(position);
                alert.setTitle("Details");
                TextView tv=new TextView(getBaseContext());
                tv.setText("TODO");
                ScrollView sv=new ScrollView(getBaseContext());
                sv.addView(tv);
                alert.setView(sv);
                alert.show();
            }
        });
    }

    private void doStats() {
        long calendar_id = getSelectedCalendarID();
        Calendar endTime = Calendar.getInstance();
        Calendar beginTime = (Calendar) endTime.clone();
        endTime.add(Calendar.MONTH, +1);
        beginTime.add(Calendar.MONTH, -36);
        long startMillis = beginTime.getTimeInMillis();
        long endMillis = endTime.getTimeInMillis();

        Cursor cur = null;
        ContentResolver cr = getContentResolver();

        // Construct the query with the desired date range.
        Uri.Builder builder = Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, startMillis);
        ContentUris.appendId(builder, endMillis);

        // Submit the query
        cur = cr.query(builder.build(), new String[] { Instances.BEGIN,
                Instances.TITLE }, Instances.CALENDAR_ID + " = ?",
                new String[] { "" + calendar_id }, Instances.BEGIN + " ASC");

        int last_month = -1;
        double sum = 0;
        int year = 0, month = 0;
        while (cur.moveToNext()) {
            String title = "";

            long beginVal = cur.getLong(0);
            title = cur.getString(1);
            String s[] = title.split(ExpenseConstants.EVENT_TILE_DELIMITER_REGEX);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(beginVal);

            if (s.length > 1) {
                try {
                    double money = Double.parseDouble(s[1]);
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    int current_month = (year << 4) + month;
                    if (current_month != last_month) {
                        if (last_month != -1) {
                            String key = String.format(Locale.getDefault(),"%d-%02d",
                                    last_month >> 4, last_month % 16 + 1);
                            this.mAdapter.pushData(key, (int) sum);
                        }
                        sum = money;
                        last_month = current_month;
                    } else {
                        sum += money;
                    }
                } catch (Exception e) {
                    Log.d("Debug", "", e);
                }
            }
        }

        if (last_month != -1) {
            String key = String.format(Locale.getDefault(),"%d-%02d", year, month + 1);
            this.mAdapter.pushData(key, (int) sum);
        }
    }

    private long getSelectedCalendarID() {
        long cal_id = -1L;
        SharedPreferences sharedPrefenceManger = PreferenceManager
                .getDefaultSharedPreferences(this);

        String cal = sharedPrefenceManger.getString(
                ExpenseConstants.ExpenseEvents._ID,
                ExpenseConstants.ExpenseEvents._ID + "");
        try {
            cal_id = Long.parseLong(cal);
        } catch (NumberFormatException e) {
            Log.d("GET CALENDAR ID FAILED", cal, e);
        }
        return cal_id;
    }

}
