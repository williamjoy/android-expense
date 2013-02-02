package org.williamjoy.gexpense;

import java.util.Calendar;
import java.util.Set;

import org.williamjoy.gexpense.util.DateHelper;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public abstract class AbstractExpenseActivity extends Activity {
    protected Calendar mDate;
    protected String[] what = new String[] {};
    protected String[] location = new String[] {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        onNewIntent(getIntent());
        mDate = Calendar.getInstance();
        setContentView(R.layout.expense_layout);

        ActionBar bar = this.getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("Create");
        loadAutoCompleteResource();
        final TextView editMoney = (TextView) this
                .findViewById(R.id.editTextMoney);
        final AutoCompleteTextView title = (AutoCompleteTextView) this
                .findViewById(R.id.editTextTitle);
        title.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, this.what));

        final AutoCompleteTextView location = (AutoCompleteTextView) this
                .findViewById(R.id.editTextLocation);
        location.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, this.location));
        fillInForm();
        notifyDateDisplayRefresh();

        OnEditorActionListener editorActionListener = new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId,
                    KeyEvent event) {
                boolean handled = false;
                if (v == location) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        doSubmit();
                        handled = true;
                    }
                } else if (v == title) {
                    editMoney.requestFocus();
                    handled = true;
                } else if (v == editMoney) {
                    location.requestFocus();
                    handled = true;
                }
                return handled;
            }
        };
        location.setOnEditorActionListener(editorActionListener);
        title.setOnEditorActionListener(editorActionListener);
        editMoney.setOnEditorActionListener(editorActionListener);
    }

    protected abstract void doSubmit();

    protected abstract void fillInForm();

    protected void notifyDateDisplayRefresh() {
        EditText date = (EditText) this.findViewById(R.id.editTextDate);
        if (date != null) {
            date.setText(DateHelper.getDisplayDate(mDate));
        }
    }

    public void onClickDatePicker(View view) {
        OnDateSetListener onDateSetListener = new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                    int dayOfMonth) {
                DateHelper.setDate(mDate, year, monthOfYear, dayOfMonth);
                notifyDateDisplayRefresh();
            }
        };
        DatePickerDialog picker = new DatePickerDialog(this, onDateSetListener,
                mDate.get(Calendar.YEAR), mDate.get(Calendar.MONTH),
                mDate.get(Calendar.DAY_OF_MONTH));
        picker.setCancelable(true);
        picker.setCanceledOnTouchOutside(true);
        picker.setTitle("Date");
        picker.show();

    }

    protected void loadAutoCompleteResource() {
        SharedPreferences sharedPrefenceManger = PreferenceManager
                .getDefaultSharedPreferences(this);
        Set<String> set = sharedPrefenceManger.getStringSet("what", null);
        if (set != null)
            what = set.toArray(what);
        set = sharedPrefenceManger.getStringSet("locations", null);
        if (set != null)
            location = set.toArray(location);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.expense_edit_option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemSaveExpense:
                this.doSubmit();
                break;
            case android.R.id.home:
                Toast.makeText(getApplicationContext(), "Event not saved",
                        Toast.LENGTH_SHORT).show();
                this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
