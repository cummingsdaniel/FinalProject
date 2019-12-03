package com.marksimonyi.android.cst2335finalproject.NewsApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.marksimonyi.android.cst2335finalproject.R;

/**
 * Article.java
 * Section 020
 * Daniel Cummings
 * 2019-12-02
 */
/*This is the Article activity class. */
public class Article extends Activity {
    WebView articleWebView;
    ProgressBar progressBar;
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsapp_articale_activity);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        progressBar = findViewById(R.id.newsapp_progressbar);
        articleWebView = findViewById(R.id.webView);
        Log.d("noooooo", url);
        articleWebView.getSettings().setJavaScriptEnabled(true); /*sets the webView to enable Javascript Execution*/
        articleWebView.getSettings().setLoadWithOverviewMode(true); /*sets web content to fit width wise on screen*/

        articleWebView.setWebViewClient(new WebViewClient() {


            /*sets the visability of the progress bar in front of webview*/
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                progressBar.setVisibility(View.INVISIBLE);
                view.loadUrl(url);
                return true;
            }

            /*sets the visability of the progress bar to Invisible on finished page */
            @Override
            public void onPageFinished(WebView view, final String url) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        articleWebView.loadUrl(url);

    }
}