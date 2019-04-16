package com.akashapplications.technicalanna.HelperActivity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.akashapplications.technicalanna.R;
import com.akashapplications.technicalanna.Utils.Tokens;

public class PDFReader extends AppCompatActivity  {

    WebView webView;
    ProgressBar progressBar;
    String link="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfreader);
        link = getIntent().getStringExtra("link");
//        webView = findViewById(R.id.webview);
//        progressBar = findViewById(R.id.progressbar);
//
//        progressBar.setVisibility(View.VISIBLE);
//        progressBar.setIndeterminate(true);
//        progressBar.getIndeterminateDrawable().setColorFilter(
//                getResources().getColor(R.color.blue_button),
//                android.graphics.PorterDuff.Mode.SRC_IN);
//
//        String finalURL = "https://docs.google.com/gview?embedded=true&url="+link;
//
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setBuiltInZoomControls(true);
//        webView.setWebChromeClient(new WebChromeClient(){
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                super.onProgressChanged(view, newProgress);
//                if(newProgress == 100)
//                    progressBar.setVisibility(View.GONE);
//            }
//        });
//
//        webView.loadUrl(finalURL);
//
//        Log.e(Tokens.LOG,link);
        WebView mWebView=new WebView(PDFReader.this);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.loadUrl("https://docs.google.com/gview?embedded=true&url="+link);
        setContentView(mWebView);

    }


}
