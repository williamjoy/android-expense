package org.williamjoy.gexpense;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class GoogleChartJSActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        
        webview.loadData("<p>Hello</p>\n"+tableHeader+tableRows, "text/html", null);
        
    }
}
