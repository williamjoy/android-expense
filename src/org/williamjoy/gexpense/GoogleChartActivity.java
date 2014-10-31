package org.williamjoy.gexpense;

import org.williamjoy.gexpense.util.RawTextHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class GoogleChartActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
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
    }
}
