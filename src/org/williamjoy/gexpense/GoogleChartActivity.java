package org.williamjoy.gexpense;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import org.williamjoy.gexpense.util.RawTextHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class GoogleChartActivity extends Activity {
    private String html_data = "NULL";
    private File tmpHTMLFile;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        Intent input = this.getIntent();
        String tableHeader=input.getStringExtra("googleDataTableHeader");
        String tableRows=input.getStringExtra("googleDataTableRows");
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

        int orientation = this.getResources().getConfiguration().orientation;
        String data=RawTextHelper.getRawTextFromResource(getApplicationContext(), R.raw.google_chart);
        data=data.replace("__::DATATABLE_HEADER::__", tableHeader);
        data=data.replace("__::DATATABLE_ROWS::__", tableRows);
        data=data.replace("__::LEGEND_POSITION::__"   , orientation==Configuration.ORIENTATION_PORTRAIT?"bottom":"right");
        data=data.replace("__::COLUMN_CHART_STYLE::__", orientation==Configuration.ORIENTATION_PORTRAIT?"width: 120%; height: 60%;":"width: 100%; height: 100%;");
        data=data.replace("__::PIE_CHART_STYLE::__"   , orientation==Configuration.ORIENTATION_PORTRAIT?"width: 120%; height: 60%;":"width: 100%; height: 100%;");
        data=data.replace("__::H_AXIS_GRIDLINES_COUNT::__", sharedPrefenceManger.getInt("history_month", 12) + "");
        webview.loadData(data, "text/html", null);
        
        String html=RawTextHelper.getRawTextFromResource(getApplicationContext(), R.raw.calendar_chart);
        html=html.replace("__::DATATABLE_HEADER::__", tableHeader);
        html=html.replace("__::DATATABLE_ROWS::__", tableRows);
        this.html_data=html;
    }
    private void writeTmpFile(String data) {
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
            pw.print(data);
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
        switch (item.getItemId()){
            case R.id.menuItemBrowser:
                writeTmpFile(this.html_data);
                viewInBrowser();
                break;
        }
        return
                true;
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
                    "com.android.chrome package not installed", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
    }
    private void viewInBrowser() {

        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {

            Uri uri = Uri.fromFile(tmpHTMLFile);
            if (false == startBrowserActivity(uri, "com.android.chrome",
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
}
