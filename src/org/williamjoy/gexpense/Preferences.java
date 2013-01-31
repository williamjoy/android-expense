package org.williamjoy.gexpense;

import java.util.ArrayList;
import java.util.Map;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.CalendarContract.Calendars;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Preferences extends PreferenceActivity {
    private String[] calendarNames = {};
    private String[] calendarIDs = {};
    private final String[] EVENT_PROJECTION = new String[] { Calendars._ID, // 0
            Calendars.ACCOUNT_NAME, // 1
            Calendars.CALENDAR_DISPLAY_NAME, // 2
            Calendars.OWNER_ACCOUNT, // 3
            Calendars.ACCOUNT_TYPE,// 4
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        initlizeCalendarList();
        ListPreference calendar = (ListPreference) this
                .findPreference(GExpenseConstants.ExpenseEvents._ID);
        calendar.setEntries(calendarNames);
        calendar.setEntryValues(calendarIDs);

        Preference about = (Preference) findPreference("about");
        about.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                showAbout();
                return true;
            }
        });
        Preference history_month = (Preference) findPreference("history_month");
        history_month
                .setOnPreferenceClickListener(new OnPreferenceClickListener() {

                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        showHistoryMonthSeek();
                        return true;
                    }
                });
    }

    protected void showHistoryMonthSeek() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Display historical months");
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View historyView = inflater.inflate(R.layout.history_months, null);
        alert.setView(historyView);
        alert.setIcon(R.drawable.ic_money);

        final SeekBar skBar = (SeekBar) historyView
                .findViewById(R.id.seekBarHistoryMonth);
        final TextView text = (TextView) historyView
                .findViewById(R.id.textViewHistoryMonth);

        Preference history_month = (Preference) findPreference("history_month");
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(Preferences.this);
        int progress = sharedPref.getInt(history_month.getKey(),
                GExpenseConstants.MINUMUM_MONTHS);
        text.setText("Display from " + progress + " month(s) ago");
        progress = progress - GExpenseConstants.MINUMUM_MONTHS;
        skBar.setProgress(progress);
        OnSeekBarChangeListener l = new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser) {
                text.setText("Display from "
                        + (progress + GExpenseConstants.MINUMUM_MONTHS)
                        + " month(s) ago");
            }
        };
        skBar.setOnSeekBarChangeListener(l);

        OnClickListener positive = new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                int months = skBar.getProgress()
                        + GExpenseConstants.MINUMUM_MONTHS;
                Preference history_month = (Preference) findPreference("history_month");
                SharedPreferences.Editor editor = PreferenceManager
                        .getDefaultSharedPreferences(Preferences.this).edit();
                editor.putInt(history_month.getKey(), months);
                editor.commit();
            }
        };
        alert.setPositiveButton("Ok", positive);
        alert.show();

    }

    private void showAbout() {
        Intent intent = new Intent(getBaseContext(), AboutActivity.class);
        startActivity(intent);
    }

    private void initlizeCalendarList() {
        ArrayList<String> calendarNameList = new java.util.ArrayList<String>();
        ArrayList<String> calendarIDList = new java.util.ArrayList<String>();

        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = Calendars.CONTENT_URI;
        String selection = Calendars.ACCOUNT_TYPE + " = ? AND "
                + Calendars.OWNER_ACCOUNT + " LIKE ? ";
        cur = cr.query(uri, EVENT_PROJECTION, selection,
                GExpenseConstants.COM_DOT_GOOGLE, null);
        while (cur.moveToNext()) {
            long calID = cur.getLong(0);
            // String accountName = cur.getString(1);
            String displayName = cur.getString(2);
            String ownerName = cur.getString(3);
            Log.d(this.getPackageCodePath(), displayName + "|" + ownerName);

            calendarNameList.add(displayName);
            calendarIDList.add(calID + "");
        }
        calendarNames = calendarNameList.toArray(calendarNames);
        calendarIDs = calendarIDList.toArray(calendarIDs);
    }

}