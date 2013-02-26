package org.williamjoy.gexpense;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class Preferences extends PreferenceActivity {
    private String[] calendarNames = {};
    private String[] calendarIDs = {};
    private final String[] EVENT_PROJECTION = new String[] { Calendars._ID, // 0
            Calendars.ACCOUNT_NAME, // 1
            Calendars.CALENDAR_DISPLAY_NAME, // 2
            Calendars.OWNER_ACCOUNT, // 3
            Calendars.ACCOUNT_TYPE,// 4
    };

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        ActionBar bar = this.getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("Settings");
        initlizeCalendarList();
        ListPreference calendar = (ListPreference) this
                .findPreference(ExpenseConstants.ExpenseEvents._ID);
        calendar.setEntries(calendarNames);
        calendar.setEntryValues(calendarIDs);

        Preference about = (Preference) findPreference(this.getResources()
                .getString(R.string.key_about));
        about.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                showAbout();
                return true;
            }
        });
        Preference history_month = (Preference) findPreference(this
                .getResources().getString(R.string.key_history));
        Preference createCalendar = (Preference) findPreference("calendar_create");
        createCalendar.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showCreateCalendarDialog();
                return true;
            }
        });
        history_month.setOnPreferenceClickListener(new OnPreferenceClickListener() {

                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        showHistoryMonthSeek();
                        return true;
                    }
                });
    }

    protected void showCreateCalendarDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("New calendar");
        alert.setMessage("Create a new calendar");
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View createCalendarView = inflater.inflate(R.layout.create_calendar, null);
        alert.setView(createCalendarView);
        OnClickListener dialogListener = new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE)
                    return;
                String input=((EditText)createCalendarView.findViewById(R.id.create_calendar)).getText().toString();
                Toast.makeText(getBaseContext(), "Not creat "+input +", API not work for create calendar!", Toast.LENGTH_SHORT).show();
            }
        };
        alert.setPositiveButton("Ok", dialogListener);
        alert.setNegativeButton("Cancel", dialogListener);
        alert.show();
    }

    protected void showHistoryMonthSeek() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Display history limit");
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View historyView = inflater.inflate(R.layout.history_months, null);
        alert.setView(historyView);
        alert.setMessage("set number of display month limit");
        final NumberPicker numberPicker = (NumberPicker) historyView
                .findViewById(R.id.seekBarHistoryMonth);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(12);
        Preference history_month = (Preference) findPreference("history_month");
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(Preferences.this);
        int storedMonth = sharedPref.getInt(history_month.getKey(),
                ExpenseConstants.MINUMUM_MONTHS);
        numberPicker.setValue(storedMonth);

        OnClickListener dialogListener = new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE)
                    return;
                int months = numberPicker.getValue();
                Preference history_month = (Preference) findPreference("history_month");
                SharedPreferences.Editor editor = PreferenceManager
                        .getDefaultSharedPreferences(Preferences.this).edit();
                editor.putInt(history_month.getKey(), months);
                editor.commit();
            }
        };
        alert.setPositiveButton("Ok", dialogListener);
        alert.setNegativeButton("Cancel", dialogListener);
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
                ExpenseConstants.COM_DOT_GOOGLE, null);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }
        return true;
    }
}