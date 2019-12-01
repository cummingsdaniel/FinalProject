package com.marksimonyi.android.cst2335finalproject;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class Article extends AppCompatActivity{
    WebView webView;
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        WebView website = (WebView) findViewById(R.id.webview);

        website.loadUrl("www.example.com");
    }
}
