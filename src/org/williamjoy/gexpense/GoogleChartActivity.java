package org.williamjoy.gexpense;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashMap;

import org.williamjoy.gexpense.googlechart.AbstractChart;
import org.williamjoy.gexpense.googlechart.ColumnChart;
import org.williamjoy.gexpense.googlechart.PieChart;
import org.williamjoy.gexpense.googlechart.RawTextHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class GoogleChartActivity extends Activity {
    public static final int PIE_CHART = 0x01;
    public static final int CLOUMN_CHART = 0x02;
    private AbstractChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.expense_report);

        SharedPreferences sharedPrefenceManger = PreferenceManager
                .getDefaultSharedPreferences(this);
        WebView webview = (WebView) this.findViewById(R.id.webViewChart);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setDisplayZoomControls(true);
        webSettings.setCacheMode(sharedPrefenceManger.getBoolean(
                "LOAD_CACHE_ONLY", false) ? WebSettings.LOAD_CACHE_ONLY
                : WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setLightTouchEnabled(true);
        final Activity activity = this;
        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                activity.setProgress(progress * 1000);
            }
        });
        webview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode,
                    String description, String failingUrl) {
                Toast.makeText(activity, "Oh no! " + description,
                        Toast.LENGTH_SHORT).show();
            }
        });

        RawTextHelper helper = new RawTextHelper(this);
        String html = helper.getRawText();

        if (getIntent().hasExtra("chart_type")) {
            mChart = new ColumnChart(html);
        } else {
            mChart = new PieChart(html);
            ((PieChart) mChart).setEnable3D(sharedPrefenceManger.getBoolean("is3D", false));
        }

        Serializable data = this.getIntent().getSerializableExtra("data");
        if (data instanceof HashMap<?, ?>) {
            mChart.setDataTable((HashMap<String, Double>) data);
        } else {
            mChart.setDataTable(data.toString());
        }
        mChart.setTitle("Expense by Type");
        Point size=new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        mChart.setWidth(size.x);
        mChart.setHeight(size.y);

        webview.loadData(mChart.getHTMLData(), "text/html", null);
        
    }

    private void viewInBrowser() {

        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {

            writeTmpFile();
            Uri uri = Uri.fromFile(tmpHTMLFile);
            if (false == startBrowserActivity(uri, "com.chrome.beta",
                    "com.google.android.apps.chrome.Main")) {
                startBrowserActivity(uri, "com.android.browser",
                        "com.android.browser.BrowserActivity");
            }
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Toast.makeText(getBaseContext(), "Media is read only!",
                    Toast.LENGTH_SHORT).show();

        } else if (Environment.MEDIA_REMOVED.equals(state)) {
            Toast.makeText(getBaseContext(), "Media Removed!",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), "Media Error!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private boolean startBrowserActivity(Uri uri, String pkg, String activity) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        try {
            browserIntent.setClassName(pkg, activity);
            browserIntent.setData(uri);
            startActivity(browserIntent);
            return true;
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getBaseContext(),
                    "com.chrome.beta package not installed", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
    }

    private File tmpHTMLFile;

    private void writeTmpFile() {
        tmpHTMLFile = new File(Environment.getExternalStorageDirectory()
                .getPath() + "/Expense/chart.html");
        try {
            if (!tmpHTMLFile.getParentFile().exists()) {
                tmpHTMLFile.getParentFile().mkdirs();
            }
            if (!tmpHTMLFile.exists()) {
                tmpHTMLFile.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(tmpHTMLFile);
            PrintWriter pw = new PrintWriter(fos);
            pw.print(mChart.getHTMLData());
            pw.flush();
            pw.close();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chart_option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.menuItemBrowser):
                viewInBrowser();
                break;
            case (R.id.menuItemDebug):
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setTitle("Source");
                    TextView tv=new TextView(getBaseContext());
                    tv.setText(mChart.getHTMLData());
                    ScrollView sv=new ScrollView(getBaseContext());
                    sv.addView(tv);
                    alert.setView(sv);
                    alert.show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
